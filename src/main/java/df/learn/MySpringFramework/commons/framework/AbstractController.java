package df.learn.MySpringFramework.commons.framework;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.SessionAttributes;

import df.learn.MySpringFramework.commons.framework.web.editor.BasicTypeEditor;
import df.learn.MySpringFramework.commons.framework.web.editor.DateEditor;
import df.learn.MySpringFramework.config.web.WebConstants;

@ControllerAdvice
@SessionAttributes({ WebConstants.LOGIN_ACCOUNT })
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(int.class, new BasicTypeEditor(int.class));
		binder.registerCustomEditor(long.class, new BasicTypeEditor(long.class));
		binder.registerCustomEditor(double.class, new BasicTypeEditor(double.class));
		binder.registerCustomEditor(float.class, new BasicTypeEditor(float.class));
		binder.registerCustomEditor(char.class, new BasicTypeEditor(char.class));
		binder.registerCustomEditor(boolean.class, new BasicTypeEditor(boolean.class));
		binder.registerCustomEditor(short.class, new BasicTypeEditor(short.class));
		binder.registerCustomEditor(byte.class, new BasicTypeEditor(byte.class));
	}
}
