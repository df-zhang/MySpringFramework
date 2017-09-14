package df.learn.MySpringFramework.modules.services;

import org.springframework.data.domain.Page;

import df.learn.MySpringFramework.modules.entities.DemoSharding;

public interface DemoShardingService {

	Page<DemoSharding> findPage(String table, final DemoSharding t);

	long count(String table);

	long count(String table, DemoSharding t);

	DemoSharding save(String table, DemoSharding t);

	void deleteByIdBetween(String table, long min, long max);

	DemoSharding get(String table, long id);

}
