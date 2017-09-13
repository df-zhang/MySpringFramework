package df.learn.MySpringFramework.commons.framework.web.editor;

import java.beans.PropertyEditorSupport;

/**
 * @ClassName df.learn.MySpringFramework.commons.framework.web.editor.BasicTypeEditor
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午6:09:06
 * @Author 854154025@qq.com
 * 
 * @Description Spring-MVC在解析HTTP请求时，将参数转换成对应类型。
 * 
 */
public class BasicTypeEditor extends PropertyEditorSupport {
	private Class<?> clazz;

	public BasicTypeEditor(Class<?> clazz) {
		basicType(clazz);
		this.clazz = clazz;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.equals("")) {
			text = "0";
		}
		if (clazz == int.class) {
			setValue(Integer.parseInt(text));
		} else if (clazz == double.class) {
			setValue(Double.parseDouble(text));
		} else if (clazz == float.class) {
			setValue(Float.parseFloat(text));
		} else if (clazz == long.class) {
			setValue(Long.parseLong(text));
		} else if (clazz == short.class) {
			setValue(Short.parseShort(text));
		} else if (clazz == boolean.class) {
			setValue(Boolean.parseBoolean(text));
		} else if (clazz == char.class) {
			setValue(text.charAt(0));
		} else if (clazz == byte.class) {
			setValue(Byte.parseByte(text));
		} else {
			throw new IllegalArgumentException(clazz + " is not a base type");
		}
	}

	@Override
	public String getAsText() {
		return getValue().toString();
	}

	private boolean basicType(Class<?> clazz) {
		if (clazz == int.class || clazz == double.class || clazz == float.class || clazz == long.class || clazz == short.class || clazz == boolean.class || clazz == char.class || clazz == byte.class)
			return true;
		else
			throw new IllegalArgumentException(clazz + " is not a base type");
	}
}
