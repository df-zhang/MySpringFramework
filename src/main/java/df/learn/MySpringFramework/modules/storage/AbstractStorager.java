package df.learn.MySpringFramework.modules.storage;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import df.learn.MySpringFramework.commons.properties.PropertyProcessor;

/**
 * @ClassName df.learn.MySpringFramework.modules.storage.AbstractStorager  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午4:23:53 
 * @Author 854154025@qq.com
 * 
 * @Description 抽象存储类
 * 
 */
public abstract class AbstractStorager implements Storagable {

	protected static Logger logger = LoggerFactory.getLogger(Storagable.class);
	protected final String localBasePath;
	protected final String tempFolder;

	public AbstractStorager() {
		localBasePath = PropertyProcessor.getProperties("path.base");
		tempFolder = localBasePath + File.separator +PropertyProcessor.getProperties("path.temp");
	}

}
