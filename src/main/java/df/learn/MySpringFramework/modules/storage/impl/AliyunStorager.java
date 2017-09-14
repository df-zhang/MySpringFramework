package df.learn.MySpringFramework.modules.storage.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.terracotta.agent.repkg.de.schlichtherle.io.FileInputStream;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import df.learn.MySpringFramework.commons.CommonException;
import df.learn.MySpringFramework.commons.properties.PropertyProcessor;
import df.learn.MySpringFramework.modules.storage.AbstractStorager;
import df.learn.MySpringFramework.modules.storage.Store;

/**
 * 阿里云云存储SDK对接类。 阿里云返回的ETag即文件MD5值
 * 
 * @author Administrator
 * 
 */
public class AliyunStorager extends AbstractStorager {

	/**
	 * 最大连接数{@value}个
	 */
	public static final int STORAGE_MAXCONNECTIONS = 500;
	/**
	 * Socket超时时间{value}秒
	 */
	public static final int STORAGE_SOCKETTIMEOUT = 50 * 1000;
	/**
	 * 连接超时时间{value}秒
	 */
	public static final int STORAGE_CONNECTIONTIMEOUT = 50 * 1000;
	/**
	 * 失败后最大重试次数{value}次
	 */
	public static final int STORAGE_MAXERRORRETRY = 3;

	private OSSClient writeClient; // 写客户端
	private OSSClient readClient; // 读客户端

	public AliyunStorager() {
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxConnections(STORAGE_MAXCONNECTIONS);
		config.setSocketTimeout(STORAGE_SOCKETTIMEOUT);
		config.setConnectionTimeout(STORAGE_CONNECTIONTIMEOUT);
		config.setMaxErrorRetry(STORAGE_MAXERRORRETRY);

		String STORAGE_INTERNAL_ENDPOINT = PropertyProcessor.getProperties("storage.endpoint.internal");
		String STORAGE_ENDPOINT = PropertyProcessor.getProperties("storage.endpoint.external");
		String STORAGE_ACCESSKEY = PropertyProcessor.getProperties("storage.accesskey");
		String STORAGE_SECRETKEY = PropertyProcessor.getProperties("storage.secretkey");
		writeClient = new OSSClient(STORAGE_INTERNAL_ENDPOINT, STORAGE_ACCESSKEY, STORAGE_SECRETKEY, config);
		readClient = new OSSClient(STORAGE_ENDPOINT, STORAGE_ACCESSKEY, STORAGE_SECRETKEY, config);
	}

	@Override
	public String getType() {
		return "aliyun";
	}

	@Override
	public boolean putObject(Store store) throws IOException {
		Assert.notNull(store);
		Assert.hasText(store.getBucketName());
		String key = store.getKey();
		Assert.hasText(key);

		InputStream in = null;

		if (store.getObject() != null) {
			// 优先使用本地临时文件
			in = new FileInputStream(store.getObject());
		} else if (store.getObjectContent() != null) {
			// 其次查找内存中的流
			in = store.getObjectContent();
		} else {
			return false;
		}
		// 封装阿里云元数据
		ObjectMetadata metadata = new ObjectMetadata();
		if (store.getExpirationTime() != null) {
			// 设置有效期，超过有效期后阿里云会自动删除该文件
			metadata.setExpirationTime(store.getExpirationTime());
		}
		metadata.setContentLength(store.getContentLength());// 文件大小
		if (StringUtils.isNotEmpty(store.getContentDisposition())) {
			// 设置文件在浏览器下载时解析的名称
			metadata.setContentDisposition(store.getContentDisposition());
		}
		if (StringUtils.isNotEmpty(store.getContentType())) {
			// 设置文件类型
			metadata.setContentType(store.getContentType());
		}
		try {
			// 将文件存储到阿里云云存储
			PutObjectResult result = writeClient.putObject(store.getBucketName(), store.getKey(), in, metadata);
			if (result != null) {
				// 成功后返回ETag
				store.setTag(result.getETag());
			}
		} catch (Exception e) {
			logger.error("Store Object Exception  " + e.getMessage(), e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Close InputStream Exception  " + e.getMessage(), e);
				} finally {
					in = null;
				}
			}
		}
		return true;
	}

	@Override
	public Store getObject(String bucket, String key) throws IOException {
		Assert.hasText(bucket);
		Assert.hasText(key);
		try {
			// OSSClient client = new OSSClient(endpoint, accesskey, secretkey,
			// clientConfig);
			OSSObject obj = writeClient.getObject(bucket, key);
			if (obj != null) {
				Store store = new Store();
				store.setObjectContent(obj.getObjectContent());
				ObjectMetadata metadata = obj.getObjectMetadata();
				if (metadata != null) {
					store.setContentDisposition(metadata.getContentDisposition());
					store.setContentLength(metadata.getContentLength());
					store.setContentType(metadata.getContentType());
					store.setTag(metadata.getETag());
					try {
						store.setExpirationTime(metadata.getExpirationTime());
					} catch (ParseException e) {
					}
				}
				store.setKey(key);
				return store;
			}
		} catch (ServiceException e) {
			// if (OSSErrorCode.NO_SUCH_KEY.equals(e.getErrorCode())) {
			return null;
		}
		return null;
	}

	@Override
	public boolean exists(String bucket, String key) throws FileNotFoundException {
		Assert.hasText(bucket);
		Assert.hasText(key);
		try {
			OSSObject obj = readClient.getObject(bucket, key);
			InputStream inputStream = null;
			if (obj != null && (inputStream = obj.getObjectContent()) != null) {
				inputStream.close();
				inputStream = null;
				obj.setObjectContent(null);
				obj = null;
			}
		} catch (ServiceException e) {
			if (OSSErrorCode.NO_SUCH_KEY.equals(e.getErrorCode())) {
				return false;
			}
			throw new CommonException("storage failed", e);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteObject(String bucket, String key) throws IOException {
		Assert.hasText(bucket);
		Assert.hasText(key);
		writeClient.deleteObject(bucket, key);
		return true;
	}

	@Override
	public List<String> deleteObjects(String bucket, List<String> keys) throws IOException {
		Assert.hasText(bucket);
		Assert.notEmpty(keys);
		DeleteObjectsResult result = writeClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys));
		if (result != null) {
			return result.getDeletedObjects();
		}
		return new ArrayList<>();
	}

	@Override
	public boolean copy(String srcBucket, String srcKey, String dstBucket, String dstKey) throws IOException {
		if (srcKey.equals(dstKey) && srcBucket.equals(dstBucket)) {
			return true;
		}

		CopyObjectResult result = writeClient.copyObject(srcBucket, srcKey, dstBucket, dstKey);
		return result != null;
	}

	@Override
	public String getURL(String bucket, String key, Date expirationTime) {
		if (StringUtils.isEmpty(key)) {
			return "";
		}
		// 设置URL过期时间为1小时
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000L);
		return readClient.generatePresignedUrl(bucket, key, expiration).toExternalForm();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#move(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * @Methods move
	 * 
	 * @param srcBucket
	 * 
	 * @param srcKey
	 * 
	 * @param dstBucket
	 * 
	 * @param dstKey
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public boolean move(String srcBucket, String srcKey, String dstBucket, String dstKey) throws IOException {
		if (srcKey.equals(dstKey) && srcBucket.equals(dstBucket)) {
			return true;
		}
		CopyObjectResult result = writeClient.copyObject(srcBucket, srcKey, dstBucket, dstKey);
		if (result != null) {
			writeClient.deleteObject(srcBucket, srcKey);
			return true;
		}
		return false;
	}
}
