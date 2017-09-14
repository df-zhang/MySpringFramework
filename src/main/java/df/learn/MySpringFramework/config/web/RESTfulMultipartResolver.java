package df.learn.MySpringFramework.config.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @ClassName df.learn.MySpringFramework.config.web.RESTfulMultipartResolver  
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午4:35:28 
 * @Author 854154025@qq.com
 * 
 * @Description 自定义的MultipartResolver，支持PUT上传
 * 
 */
public class RESTfulMultipartResolver extends CommonsMultipartResolver {
	/**
	 * Constant for HTTP POST method.
	 */
	private static final String POST_METHOD = "POST";
	/**
	 * Constant for HTTP PUT method.
	 */
	private static final String PUT_METHOD = "PUT";

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		return (request != null && isMultipartContent(request));
	}

	private boolean isMultipartContent(HttpServletRequest request) {
		if (!POST_METHOD.equalsIgnoreCase(request.getMethod()) && !PUT_METHOD.equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		return FileUploadBase.isMultipartContent(new ServletRequestContext(request));
	}
}
