package df.learn.MySpringFramework.modules.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.modules.entities.Demo  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:13:42 
 * @Author 854154025@qq.com
 * 
 * @Description TODO
 * 
 */
@Entity
@Table(name = "tbl_demo", indexes = { @Index(columnList = "name", name = "idx_demo_name"), @Index(columnList = "title", name = "idx_demo_title") })
public class Demo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4978721953921621950L;
	private long auto;
	private Set<DemoChild> childs;
	private String feature;
	private String name;
	private String title;

	/**
	 * @return auto
	 */
	@TableGenerator(name = "seq_demo", table = "ulive_demo_seq", pkColumnName = "seqName", pkColumnValue = "demo", valueColumnName = "seqVal", initialValue = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_demo")
	public long getAuto() {
		return auto;
	}

	/**
	 * @param auto 属性赋值 auto
	 */
	public void setAuto(long auto) {
		this.auto = auto;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	public Set<DemoChild> getChilds() {
		return childs;
	}

	public String getFeature() {
		return feature;
	}

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void setChilds(Set<DemoChild> childs) {
		this.childs = childs;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
