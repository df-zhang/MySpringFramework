package df.learn.MySpringFramework.modules.services;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import df.learn.MySpringFramework.commons.framework.AbstractService;
import df.learn.MySpringFramework.commons.framework.BaseRepository;
import df.learn.MySpringFramework.modules.entities.Demo;
import df.learn.MySpringFramework.modules.entities.DemoChild;
import df.learn.MySpringFramework.modules.repositories.DemoChildRepository;
import df.learn.MySpringFramework.modules.repositories.DemoRepository;

/**
 * @author Administrator
 *
 */
@Service
public class DemoServiceImpl extends AbstractService<Demo, Long> implements DemoService {

	@Resource
	private DemoRepository repository;

	@Resource
	private DemoChildRepository childRepository;

	@Override
	protected BaseRepository<Demo, Long> getRepository() {
		return repository;
	}

	@Override
	public Set<DemoChild> findChildByDemo(Demo demo) {
		return childRepository.findByParent(demo);
	}

}
