package df.learn.MySpringFramework.modules.repositories;

import java.util.List;

import df.learn.MySpringFramework.commons.framework.BaseRepository;
import df.learn.MySpringFramework.modules.entities.DemoShardingTable;

public interface DemoShardingTableRepository extends BaseRepository<DemoShardingTable, Long> {

	List<DemoShardingTable> findByHash(String hash);

	List<DemoShardingTable> findByHashAndDisable(String hash, int disable);

	DemoShardingTable findByName(String name);

}
