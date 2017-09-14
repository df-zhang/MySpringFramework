package df.learn.MySpringFramework.modules.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.modules.entities.DemoMessage  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:13:58 
 * @Author 854154025@qq.com
 * 
 * @Description TODO
 * 
 */
@Entity
@Table(name = "tbl_demo_message")
public class DemoMessage extends BaseEntity{

	/**  
	 * @Fields serialVersionUID : TODO
	 */  
	private static final long serialVersionUID = -2762866589250194325L;

}
