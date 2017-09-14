package df.learn.MySpringFramework.modules.repositories;

import java.util.Set;

import df.learn.MySpringFramework.commons.framework.BaseRepository;
import df.learn.MySpringFramework.modules.entities.Demo;

/**
 * @ClassName df.learn.MySpringFramework.modules.repositories.DemoRepository
 * 
 * @Version v1.0
 * @Date 2017年9月14日 下午1:23:38
 * @Author 854154025@qq.com
 * 
 * @Description 更多参考http://www.ibm.com/developerworks/cn/opensource/os-cn-spring-jpa/
 * 
 */
public interface DemoRepository extends BaseRepository<Demo, Long> {

	Demo findByName(String name);

	Set<Demo> findByNameLike(String name);

	Set<Demo> findByNameOrFeature(String name, String feature);

	Set<Demo> findByFeatureNot(String feature);

	Set<Demo> findByTitleIsNull();
}
