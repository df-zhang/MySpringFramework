package df.learn.MySpringFramework.commons.framework;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * @ClassName df.learn.MySpringFramework.commons.framework.BaseService  
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午6:10:45 
 * @Author 854154025@qq.com
 * 
 * @Description 业务处理
 * 
 * @param <T>
 * @param <ID>
 */
public interface BaseService<T extends BaseEntity, ID extends Serializable> {

	/**
	 * 根据ID验证对象是否已持久化。
	 * 
	 * @param id
	 * @return 已存在返回true
	 */
	boolean exists(ID id);

	/**
	 * 根据ID获得已持久化后的对象。
	 * 
	 * @param id
	 * @return
	 */
	T get(ID id);

	/**
	 * 持久化对象，存在时修改。
	 * 
	 * @param t
	 * @return
	 */
	T save(T t);

	/**
	 * 持久化多个对象，存在时做修改。
	 * 
	 * @param entities
	 * @return
	 */
	<S extends T> List<S> save(Iterable<S> entities);

	/**
	 * 获得所有持久化数据总数。
	 * 
	 * @return
	 */
	long count();

	/**
	 * 查询所有持久化数据。
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 根据Sort排序的所有持久化数据
	 * 
	 * @param sort
	 * @return
	 */
	List<T> findAll(Sort sort);

	/**
	 * 根据ID集合查找
	 * 
	 * @param ids
	 * @return
	 */
	List<T> findAll(Iterable<ID> ids);

	/**
	 * 根据Specification条件，Pageable分页条件查询。
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<T> findAll(Specification<T> spec, Pageable pageable);

	/**
	 * 查询分页，封装本系统的实体对象，实际实现为调用findAll(Specification<T> spec, Pageable pageable)
	 * 
	 * @param t
	 * @return
	 */
	Page<T> findPage(final T t);
	
	Page<T> findPage(String sql,Map<String,Object> parameters, final T t);
	long getCount(String countSql,Map<String,Object> parameters);
	List<T> getResultList(String sql,Map<String,Object> parameters, final T t);
    Page<T> findPage(String countSql,Map<String,Object> countParameters,String sql,Map<String,Object> parameters, final T t);
	/**
	 * 根据ID删除
	 * 
	 * @param id
	 */
	void delete(ID id);

	/**
	 * 删除对象
	 * 
	 * @param t
	 */
	void delete(T t);

	/**
	 * 删除多个对象
	 * 
	 * @param entities
	 */
	void delete(Iterable<T> entities);

	/**
	 * 删除全部
	 */
	void deleteAll();

	/**
	 * 同步数据到数据库，类似提交事务commit（若有事务）
	 */
	void flush();

}
