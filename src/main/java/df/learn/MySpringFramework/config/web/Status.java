package df.learn.MySpringFramework.config.web;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName df.learn.MySpringFramework.config.web.Status
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:16:36
 * @Author 854154025@qq.com
 * 
 * @Description 状态值2000~9999
 * 
 */
public enum Status {
	// basic 2000~ 2099
	/**
	 * @Fields SUCCESS : 成功
	 */
	SUCCESS(2000, "api.success"),
	/**
	 * @Fields FAIL : 失败
	 */
	FAIL(2001, "api.fail"),
	/**
	 * @Fields INVALID_PARAM : 参数值不匹配
	 */
	PARAM_INVALID(2002, "api.param.invalid"),

	/**
	 * @Fields PARAM_LEN_TOO_SHORT : 参数值过短
	 */
	PARAM_LEN_TOO_SHORT(2004, "api.param.tooshort"),

	/**
	 * @Fields PARAM_LEN_TOO_LONG : 参数值太长
	 */
	PARAM_LEN_TOO_LONG(2005, "api.param.toolong"),

	/**
	 * @Fields AUTH_EXPIRED : 权限过期
	 */
	AUTH_EXPIRED(2006, "api.auth.expired"),

	/**
	 * @Fields NO_PERMISSION : 无权限
	 */
	NO_PERMISSION(2007, "api.nopermission"),

	/**
	 * @Fields NOT_FOUND : 未找到
	 */
	NOT_FOUND(2008, "api.notfound"),

	/**
	 * @Fields EXISTED : 已存在
	 */
	EXISTED(2009, "api.existed"),
	/**
	 * @Fields EXCEPTION : 服务器异常
	 */
	EXCEPTION(2010, "api.exception"),

	// user and login
	/**
	 * @Fields PASSWORD_INCORRECT : 密码不匹配
	 */
	PASSWORD_INCORRECT(2011, "api.password.incorrect"),

	/**
	 * @Fields MAXCODE : 最大响应code
	 */
	MAXCODE(9999, "api.maxcode");

	private final int code;
	private final String resource;

	private Status(int code, String resource) {
		this.code = code;
		this.resource = resource;
	}

	public int code() {
		return this.code;
	}

	public String resource() {
		return this.resource;
	}

	public static Status findByCode(int code) {
		for (Status rs : values()) {
			if (rs.code == code) {
				return rs;
			}
		}
		throw new IllegalArgumentException("Cannot create enum from " + code + " code!");
	}

	public static Map<Integer, String> all2Map() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Status t : Status.values()) {
			map.put(t.code, t.resource);
		}
		return map;
	}
}
