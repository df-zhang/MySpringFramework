package df.learn.MySpringFramework.commons.framework;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
/**
 * @ClassName df.learn.MySpringFramework.commons.framework.BaseRepository  
 * 
 * @Version v1.0
 * @Date 2017年9月13日 下午6:12:21 
 * @Author 854154025@qq.com
 * 
 * @Description SpringRepository持久化接口
 * 
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID>, 
	JpaSpecificationExecutor<T>{
}
