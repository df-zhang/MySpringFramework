package df.learn.MySpringFramework.modules.controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import df.learn.MySpringFramework.commons.framework.AbstractController;
import df.learn.MySpringFramework.config.web.DataTable;
import df.learn.MySpringFramework.config.web.Response;
import df.learn.MySpringFramework.config.web.Response.ResponseBuilder;
import df.learn.MySpringFramework.modules.entities.Demo;
import df.learn.MySpringFramework.modules.services.DemoService;

@Controller
// @RestController
@RequestMapping("demo")
public class DemoController extends AbstractController {
	@Resource
	private DemoService service;

	@RequestMapping("index")
	public ModelAndView index() {
		return new ModelAndView("demo");
	}

	@RequestMapping(value = "page.json", method = RequestMethod.POST)
	@ResponseBody
	public DataTable page(Demo demo) {
		return DataTable.fromPageDomain(service.findPage(demo)).setDraw(demo.getDraw());
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public Response add(Demo demo) {
		return ResponseBuilder.create().status(df.learn.MySpringFramework.config.web.Status.SUCCESS).build();
	}
}
