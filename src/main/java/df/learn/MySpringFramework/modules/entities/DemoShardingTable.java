package df.learn.MySpringFramework.modules.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @author zdf
 * 分表管理表
 */
@Entity
@Table(name = "tbl_demo_sharding_table", indexes = {
		@Index(columnList = "hash", name = "idx_demosharingtable_name") })
public class DemoShardingTable extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3172327287993463699L;
	private String name;
	private int createDate;
	private String hash; //SHA1
	private int disable = 1;
	private long disableTime;

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public int getCreateDate() {
		return createDate;
	}

	public String getHash() {
		return hash;
	}

	public int getDisable() {
		return disable;
	}

	public long getDisableTime() {
		return disableTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreateDate(int createDate) {
		this.createDate = createDate;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}

	public void setDisableTime(long disableTime) {
		this.disableTime = disableTime;
	}

}
