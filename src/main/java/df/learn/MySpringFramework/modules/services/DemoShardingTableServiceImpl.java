package df.learn.MySpringFramework.modules.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import df.learn.MySpringFramework.commons.framework.AbstractService;
import df.learn.MySpringFramework.commons.framework.BaseRepository;
import df.learn.MySpringFramework.commons.utils.SecondClock;
import df.learn.MySpringFramework.modules.entities.DemoShardingTable;
import df.learn.MySpringFramework.modules.repositories.DemoShardingTableRepository;

@Service
public class DemoShardingTableServiceImpl extends AbstractService<DemoShardingTable, Long> implements DemoShardingTableService {

	@Resource
	private DemoShardingTableRepository repository;
	@PersistenceContext
	private EntityManager em;
	@Resource
	private DemoShardingService demoShardingService;

	@Override
	protected BaseRepository<DemoShardingTable, Long> getRepository() {
		return repository;
	}

	@Override
	// @CachePut(value = "DemoShardingTable", key = "#t.hash")
	public DemoShardingTable save(DemoShardingTable t) {
		return super.save(t);
	}

	@Override
	public void createByNameIfNotExist(String name) {
		String hash = DigestUtils.sha1Hex(name);
		DemoShardingTable tbl = findEnabledByhash(hash);
		String subfix = name.replaceAll("-", "").toLowerCase();
		String tableName = "sewise_devicecapture_".concat(subfix);
		if (tbl == null) {
			em.flush();
			tbl = new DemoShardingTable();
			tbl.setName(tableName);
			tbl.setHash(hash);
			tbl.setCreateDate(Integer.parseInt(DateFormatUtils.format(SecondClock.getNow(), "yyyyMMdd")));
			save(tbl);
		}
		em.createNativeQuery(new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (").append("id bigint(20) NOT NULL AUTO_INCREMENT, ").append("attr1 varchar(255) DEFAULT NULL,").append("attr2 varchar(255) DEFAULT NULL, ")
				.append("createTime bigint(20) DEFAULT 0, ").append("time int(11) DEFAULT 0, ").append("type int(11) DEFAULT 1, ").append("uri varchar(255) DEFAULT NULL, ").append("uploadTime bigint(20) DEFAULT 0, ").append("PRIMARY KEY (id), ")
				.append(" KEY sdx_typetime_devicecapture").append(subfix).append(" (type, time), ").append(" KEY sdx_time_devicecapture").append(subfix).append(" (time) ").append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;").toString()).executeUpdate();
		em.flush();
	}

	@Override
	public void createByName(String name) {
		String hash = DigestUtils.sha1Hex(name);
		DemoShardingTable tbl = findEnabledByhash(hash);
		if (tbl != null) {
			deleteTable(tbl);
			flush();
		}
		em.flush();
		String subfix = name.replaceAll("-", "").toLowerCase();
		String tableName = new StringBuilder("sewise_devicecapture_").append(subfix).toString();
		tbl = new DemoShardingTable();
		tbl.setName(tableName);
		tbl.setHash(hash);
		tbl.setCreateDate(Integer.parseInt(DateFormatUtils.format(SecondClock.getNow(), "yyyyMMdd")));
		save(tbl);
		em.createNativeQuery(new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (").append("id bigint(20) NOT NULL AUTO_INCREMENT, ").append("feature varchar(255) DEFAULT NULL,").append("name varchar(255) DEFAULT NULL, ")
				.append("title varchar(255) DEFAULT NULL, ").append("PRIMARY KEY (id), ").append(" KEY idx_demosharding_name").append(subfix).append(" (name)").append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;").toString()).executeUpdate();
		em.flush();
	}

	@Override
	public void disable(String sn) {
		disableByHash(DigestUtils.sha1Hex(sn));
	}

	@Override
	public void deleteEmptyTableByName(String name) {
		String hash = DigestUtils.sha1Hex(name);
		List<DemoShardingTable> tables = repository.findByHashAndDisable(hash, 0);
		List<DemoShardingTable> deletable = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(tables)) {
			Object count = null;
			for (DemoShardingTable tbl : tables) {
				count = demoShardingService.count(tbl.getName());
				if (count != null && Long.parseLong(count.toString()) > 0L) {
					continue;
				}
				dropByName(tbl.getName());
				deletable.add(tbl);
			}
			if (CollectionUtils.isNotEmpty(tables)) {
				repository.deleteInBatch(deletable);
			}
		}
	}

	@Override
	public void deleteTable(DemoShardingTable t) {
		if (t == null) {
			return;
		}
		if (demoShardingService.count(t.getName()) > 0) {
			disableByHash(t.getHash());
		} else {
			dropByName(t.getName());
			repository.delete(t);
		}
	}

	public void dropByName(String name) {
		em.createNativeQuery(new StringBuilder("DROP TABLE IF EXISTS ").append(name).toString()).executeUpdate();
	}

	@Override
	// @CachePut(value = "DemoShardingTable", key = "#hash")
	public DemoShardingTable findEnabledByhash(String hash) {
		List<DemoShardingTable> tables = repository.findByHashAndDisable(hash, 1);
		if (CollectionUtils.isNotEmpty(tables)) {
			return tables.get(0);
		}
		return null;
	}

	@Override
	// @CacheEvict(value = "DemoShardingTable", key = "#hash")
	public void disableByHash(String hash) {
		DemoShardingTable tbl = findEnabledByhash(hash);
		if (tbl != null) {
			StringBuilder builder = new StringBuilder();
			long curTime = System.currentTimeMillis();
			String oldTblName = tbl.getName();
			String newTblName = builder.append(oldTblName).append("_").append(curTime).toString();
			tbl.setDisable(1);
			tbl.setDisableTime(curTime);
			tbl.setName(newTblName);
			repository.save(tbl);
			builder.delete(0, builder.length());
			em.createNativeQuery(builder.append("ALTER TABLE ").append(oldTblName).append(" RENAME ").append(newTblName).toString()).executeUpdate();
		}
	}

	@Override
	public List<DemoShardingTable> findAllEnabled() {
		return repository.findAll(new Specification<DemoShardingTable>() {

			@Override
			public Predicate toPredicate(Root<DemoShardingTable> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("disable"), 0);
			}
		});
	}

	@Override
	// @CacheEvict(value = "DemoShardingTable", key = "#hash")
	public void deleteByHash(String hash) {
		deleteTable(findEnabledByhash(hash));
	}

	@Override
	public List<DemoShardingTable> findAllDisabled() {
		return repository.findAll(new Specification<DemoShardingTable>() {

			@Override
			public Predicate toPredicate(Root<DemoShardingTable> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("disable"), 1);
			}
		});
	}

	@Override
	public void deleteByName(String table) {
		deleteTable(repository.findByName(table));
	}
}
