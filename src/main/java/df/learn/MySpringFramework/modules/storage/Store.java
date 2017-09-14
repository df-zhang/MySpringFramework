package df.learn.MySpringFramework.modules.storage;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.compress.utils.IOUtils;

/**
 * @ClassName df.learn.MySpringFramework.modules.storage.Store
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午4:23:07
 * @Author 854154025@qq.com
 * 
 * @Description 文件存储对象，描述文件基本特性。包含文件流，文件类型，文件大小，默认文件名，文件相对路径，存储标签，存储到期时间，目录，文件名和下载路径。<br>
 * 
 */
public class Store implements Closeable {
	/**
	 * @Fields bucketName : 对应云存储bucketName
	 */
	private String bucketName;
	/**
	 * @Fields object : 文件，若存在服务器硬盘中（一般在临时目录下），返回File对象。
	 */
	private File object;
	/**
	 * @Fields objectContent : 文件流，可能为ByteArrayInputStream和FileInputStream
	 */
	private InputStream objectContent;
	/**
	 * @Fields contentType : 文件类型，如Image/jpg
	 */
	private String contentType;
	/**
	 * @Fields contentLength : 文件大小
	 */
	private long contentLength;
	/**
	 * @Fields contentDisposition : 下载文件名描述
	 */
	private String contentDisposition;
	/**
	 * @Fields key : 对应云存储相对路径
	 */
	private String key;
	/**
	 * @Fields tag : 通常为文件MD5值
	 */
	private String tag;
	/**
	 * @Fields expirationTime : 有效期，默认永久有效
	 */
	private Date expirationTime;
	/**
	 * @Fields url : 下载地址
	 */
	private URL url;
	/**
	 * @Fields uploadTime : 上传时间
	 */
	private long uploadTime;

	public String getBucketName() {
		return bucketName;
	}

	public File getObject() {
		return object;
	}

	public InputStream getObjectContent() {
		return objectContent;
	}

	public String getContentType() {
		return contentType;
	}

	public long getContentLength() {
		return contentLength;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public String getKey() {
		return key;
	}

	public String getTag() {
		return tag;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public URL getUrl() {
		return url;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public void setObject(File object) {
		this.object = object;
	}

	public void setObjectContent(InputStream objectContent) {
		this.objectContent = objectContent;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Override
	public String toString() {
		return "StorageObject [bucketName=" + bucketName + ", contentType=" + contentType + ", contentLength=" + contentLength + ", contentDisposition=" + contentDisposition + ", key=" + key + ", tag=" + tag + ", expirationTime=" + expirationTime + ", url="
				+ url + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 * 
	 * @Methods close
	 * 
	 * @throws IOException
	 * 
	 * @Description 对象使用完成后请主动关闭对象
	 */
	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(getObjectContent());
	}
}