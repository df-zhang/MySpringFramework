package df.learn.MySpringFramework.config.web;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.config.web.DataTable  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午4:35:58 
 * @Author 854154025@qq.com
 * 
 * @Description 发往页面上的分页数据
 * 
 */
public class DataTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1319691880211083298L;
	private Object data;
	private long recordsTotal;
	private long recordsFiltered;
	private int totalPages = 1;
	private String draw;
	
	

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public Object getData() {
		return data;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public String getDraw() {
		return draw;
	}

	public DataTable setData(Object data) {
		this.data = data;
		return this;
	}

	public DataTable setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
		return this;
	}

	public DataTable setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
		return this;
	}

	public DataTable setDraw(String draw) {
		this.draw = draw;
		return this;
	}

	public static DataTable fromPageDomain(Page<? extends BaseEntity> page) {
		DataTable dt = new DataTable();
		dt.data = page.getContent();
		dt.recordsTotal = page.getTotalElements();
		dt.recordsFiltered = page.getTotalElements();
		dt.totalPages = page.getTotalPages();
		return dt;
	}
}
