package df.learn.MySpringFramework.modules.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import df.learn.MySpringFramework.commons.framework.BaseEntity;

/**
 * @ClassName df.learn.MySpringFramework.modules.entities.DemoSeq  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:13:36 
 * @Author 854154025@qq.com
 * 
 * @Description TODO
 * 
 */
@Entity
@Table(name = "tbl_demo_seq")
public class DemoSeq extends BaseEntity{
	/**  
	 * @Fields serialVersionUID : TODO
	 */  
	private static final long serialVersionUID = 1654866537201796437L;
	private String seqName;
	private long seqVal;

	/**
	 * @return seqName
	 */
	public String getSeqName() {
		return seqName;
	}

	/**
	 * @return seqVal
	 */
	public long getSeqVal() {
		return seqVal;
	}

	/**
	 * @param seqName 属性赋值 seqName
	 */
	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	/**
	 * @param seqVal 属性赋值 seqVal
	 */
	public void setSeqVal(long seqVal) {
		this.seqVal = seqVal;
	}

}
