package df.learn.MySpringFramework.config;

import java.math.BigDecimal;

/** 
 * @author kezhiliang E-mail: fg456hj@qq.com
 * @version create date：2017年2月8日 下午8:18:49 
 * 专用于业务逻辑的常量，不涉及system的常量
 */
public class ModuleConstants {
	/**我们公司的赚取老师的佣金 , 1 = 本金   20%是我们的佣金*/
	public static final BigDecimal PRICE_RATE = new BigDecimal("1.20");
	public static final int  APP_SCOPE_LEVEL = 4;
	public static final int  USER_GOOD_SCORE = 100;
	public static final BigDecimal MIN_PRICE= new BigDecimal("0.1");
	
	public static final String DEEN_USERNAME = "3";
	public static final String WX_USERNAME = "4";
}
