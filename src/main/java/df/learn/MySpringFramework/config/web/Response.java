package df.learn.MySpringFramework.config.web;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import df.learn.MySpringFramework.commons.utils.JsonUtils;

/**
 * @ClassName df.learn.MySpringFramework.config.web.Response
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:16:51
 * @Author 854154025@qq.com
 * 
 * @Description 国际化的Api响应数据
 * 
 */
@JsonInclude(Include.NON_NULL)
public class Response implements Serializable {

	/**
	 * @Fields serialVersionUID : {@value}
	 */
	private static final long serialVersionUID = -1858686184021776698L;
	private int code;
	private String message;
	private Object data;

	private Response(int code, String message, Object data) {
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
	public static final Response SUCCESS = ResponseBuilder.create().status(Status.SUCCESS).build();
	/**
	 * @Fields FAIL : 失败
	 */
	public static final Response FAIL = ResponseBuilder.create().status(Status.FAIL).build();
	/**
	 * @Fields PARAM_INVALID : 参数值不匹配
	 */
	public static final Response PARAM_INVALID = ResponseBuilder.create().status(Status.PARAM_INVALID).build();
	/**
	 * @Fields PARAM_LEN_TOO_SHORT : 参数值过短
	 */
	public static final Response PARAM_LEN_TOO_SHORT = ResponseBuilder.create().status(Status.PARAM_LEN_TOO_SHORT).build();
	/**
	 * @Fields PARAM_LEN_TOO_LONG : 参数值过长
	 */
	public static final Response PARAM_LEN_TOO_LONG = ResponseBuilder.create().status(Status.PARAM_LEN_TOO_LONG).build();
	/**
	 * @Fields AUTH_EXPIRED : 授权失效，需重新登录
	 */
	public static final Response AUTH_EXPIRED = ResponseBuilder.create().status(Status.AUTH_EXPIRED).build();
	public static final Response NO_PERMISSION = ResponseBuilder.create().status(Status.NO_PERMISSION).build();
	public static final Response NOT_FOUND = ResponseBuilder.create().status(Status.NOT_FOUND).build();
	public static final Response EXISTED = ResponseBuilder.create().status(Status.EXISTED).build();
	public static final Response EXCEPTION = ResponseBuilder.create().status(Status.EXCEPTION).build();
	public static final Response PASSWORD_INCORRECT = ResponseBuilder.create().status(Status.PASSWORD_INCORRECT).build();
	/**
	 * @Fields MAXCODE : 默认最大code值
	 */
	public static final Response MAXCODE = ResponseBuilder.create().status(Status.MAXCODE).build();

	public String toJsonString() {
		return JsonUtils.toJsonString(this);
	}

	/**
	 * @ClassName com.ulive3.modules.api.ResponseBuilder
	 * 
	 * @Version v1.0
	 * @Date 2017年2月28日 下午11:38:48
	 * 
	 * 
	 * @Description ResponseBuilder，{@link Response} 构造器，默认status=SUCCESS
	 * 
	 */
	public static class ResponseBuilder {
		private Status status;
		private int code;
		private String message;
		private String resource;
		private String parameter;
		private Object data;

		public static ResponseBuilder create() {
			return new ResponseBuilder();
		}

		private ResponseBuilder() {
		}

		public Status status() {
			return this.status;
		}

		public ResponseBuilder status(Status apiStatus) {
			this.status = apiStatus;
			return this;
		}

		public int code() {
			return this.code;
		}

		public ResponseBuilder code(int code) {
			this.code = code;
			return this;
		}

		public String message() {
			return this.message;
		}

		public ResponseBuilder message(String message) {
			this.message = message;
			return this;
		}

		public String resource() {
			return this.resource;
		}

		public ResponseBuilder resource(String resource) {
			this.resource = resource;
			return this;
		}

		public String parameter() {
			return this.parameter;
		}

		public ResponseBuilder parameter(String parameter) {
			this.parameter = parameter;
			return this;
		}

		public Object data() {
			return this.data;
		}

		public ResponseBuilder data(Object data) {
			this.data = data;
			return this;
		}

		public Response build() {
			if (code == 0) {
				if (status == null) {
					status = Status.SUCCESS;
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
							status = Status.findByCode(code);
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
			return new Response(code, message, data);
		}
	}
}
