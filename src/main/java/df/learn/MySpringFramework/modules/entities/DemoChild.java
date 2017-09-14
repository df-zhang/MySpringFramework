package df.learn.MySpringFramework.modules.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.modules.entities.DemoChild  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:13:48 
 * @Author 854154025@qq.com
 * 
 * @Description TODO
 * 
 */
@Entity
@Table(name = "tbl_demo_child", indexes = { @Index(columnList = "name", name = "idx_demochild_name"),
		@Index(columnList = "title", name = "idx_demochild_title") })
public class DemoChild extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4523040379306010660L;
	private String feature;
	private String name;
	private int number;
	private Demo parent;
	private String title;

	public String getFeature() {
		return feature;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent", unique = true, nullable = false)
	public Demo getParent() {
		return parent;
	}

	public String getTitle() {
		return title;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setParent(Demo parent) {
		this.parent = parent;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
