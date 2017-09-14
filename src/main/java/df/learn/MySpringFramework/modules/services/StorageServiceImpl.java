package df.learn.MySpringFramework.modules.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import df.learn.MySpringFramework.commons.properties.PropertyProcessor;
import df.learn.MySpringFramework.modules.storage.Storagable;
import df.learn.MySpringFramework.modules.storage.Store;

@Service
public class StorageServiceImpl implements StorageService {
	private final String bucketName = PropertyProcessor.getProperties("storage.bucket");
	@Resource
	private Storagable storage;

	@Override
	public String getType() {
		return storage.getType();
	}

	@Override
	public boolean putObject(Store store) throws IOException {
		Assert.notNull(store);
		Assert.hasText(store.getKey());
		store.setBucketName(bucketName);
		return storage.putObject(store);
	}

	@Override
	public Store getObject(String key) throws IOException {
		Assert.hasText(key);
		return storage.getObject(bucketName, key);
	}

	@Override
	public boolean exists(String key) throws IOException {
		Assert.hasText(key);
		return storage.exists(bucketName, key);
	}

	@Override
	public void deleteObject(String key) throws IOException {
		Assert.hasText(key);
		storage.deleteObject(bucketName, key);
	}

	@Override
	public List<String> deleteObjects(List<String> keys) throws IOException {
		Assert.notEmpty(keys);
		return storage.deleteObjects(bucketName, keys);
	}

	@Override
	public boolean move(String sourceKey, String destinationKey) throws IOException {
		Assert.hasText(sourceKey);
		Assert.hasText(destinationKey);
		if (sourceKey.equals(destinationKey)) {
			return true;
		}
		if (storage.copy(bucketName, sourceKey, bucketName, destinationKey)) {
			return storage.deleteObject(bucketName, sourceKey);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulive3.modules.storage.services.StorageService#copy(java.lang.String, java.lang.String)
	 * 
	 * @Methods copy
	 * 
	 * @param sourceKey
	 * 
	 * @param destinationKey
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 * @Description TODO
	 */
	@Override
	public boolean copy(String sourceKey, String destinationKey) throws IOException {
		Assert.hasText(sourceKey);
		Assert.hasText(destinationKey);
		if (sourceKey.equals(destinationKey)) {
			return true;
		}
		return storage.copy(bucketName, sourceKey, bucketName, destinationKey);
	}

	@Override
	public String getURL(String key, Date expirationTime) {
		Assert.hasText(key);
		if (!"local".equals(storage.getType())) {
			return storage.getURL(bucketName, key, expirationTime);
		}
		return "";
	}

}
