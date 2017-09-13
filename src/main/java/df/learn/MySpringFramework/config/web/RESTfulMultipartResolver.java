package df.learn.MySpringFramework.config.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * For upload file with PUT method
 * @author zdf
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
