/*
 * ©2016-2017 Deen12.com All Rights Reserved.
 * 
 * 【注意】深圳市德恩移动互联网络有限公司 版权所有，本内容仅限于深圳市德恩移动互联网络有限公司内部传阅，禁止外泄以及用于其他的商业目。
 * 
 * 
 * 
 *
 */
package df.learn.MySpringFramework.config.web;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import df.learn.MySpringFramework.commons.utils.JsonUtils;

/**
 * @ClassName com.ulive3.modules.api.ApiResponse
 * 
 * @Version v1.0
 * @Date 2017年2月28日 上午1:00:07
 * @Author df.zhang@deen12.com
 * 
 * @Description 国际化的Api响应数据
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ApiResponse implements Serializable {

	/**
	 * @Fields serialVersionUID : {@value}
	 */
	private static final long serialVersionUID = -1858686184021776698L;
	private int code;
	private String message;
	private Object data;

	private ApiResponse(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @Fields SUCCESS : 成功
	 */
	public static final ApiResponse SUCCESS = ApiResponseBuilder.create().status(ApiStatus.SUCCESS).build();
	/**
	 * @Fields FAIL : 失败
	 */
	public static final ApiResponse FAIL = ApiResponseBuilder.create().status(ApiStatus.FAIL).build();
	/**
	 * @Fields PARAM_INVALID : 参数值不匹配
	 */
	public static final ApiResponse PARAM_INVALID = ApiResponseBuilder.create().status(ApiStatus.PARAM_INVALID).build();
	/**
	 * @Fields PARAM_LEN_TOO_SHORT : 参数值过短
	 */
	public static final ApiResponse PARAM_LEN_TOO_SHORT = ApiResponseBuilder.create().status(ApiStatus.PARAM_LEN_TOO_SHORT).build();
	/**
	 * @Fields PARAM_LEN_TOO_LONG : 参数值过长
	 */
	public static final ApiResponse PARAM_LEN_TOO_LONG = ApiResponseBuilder.create().status(ApiStatus.PARAM_LEN_TOO_LONG).build();
	/**
	 * @Fields AUTH_EXPIRED : 授权失效，需重新登录
	 */
	public static final ApiResponse AUTH_EXPIRED = ApiResponseBuilder.create().status(ApiStatus.AUTH_EXPIRED).build();
	public static final ApiResponse NO_PERMISSION = ApiResponseBuilder.create().status(ApiStatus.NO_PERMISSION).build();
	public static final ApiResponse NOT_FOUND = ApiResponseBuilder.create().status(ApiStatus.NOT_FOUND).build();
	public static final ApiResponse EXISTED = ApiResponseBuilder.create().status(ApiStatus.EXISTED).build();
	public static final ApiResponse EXCEPTION = ApiResponseBuilder.create().status(ApiStatus.EXCEPTION).build();
	public static final ApiResponse PASSWORD_INCORRECT = ApiResponseBuilder.create().status(ApiStatus.PASSWORD_INCORRECT).build();
	/**
	 * @Fields MAXCODE : 默认最大code值
	 */
	public static final ApiResponse MAXCODE = ApiResponseBuilder.create().status(ApiStatus.MAXCODE).build();
	
	public String toJsonString() {
		return JsonUtils.toJsonString(this);
	}

	/**
	 * @ClassName com.ulive3.modules.api.ApiResponseBuilder
	 * 
	 * @Version v1.0
	 * @Date 2017年2月28日 下午11:38:48
	 * @Author df.zhang@deen12.com
	 * 
	 * @Description ApiResponseBuilder，{@link ApiResponse} 构造器，默认status=SUCCESS
	 * 
	 */
	public static class ApiResponseBuilder {
		private ApiStatus status;
		private int code;
		private String message;
		private String resource;
		private String parameter;
		private Object data;

		public static ApiResponseBuilder create() {
			return new ApiResponseBuilder();
		}

		private ApiResponseBuilder() {
		}

		public ApiStatus status() {
			return this.status;
		}

		public ApiResponseBuilder status(ApiStatus apiStatus) {
			this.status = apiStatus;
			return this;
		}

		public int code() {
			return this.code;
		}

		public ApiResponseBuilder code(int code) {
			this.code = code;
			return this;
		}

		public String message() {
			return this.message;
		}

		public ApiResponseBuilder message(String message) {
			this.message = message;
			return this;
		}

		public String resource() {
			return this.resource;
		}

		public ApiResponseBuilder resource(String resource) {
			this.resource = resource;
			return this;
		}

		public String parameter() {
			return this.parameter;
		}

		public ApiResponseBuilder parameter(String parameter) {
			this.parameter = parameter;
			return this;
		}

		public Object data() {
			return this.data;
		}

		public ApiResponseBuilder data(Object data) {
			this.data = data;
			return this;
		}

		public ApiResponse build() {
			if (code == 0) {
				if (status == null) {
					status = ApiStatus.SUCCESS;
				}
				code = status.code();
				resource = status.resource();
			}
			// 如果自定义消息为空
			if (StringUtils.isEmpty(message)) {
				// 如果resource不未空
				if (StringUtils.isNotEmpty(resource)) {
					message = resource; // TODO
				} else {
					if (status == null) {
						// 那么code 不为0
						try {
							status = ApiStatus.findByCode(code);
							resource = status.resource();
							message = resource; // TODO
						} catch (IllegalArgumentException e) {
							message = "";
						}
					}
				}
			}
			if (StringUtils.isNotEmpty(parameter)) {
				message += "{" + parameter + "}";
			}
			return new ApiResponse(code, message, data);
		}
	}
}
