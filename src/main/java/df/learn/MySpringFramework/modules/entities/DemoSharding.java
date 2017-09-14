package df.learn.MySpringFramework.modules.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.modules.entities.DemoSharding  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:02:11 
 * @Author 854154025@qq.com
 * 
 * @Description JPA分表弊端，数据库中会多出一个空表。<br>
 * 此类数据需要持久化时，使用分表功能将数据保存到分表中，而不是此类映射的表
 * 
 */
@Entity
@Table(name = "tbl_demo_sharding")
public class DemoSharding extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5801806396592044092L;
	private String feature;
	private String name;
	private String title;

	public String getFeature() {
		return feature;
	}

	public String getName() {
		return name;
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

	public void setTitle(String title) {
		this.title = title;
	}

}
