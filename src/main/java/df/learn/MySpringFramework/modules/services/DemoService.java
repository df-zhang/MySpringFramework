package df.learn.MySpringFramework.modules.services;

import java.util.Set;

import df.learn.MySpringFramework.commons.framework.BaseService;
import df.learn.MySpringFramework.modules.entities.Demo;
import df.learn.MySpringFramework.modules.entities.DemoChild;

public interface DemoService extends BaseService<Demo, Long> {

	Set<DemoChild> findChildByDemo(Demo demo);
}
