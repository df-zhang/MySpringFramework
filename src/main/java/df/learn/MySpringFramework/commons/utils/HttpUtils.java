package df.learn.MySpringFramework.commons.utils;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {
	public static String getMobileUserAgent(HttpServletRequest request) {
		String[] mobileAgents = { "ipad", "iphone", "android", "phone", "mobile", "wap", "netfront", "java",
				"opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry",
				"dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola",
				"foma", "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad",
				"webos", "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips",
				"sagem", "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			for (String mobileAgent : mobileAgents) {
				if (userAgent.indexOf(mobileAgent) >= 0) {
					return mobileAgent;
				}
			}
		}
		return "";
	}

	public static final String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0) {
			// 本机测试用
			ip = request.getLocalAddr();
		}
		return ip;
	}

	public static final String getServerHost(HttpServletRequest request) {
		String host = request.getHeader("x-forwarded-host");
		if (host == null || host.length() == 0 || "unknown".equalsIgnoreCase(host)) {
			host = request.getHeader("HTTP_X_FORWARDED_HOST");
		}
		if (host == null || host.length() == 0 || "unknown".equalsIgnoreCase(host)) {
			host = request.getHeader("host");
		}
		if (host == null || host.length() == 0 || "unknown".equalsIgnoreCase(host)) {
			host = request.getServerName() + ":" + request.getServerPort();
		}
		if (host == null || host.length() == 0) {
			// 本机测试用
			host = request.getLocalAddr() + ":" + request.getLocalPort();
		}
		return host;
	}

	/**
	 * 判断ajax请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	}

	/**
	 * 判断ajax请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAjaxJSON(HttpServletRequest request) {
		return (request.getHeader("accept") != null && request.getHeader("accept").toString().contains("json"))
				&& (request.getHeader("X-Requested-With") != null
						&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	}
	
	
	/**
	 * 获取当前访问的完整的url（包含参数）
	 * 
	 * ***/
	public static String getCompleteUrl(HttpServletRequest request){
        String url = request.getScheme() +"://" + request.getServerName()  
                       // + ":" +request.getServerPort() 
                        + request.getServletPath();
        if (request.getQueryString() != null){
            url += "?" + request.getQueryString();
        }
        return url;
	}
	
	/**
	 * 是否是微信内置浏览器
	 * 
	 * ***/
	public static boolean isWeixinBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			if (userAgent.indexOf("micromessenger") >= 0) {
				return true;
			}
		}
		return false;
	}
}
