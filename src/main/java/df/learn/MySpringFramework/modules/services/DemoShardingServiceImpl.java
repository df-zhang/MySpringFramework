package df.learn.MySpringFramework.modules.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import df.learn.MySpringFramework.modules.entities.DemoSharding;

@Service
@Transactional
public class DemoShardingServiceImpl implements DemoShardingService {
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public Page<DemoSharding> findPage(String table, final DemoSharding t) {
		StringBuilder builder = new StringBuilder("SELECT COUNT(id) FROM ").append(table);
		List<String> conditions = new ArrayList<String>();
		List<Object> parameters = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(t.getName())) {
			conditions.add("name = ?");
			parameters.add(t.getName());
		}

		if (StringUtils.isNotEmpty(t.getFeature())) {
			conditions.add("feature = ?");
			parameters.add(t.getFeature());
		}

		if (StringUtils.isNotEmpty(t.getTitle())) {
			conditions.add("title = ?");
			parameters.add(t.getTitle());
		}

		// build sql
		if (conditions.size() > 0) {
			builder.append(" WHERE ");
			for (String condition : conditions) {
				builder.append(condition).append(" AND ");
			}
			builder.delete(builder.length() - " AND ".length(), builder.length());
		}
		Query query = em.createNativeQuery(builder.toString());
		if (parameters.size() > 0) {
			int i = 1;
			for (Object obj : parameters) {
				query.setParameter(i++, obj);
			}
		}
		// id, attr1, attr2, createTime, type, uri
		long count = 0L;
		try {
			Object result = query.getSingleResult();
			if (result != null) {
				count = Long.parseLong(result.toString());
			}
		} catch (NoResultException e) {
		}

		if (count != 0L) {
			List<DemoSharding> content = new ArrayList<DemoSharding>();
			int idx = 0;
			builder.replace(idx = builder.indexOf("COUNT(id)"), idx + "COUNT(id)".length(), "id, attr1, attr2, createTime, type, uri, time, uploadTime");
			if (StringUtils.isEmpty(t.getSort())) {
				t.setSort("ID");
			}
			if (StringUtils.isEmpty(t.getOrder())) {
				t.setOrder(DemoSharding.DESC);
			}
			builder.append(" ORDER BY ").append(t.getSort()).append(" ").append(t.getOrder());
			query = em.createNativeQuery(builder.toString(), DemoSharding.class).setFirstResult(t.getPage() * t.getRows()).setMaxResults(t.getRows());

			if (parameters.size() > 0) {
				int i = 1;
				for (Object obj : parameters) {
					query.setParameter(i++, obj);
				}
			}
			content.addAll(query.getResultList());
			return new PageImpl<>(content, new PageRequest(t.getPage(), t.getRows()), count);
		}
		return new PageImpl<>(new ArrayList<DemoSharding>(), new PageRequest(t.getPage(), t.getRows()), 0);
	}

	@Override
	public DemoSharding save(String table, DemoSharding t) {
		Assert.notNull(t);
		Query query = em.createNativeQuery("INSERT INTO ".concat(table).concat("(feature, name, title ) VALUES(?, ?, ?)")).setParameter(1, t.getFeature()).setParameter(2, t.getName()).setParameter(3, t.getTitle());
		query.executeUpdate();
		try {
			Object id = em.createNativeQuery("select last_insert_id()").getSingleResult();
			if (id != null) {
				t.setId(Long.parseLong(id.toString()));
			}
		} catch (NoResultException e) {
		}
		return t;
	}

	@Override
	public long count(String table) {
		StringBuilder builder = new StringBuilder("SELECT COUNT(id) FROM ").append(table);
		try {
			Object result = em.createNativeQuery(builder.toString()).getSingleResult();
			if (result != null) {
				return Long.parseLong(result.toString());
			}
		} catch (NoResultException e) {
			return 0L;
		}
		return 0L;
	}

	@Override
	public long count(String table, DemoSharding t) {
		StringBuilder builder = new StringBuilder("SELECT COUNT(id) FROM ").append(table);
		List<String> conditions = new ArrayList<String>();
		List<Object> parameters = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(t.getName())) {
			conditions.add("name = ?");
			parameters.add(t.getName());
		}

		if (StringUtils.isNotEmpty(t.getFeature())) {
			conditions.add("feature = ?");
			parameters.add(t.getFeature());
		}

		if (StringUtils.isNotEmpty(t.getTitle())) {
			conditions.add("title = ?");
			parameters.add(t.getTitle());
		}

		// build sql
		if (conditions.size() > 0) {
			builder.append(" WHERE ");
			for (String condition : conditions) {
				builder.append(condition).append(" AND ");
			}
			builder.delete(builder.length() - " AND ".length(), builder.length());
		}
		Query query = em.createNativeQuery(builder.toString());
		if (parameters.size() > 0) {
			int i = 1;
			for (Object obj : parameters) {
				query.setParameter(i++, obj);
			}
		}
		long count = 0L;
		try {
			Object result = query.getSingleResult();
			if (result != null) {
				count = Long.parseLong(result.toString());
			}
		} catch (NoResultException e) {
		}
		return count;
	}

	@Override
	public void deleteByIdBetween(String table, long min, long max) {
		em.createNativeQuery("DELETE FROM ".concat(table).concat(" WHERE id BETWEEN ").concat(Long.toString(min)).concat(" AND ").concat(Long.toString(max)).toString()).executeUpdate();
	}

	@Override
	public DemoSharding get(String table, long id) {
		try {
			Object object = em.createNativeQuery("SELECT id, feature, name, title FROM ".concat(table).concat(" WHERE id=?"), DemoSharding.class).setParameter(1, id).getSingleResult();
			if (object != null && object instanceof DemoSharding) {
				return (DemoSharding) object;
			}
		} catch (NoResultException e) {
			return null;
		}
		return null;
	}

}
