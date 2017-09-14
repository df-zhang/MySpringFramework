package df.learn.MySpringFramework.modules.storage.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.common_utils.CommonParamCheckUtils;
import com.qcloud.cos.meta.InsertOnly;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.GetFileInputStreamRequest;
import com.qcloud.cos.request.GetFileLocalRequest;
import com.qcloud.cos.request.MoveFileRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

import df.learn.MySpringFramework.commons.properties.PropertyProcessor;
import df.learn.MySpringFramework.commons.utils.JsonUtils;
import df.learn.MySpringFramework.modules.storage.AbstractStorager;
import df.learn.MySpringFramework.modules.storage.Store;

/**
 * @ClassName com.ulive3.modules.storage.api.impl.QCloudStorager
 * 
 * @Version v1.0
 * @Date 2017年3月29日 上午3:42:01
 * @Author df.zhang@deen12.com
 * 
 * @Description TODO 注意函数{@link CommonParamCheckUtils#AssertLegalCosFilePath(String)}，key必须以/开头
 */
@Component
public class QCloudStorager extends AbstractStorager {

	private long appId;
	private String secretId;
	private String secretKey;
	private String region;
	private COSClient client;
	private Credentials cred;

	/**
	 * @Constructors QCloudStorager
	 * 
	 * 
	 * @Description TODO
	 */
	public QCloudStorager() {
		appId = PropertyProcessor.getLong("storage.appId");
		secretId = PropertyProcessor.getProperties("storage.secretId");
		secretKey = PropertyProcessor.getProperties("storage.secretKey");
		region = PropertyProcessor.getProperties("storage.region");
		// 初始化秘钥信息
		cred = new Credentials(appId, secretId, secretKey);
		// 初始化客户端配置
		ClientConfig clientConfig = new ClientConfig();
		// 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
		clientConfig.setRegion(region);
		// 初始化cosClient
		client = new COSClient(clientConfig, cred);
	}

	private void checkAndCorrectStoreParams(Store store) {
		Assert.notNull(store, "Store must not be null");
		String key = store.getKey();
		Assert.hasText(key, "Store#key must not be empty");
		store.setKey(checkKey(key));
		if (store.getObject() == null && store.getObjectContent() == null) {
			Assert.notNull(null, "Store --> object&&objectContent");
		}
	}

	private String checkKey(String key) {
		key = key.replaceAll("\\\\", "/");
		if (!key.startsWith("/")) {
			key = "/" + key;
		}
		if (key.endsWith("/")) {
			key = key.substring(0, key.length() - 1);
		}
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#getType()
	 * 
	 * @Methods getType
	 * 
	 * @return
	 * 
	 * @Description TODO
	 */
	@Override
	public String getType() {
		return "qcloud";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#putObject(com.ulive3.modules.storage.api.Store)
	 * 
	 * @Methods putObject
	 * 
	 * @param store
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public boolean putObject(Store store) throws IOException {
		checkAndCorrectStoreParams(store);
		UploadFileRequest request = null;
		long contentLength = 0L;
		if (store.getObject() != null) {
			contentLength = store.getObject().length();
			request = new UploadFileRequest(store.getBucketName(), store.getKey(), store.getObject().getAbsolutePath());
		} else if (store.getObjectContent() != null) {
			byte[] bts = IOUtils.toByteArray(store.getObjectContent());
			contentLength = bts.length;
			request = new UploadFileRequest(store.getBucketName(), store.getKey(), bts);
		}
		if (request == null) {
			return false;
		}
		request.setInsertOnly(InsertOnly.OVER_WRITE);
		if (contentLength < 10L * 1024L * 1024L * 1024L) {
			// 十兆文件 10737418240
			logger.debug("十兆文件，计算Sha {}", contentLength);
			request.setEnableShaDigest(true);// 计算sha摘要，如果开启sha，并且bucket下有相同内容文件，则会触发秒传。sha计算会耗费一定的CPU和时间，建议大文件不开启。
		}
		String jsonStr = client.uploadFile(request);// {'code':$code, 'message':$mess, 'data':$data}
		logger.debug("Upload Result : {}", jsonStr);
		if (StringUtils.isNotEmpty(jsonStr)) {
			QCloudResult result = JsonUtils.json2Obj(jsonStr, QCloudResult.class);
			if (result != null && result.getCode() == 0) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#getObject(java.lang.String, java.lang.String)
	 * 
	 * @Methods getObject
	 * 
	 * @param bucket
	 * 
	 * @param key
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public Store getObject(String bucket, String key) throws IOException {
		key = checkKey(key);
		String filePath = tempFolder + key + System.nanoTime();
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		GetFileLocalRequest request = new GetFileLocalRequest(bucket, key, filePath);
		request.setUseCDN(false);
		// getFileLocalRequest.setReferer("*.myweb.cn");//设置Referer(针对开启了refer防盗链的bucket)
		String jsonStr = client.getFileLocal(request);
		QCloudResult result = JsonUtils.json2Obj(jsonStr, QCloudResult.class);
		if (result != null && result.getCode() == 0) {
			Store store = new Store();
			store.setBucketName(bucket);
			store.setKey(key);
			store.setContentLength(file.length());
			store.setObject(file);
			return store;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#exists(java.lang.String, java.lang.String)
	 * 
	 * @Methods exists
	 * 
	 * @param bucket
	 * 
	 * @param key
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public boolean exists(String bucket, String key) throws IOException {
		key = checkKey(key);
		GetFileInputStreamRequest request = new GetFileInputStreamRequest(bucket, key);
		request.setRangeStart(0L);
		request.setRangeEnd(100L);
		// 下载100字节 判断有没有文件
		InputStream inputStream = null;
		try {
			inputStream = client.getFileInputStream(request);
			return inputStream.available() != 0;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#deleteObject(java.lang.String, java.lang.String)
	 * 
	 * @Methods deleteObject
	 * 
	 * @param bucket
	 * 
	 * @param key
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public boolean deleteObject(String bucket, String key) throws IOException {
		key = checkKey(key);
		DelFileRequest request = new DelFileRequest(bucket, key);
		String jsonStr = client.delFile(request);
		QCloudResult result = JsonUtils.json2Obj(jsonStr, QCloudResult.class);
		return result != null && result.getCode() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#deleteObjects(java.lang.String, java.util.List)
	 * 
	 * @Methods deleteObjects
	 * 
	 * @param bucket
	 * 
	 * @param keys
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public List<String> deleteObjects(String bucket, List<String> keys) throws IOException {
		List<String> results = new ArrayList<>();
		if (keys.size() > 0) {
			for (String key : keys) {
				if (deleteObject(bucket, key)) {
					results.add(key);
				}
			}
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * @Methods copy
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
	public boolean copy(String srcBucket, String srcKey, String dstBucket, String dstKey) throws IOException {
		srcKey = checkKey(srcKey);
		dstKey = checkKey(dstKey);
		if (srcBucket.equals(dstBucket) && srcKey.equals(dstKey)) {
			return true;
		}

		Store store = getObject(srcBucket, srcKey);
		if (store != null) {
			store.setBucketName(dstBucket);
			store.setKey(dstKey);
			return putObject(store);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.api.Storagable#getURL(java.lang.String, java.lang.String, java.util.Date)
	 * 
	 * @Methods getURL
	 * 
	 * @param bucket
	 * 
	 * @param key
	 * 
	 * @param expirationTime
	 * 
	 * @return
	 * 
	 * @Description TODO
	 */
	@Override
	public String getURL(String bucket, String key, Date expirationTime) {
		key = checkKey(key);
		return "http://".concat(bucket).concat("-").concat(appId + ".cos").concat(region).concat(".myqcloud.com").concat(key);
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
		srcKey = checkKey(srcKey);
		dstKey = checkKey(dstKey);
		// 如果bucket相同，直接移动
		if (srcBucket.equals(dstBucket)) {
			if (srcKey.equals(dstKey)) {
				return true;
			}
			MoveFileRequest request = new MoveFileRequest(srcBucket, srcKey, dstKey);
			String jsonStr = client.moveFile(request);
			QCloudResult result = JsonUtils.json2Obj(jsonStr, QCloudResult.class);
			return result != null && result.getCode() == 0;
		} else {
			// 如果bucket不同，获取后上传至dstBucket，并删除srcKey
			Store store = getObject(srcBucket, srcKey);
			if (store != null) {
				store.setBucketName(dstBucket);
				store.setKey(dstKey);
				if (putObject(store)) {
					return deleteObject(srcBucket, srcKey);
				}
			}
		}
		return false;
	}
}

class QCloudResult {
	private int code;
	private String message;

	/**
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param code 属性赋值 code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @param message 属性赋值 message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}