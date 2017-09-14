package df.learn.MySpringFramework.modules.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import df.learn.MySpringFramework.commons.framework.BaseRepository;
import df.learn.MySpringFramework.modules.entities.Demo;
import df.learn.MySpringFramework.modules.entities.DemoChild;

public interface DemoChildRepository extends BaseRepository<DemoChild, Long> {
	/**
	 * 根据parent查找DemoChild集合，parent为DemoChild中的属性。<br>
	 * SQL:select * from DemoChild where parent = ?1
	 * @param parent
	 * @return
	 */
	Set<DemoChild> findByParent(Demo parent);

	@Query("select dc.parent from DemoChild dc where dc.number=?1")
	Demo findDemoByNumber(int number);

	@Modifying
	@Query("update DemoChild set number = ?2 where id = ?1")
	void updateNumber(long id, int number);
}
