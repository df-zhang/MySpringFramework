package df.learn.MySpringFramework.config.web.interceptors;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GlobalInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(GlobalInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String username = "Anonymous User";
		if (session != null) {
		}
		// 日志打印
		Map<String, String[]> map = request.getParameterMap();
		StringBuilder info = new StringBuilder(session.getId()).append("<").append(username).append("> do ").append(request.getMethod()).append(", ").append(request.getServletPath()).append(", params: [");
		if (map != null && map.size() > 0) {
			for (Entry<String, String[]> entry : map.entrySet()) {
				info.append(entry.getKey()).append("=");
				for (String val : entry.getValue()) {
					info.append(val).append(" ");
				}
				info.delete(info.length() - 1, info.length()).append(", ");
			}
			info.delete(info.length() - 2, info.length());
		}
		info.append("]");
		logger.debug(info);
		return super.preHandle(request, response, handler);
	}
}
