package df.learn.MySpringFramework.modules.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.modules.entities.DemoFile  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:13:53 
 * @Author 854154025@qq.com
 * 
 * @Description TODO
 * 
 */
@Entity
@Table(name = "tbl_demo_file")
public class DemoFile extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7734982754845769807L;
	private String uri;
	private String location;
	private String contentType;
	private long contentLength;
	private String contentDisposition;
	private String storageTag;
	private long expiresTime;
	private int downloads;
	private long uploadTime;

	public String getUri() {
		return uri;
	}

	public String getLocation() {
		return location;
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

	public String getStorageTag() {
		return storageTag;
	}

	public long getExpiresTime() {
		return expiresTime;
	}

	public int getDownloads() {
		return downloads;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public void setStorageTag(String storageTag) {
		this.storageTag = storageTag;
	}

	public void setExpiresTime(long expiresTime) {
		this.expiresTime = expiresTime;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

}
