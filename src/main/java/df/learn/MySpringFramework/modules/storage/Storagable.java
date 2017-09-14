package df.learn.MySpringFramework.modules.storage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName df.learn.MySpringFramework.modules.storage.Storagable
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午4:23:30
 * @Author 854154025@qq.com
 * 
 * @Description 文件存储接口定义
 * 
 */
public interface Storagable {

	/**
	 * 返回当前存储类型。 如：aliyun、local、qcloud、amazon s3
	 * 
	 * @return Storage type
	 */
	String getType();

	/**
	 * 上传文件，当文件夹不存在时自动创建文件夹，当文件名存在时覆盖。
	 * 
	 * @param {@link Store} 文件存储对象
	 * @return 成功返回true，失败返回false
	 */
	boolean putObject(Store store) throws IOException;

	/**
	 * 根据key获取文件，如advert/d41d8cd98f00b204e9800998ecf8427e,即获得默认主目录下的广告宣传图
	 * 
	 * @param key 包含相对路径的文件名
	 * @return {@link Store} 文件存储对象
	 */
	Store getObject(String bucket, String key) throws IOException;

	/**
	 * 检查文件是否存在，如果存在返回true，否则返回false
	 * 
	 * @param key 包含相对路径的文件名
	 * @return 存在返回true，不存在返回false
	 */
	boolean exists(String bucket, String key) throws IOException;

	/**
	 * 删除文件，如advert/d41d8cd98f00b204e9800998ecf8427e
	 * 
	 * @param key 包含相对路径的文件名
	 * @return 成功返回true，失败返回false
	 */
	boolean deleteObject(String bucket, String key) throws IOException;

	/**
	 * 删除多个文件，每次删除的文件数不多于1000
	 * 
	 * @param keys 包含相对路径的文件名集合
	 * 
	 * @return 返回删除成功的文件列表
	 */
	List<String> deleteObjects(String bucket, List<String> keys) throws IOException;

	/**
	 * 修改名称，如advert/d41d8cd98f00b204e9800998ecf8427e修改为advert/default_advert_1
	 * 
	 * @param srcKey 包含相对路径的原文件名
	 * @param dstKey 包含相对路径的修改文件名
	 * @return 成功返回true，失败返回false
	 */
	boolean copy(String srcBucket, String srcKey, String dstBucket, String dstKey) throws IOException;

	/**
	 * 修改名称，如advert/d41d8cd98f00b204e9800998ecf8427e修改为advert/default_advert_1
	 * 
	 * @param srcKey 包含相对路径的原文件名
	 * @param dstKey 包含相对路径的修改文件名
	 * @return 成功返回true，失败返回false
	 */
	boolean move(String srcBucket, String srcKey, String dstBucket, String dstKey) throws IOException;

	/**
	 * 获取文件下载路径URL,如http://smarthome.yayi-sz.com/file/download/advert/ d41d8cd98f00b204e9800998ecf8427e
	 * 
	 * @param key 包含相对路径的原文件名
	 * @param expirationTime URL到期时间
	 * @return 文件下载路径URL,如http://smarthome.yayi-sz.com/file/download/advert/ d41d8cd98f00b204e9800998ecf8427e
	 */
	String getURL(String bucket, String key, Date expirationTime);

}
