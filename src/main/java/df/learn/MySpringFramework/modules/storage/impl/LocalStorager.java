package df.learn.MySpringFramework.modules.storage.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.terracotta.agent.repkg.de.schlichtherle.io.FileInputStream;

import df.learn.MySpringFramework.modules.storage.AbstractStorager;
import df.learn.MySpringFramework.modules.storage.Store;

/**
 * 
 * @author Administrator
 * 
 */
public class LocalStorager extends AbstractStorager {

	@Override
	public boolean putObject(Store store) throws IOException {
		Assert.notNull(store);
		String bucket = store.getBucketName();
		String filePath = localBasePath;
		if (StringUtils.isNotEmpty(bucket)) {
			filePath = filePath.concat(File.separator).concat(bucket);
		}
		String key = store.getKey();
		Assert.hasText(bucket);
		Assert.hasText(key);

		File file = new File(filePath, key);
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		if (file.exists()) {
			// 文件覆盖相同
			file.delete();
		}
		BufferedInputStream in = null;
		try {
			if (store.getObject() != null) {
				in = new BufferedInputStream(new FileInputStream(store.getObject()));
			} else if (store.getObjectContent() != null) {
				in = new BufferedInputStream(store.getObjectContent());
			} else {
				return false;
			}
			FileUtils.copyInputStreamToFile(in, file);
			if (store.getContentLength() != file.length()) {
				store.setContentLength(file.length());
			}
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(in);
		}
		return true;
	}

	@Override
	public boolean exists(String bucket, String key) {
		Assert.hasText(bucket);
		Assert.hasText(key);
		return new File(localBasePath.concat(File.separator).concat(bucket), key).exists();
	}

	@Override
	public boolean deleteObject(String bucket, String key) throws IOException {
		Assert.hasText(bucket);
		Assert.hasText(key);
		File file = new File(localBasePath.concat(File.separator).concat(bucket), key);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return true;
	}

	@Override
	public List<String> deleteObjects(String bucket, List<String> keys) throws IOException {
		Assert.hasText(bucket);
		Assert.notEmpty(keys);
		List<String> deleted = new ArrayList<>();
		for (String key : keys) {
			if (deleteObject(bucket, key)) {
				deleted.add(key);
			}
		}
		return deleted;
	}

	@Override
	public boolean copy(String srcBucket, String srcKey, String dstBucket, String dstKey) throws IOException {
		File src = new File(localBasePath.concat(File.separator).concat(srcBucket), srcKey);
		File dst = new File(localBasePath.concat(File.separator).concat(dstBucket), dstKey);
		if (src.exists() && src.isDirectory()) {
			// 文件不存在时抛出异常
			throw new FileNotFoundException(src + " not file!");
		}
		try {
			FileUtils.copyFile(src, dst);
		} catch (IOException e) {
			throw e;
		}
		return true;
	}

	@Override
	public Store getObject(String bucket, String key) throws IOException {
		Assert.hasText(bucket);
		Assert.hasText(key);
		File file = new File(new StringBuilder(localBasePath).append(File.separator).append(bucket).toString(), key);
		if (file.exists()) {
			Store store = new Store();
			store.setObject(file);
			store.setBucketName(bucket);
			store.setObjectContent(new FileInputStream(file));
			store.setContentLength(file.length());
			store.setKey(key);
			return store;
		}
		return new Store();
	}

	@Override
	public String getType() {
		return "local";
	}

	@Override
	public String getURL(String bucket, String key, Date expirationTime) {
		throw new UnsupportedOperationException("local");
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
		File src = new File(localBasePath.concat(File.separator).concat(srcBucket), srcKey);
		File dst = new File(localBasePath.concat(File.separator).concat(dstBucket), dstKey);
		if (src.exists() && src.isDirectory()) {
			// 文件不存在时抛出异常
			throw new FileNotFoundException(src + " not file!");
		}
		try {
			FileUtils.moveFile(src, dst);
		} catch (IOException e) {
			throw e;
		}
		return false;
	}

	// private static boolean deleteDir(File dir) {
	// if (dir.isDirectory()) {
	// String[] children = dir.list();
	// // 递归删除目录中的子目录下
	// for (int i = 0; i < children.length; i++) {
	// boolean success = deleteDir(new File(dir, children[i]));
	// if (!success) {
	// return false;
	// }
	// }
	// }
	// // 目录此时为空，可以删除
	// return dir.delete();
	// }
}
