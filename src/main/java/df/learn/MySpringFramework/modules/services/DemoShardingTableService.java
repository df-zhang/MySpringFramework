package df.learn.MySpringFramework.modules.services;

import java.util.List;

import df.learn.MySpringFramework.commons.framework.BaseService;
import df.learn.MySpringFramework.modules.entities.DemoShardingTable;

public interface DemoShardingTableService extends BaseService<DemoShardingTable, Long> {
	
	void createByNameIfNotExist(String name);

	void createByName(String name);

	void disable(String name);

	void disableByHash(String hash);

	void deleteEmptyTableByName(String name);

	DemoShardingTable findEnabledByhash(String hash);

	List<DemoShardingTable> findAllEnabled();

	List<DemoShardingTable> findAllDisabled();

	void deleteByHash(String hash);

	void deleteByName(String table);
	
	void deleteTable(DemoShardingTable t);

}