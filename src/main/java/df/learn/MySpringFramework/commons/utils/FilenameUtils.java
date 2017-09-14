package df.learn.MySpringFramework.commons.utils;

public final class FilenameUtils {

	/**
	 * 获得文件名，返回最后一个点之前的字符串
	 * 
	 * @param key
	 * @return
	 */
	public static String getName(String key) {
		int idx1 = key.lastIndexOf("/");
		int idx2 = key.lastIndexOf('.');
		if (idx1 < 0 && idx2 < 0) {
			return key;
		}
		if (idx1 < 0) {
			return key.substring(0, idx2);
		}
		if (idx2 < 0) {
			return key.substring(idx1 + 1);
		}
		return key.substring(idx1 + 1, idx2);
	}

	/**
	 * 获得文件路径，不包括文件名
	 * 
	 * @param key
	 * @return
	 */
	public static String getLocation(String key) {
		int idx = key.lastIndexOf("/");
		if (idx > -1) {
			return key.substring(0, idx);
		}
		return "";
	}

	public static String getOriginalFilename(String key) {
		int idx = key.lastIndexOf("/");
		if (idx > -1) {
			return key.substring(idx + 1);
		}
		return key;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param key
	 * @return
	 */
	public static String getExtention(String key) {
		int idx = key.lastIndexOf(".");
		if (idx > -1) {
			return key.substring(idx + 1);
		}
		return "";
	}
	
	
	/**
	 * @Methods getExtension
	 * 
	 * @param contentType
	 * @return *.xxx ==> .xxx; 如xxx.jpeg返回.jpg
	 * 
	 * @Description TODO
	 */
	public static final String getExtensionDotInclude(String filename) {
		int idx = filename.lastIndexOf(".");
		if (idx > -1) {
			return filename.substring(idx);
		}
		return "";
	}
}
