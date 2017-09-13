package df.learn.MySpringFramework.commons.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName com.ulive3.commons.framework.AbstractService
 * 
 * @Version v1.0
 * @Date 2017年3月17日 下午11:13:46
 * @Author 84154025@qq.com
 * 
 * @Description 抽象实现的基本业务类，处理最基本的增删改查业务。 <br>
 * 关于事务：抽象类中默认使用支持事务，事务不存在时创建事务。 <br>
 * {@link Transactional#propagation()}(propagation = Propagation.REQUIRED)
 * 
 * @param <T> 继承自BaseEntity
 * @param <ID> ID类型，本项目中使用Long
 */
@Transactional(propagation = Propagation.REQUIRED)
public abstract class AbstractService<T extends BaseEntity, ID extends Serializable> implements BaseService<T, ID> {
	protected final Logger logger = Logger.getLogger(this.getClass());

	protected abstract BaseRepository<T, ID> getRepository();

	protected EntityManager getEntityManager() {
		return null;
	};

	@Override
	public boolean exists(ID id) {
		return getRepository().exists(id);
	}

	@Override
	public T get(ID id) {
		return getRepository().findOne(id);
	}

	@Override
	public T save(T t) {
		return getRepository().save(t);
	}

	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		return getRepository().save(entities);
	}

	@Override
	public long count() {
		return getRepository().count();
	}

	@Override
	public List<T> findAll() {
		return getRepository().findAll();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return getRepository().findAll(sort);
	}

	@Override
	public List<T> findAll(Iterable<ID> ids) {
		return getRepository().findAll(ids);
	}

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return getRepository().findAll(spec, pageable);
	}

	@Override
	public void delete(ID id) {
		T t = this.get(id);
		if (t != null) {
			this.delete(t);
		}
	}

	@Override
	public void delete(T t) {
		getRepository().delete(t);
	}

	@Override
	public void delete(Iterable<T> entities) {
		getRepository().deleteInBatch(entities);
	}

	@Override
	public void deleteAll() {
		// getRepository().deleteAll();
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<T> findPage(final T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getCount(String countSql, Map<String, Object> parameters) {
		EntityManager em = getEntityManager();
		Query query = em.createNativeQuery(countSql);
		if (parameters != null) {
			for (String param : parameters.keySet()) {
				query.setParameter(param, parameters.get(param));
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

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getResultList(String sql, Map<String, Object> parameters, final T t) {
		EntityManager em = getEntityManager();
		Query query = null;
		query = em.createNativeQuery(sql, t.getClass());
		// query = em.createNativeQuery(sql)
		query.setFirstResult(t.getPage() * t.getRows()).setMaxResults(t.getRows());
		if (parameters != null) {
			for (String param : parameters.keySet()) {
				query.setParameter(param, parameters.get(param));
			}
		}
		List<T> content = new ArrayList<T>();
		content.addAll(query.getResultList());
		return content;
	}

	@Override
	public Page<T> findPage(String sql, Map<String, Object> parameters, final T t) {
		// EntityManager em = getEntityManager();
		String countSql = "select count(1) " + sql.substring(sql.indexOf("from"));
		return findPage(countSql, parameters, sql, parameters, t);
	}

	@Override

	public Page<T> findPage(String countSql, Map<String, Object> countParameters, String sql, Map<String, Object> parameters, final T t) {
		long count = getCount(countSql, countParameters);
		if (count != 0L) {
			List<T> content = getResultList(sql, parameters, t);
			return new PageImpl<>(content, new PageRequest(t.getPage(), t.getRows()), count);
		}
		return new PageImpl<>(new ArrayList<T>(), new PageRequest(t.getPage(), t.getRows()), 0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void flush() {
		getRepository().flush();
	}

}
