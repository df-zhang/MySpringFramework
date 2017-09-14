package df.learn.MySpringFramework.config;

import java.util.Properties;

import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import df.learn.MySpringFramework.commons.properties.PropertyProcessor;
import df.learn.MySpringFramework.config.db.HibernateJpaIsolationSupportDialect;
import df.learn.MySpringFramework.config.db.HikariDatabase;

@Configuration
@Description("use jpa")
@EnableAspectJAutoProxy
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = ApplicationConfiguration.PACKAGES_REPOSITORIES)
public class DataSourceConfiguration {

	/**
	 * 数据库类型： DEFAULT, DB2, DERBY, H2, HSQL, INFORMIX, MYSQL, ORACLE, POSTGRESQL, SQL_SERVER, SYBASE
	 */
	private String db_type = PropertyProcessor.getProperties("db.type");// = "MYSQL";
	private String db_url = PropertyProcessor.getProperties("db.url");// =
	private String db_uname = PropertyProcessor.getProperties("db.uname");// = "root";
	private String db_upwd = PropertyProcessor.getProperties("db.upwd");// = "123";

	private String dbconfig_driverClass = PropertyProcessor.getProperties("dbconfig.driverClass");
	// 关闭连接时，是否提交未提交的事务，默认为false，即关闭连接，回滚未提交的事务
	// private boolean dbconfig_autoCommitOnClose = Boolean.parseBoolean(PropertyProcessor.getProperties("dbconfig.autoCommitOnClose"));
	// 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException，如设为0则无限期等待。单位毫秒。默认: 0
	// private int dbconfig_checkoutTimeout = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.checkoutTimeout"));
	// 初始化连接池中的连接数，取值应在minPoolSize与maxPoolSize之间，默认为3
	// private int dbconfig_initialPoolSize = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.initialPoolSize"));
	// 连接池中保留的最小连接数，默认为：3
	// private int dbconfig_minPoolSize = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.minPoolSize"));
	// 连接池中保留的最大连接数。默认值: 15
	private String dbconfig_maxPoolSize = PropertyProcessor.getProperties("dbconfig.maxPoolSize");
	// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。默认值: 3
	// private int dbconfig_acquireIncrement = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.acquireIncrement"));
	// 重新尝试的时间间隔，默认为：1000毫秒
	// private int dbconfig_acquireRetryDelay = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.acquireRetryDelay"));
	// private int dbconfig_acquireRetryAttempts = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.acquireRetryAttempts"));
	// 如果为false，则获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常，但是数据源仍有效保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试获取连接失败后该数据源将申明已断开并永久关闭。默认: false
	// private boolean dbconfig_breakAfterAcquireFailure = Boolean.parseBoolean(PropertyProcessor.getProperties("dbconfig.breakAfterAcquireFailure"));
	// private int dbconfig_maxConnectionAge = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.maxConnectionAge"));
	// 最大空闲时间，60秒内未使用则连接被丢弃。若为0则永不丢弃。默认值: 0
	private String dbconfig_maxIdleTime = PropertyProcessor.getProperties("dbconfig.maxIdleTime");
	// private int dbconfig_maxIdleTimeExcessConnections = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.maxIdleTimeExcessConnections"));
	// c3p0全局的PreparedStatements缓存的大小。如果maxStatements与maxStatementsPerConnection均为0，则缓存不生效，只要有一个不为0，则语句的缓存就能生效。如果默认值: 0
	private String dbconfig_maxStatements = PropertyProcessor.getProperties("dbconfig.maxStatements");
	// maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。默认值: 0 -->
	// 每60秒检查所有连接池中的空闲连接。默认值: 0，不检查
	// private int dbconfig_idleConnectionTestPeriod = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.idleConnectionTestPeriod"));
	// 如果设为true那么在取得连接的同时将校验连接的有效性。Default: false
	// private boolean dbconfig_testConnectionOnCheckin = Boolean.parseBoolean(PropertyProcessor.getProperties("dbconfig.testConnectionOnCheckin"));
	// private boolean dbconfig_testConnectionOnCheckout = Boolean.parseBoolean(PropertyProcessor.getProperties("dbconfig.testConnectionOnCheckout"));
	// private int dbconfig_unreturnedConnectionTimeout = Integer.parseInt(PropertyProcessor.getProperties("dbconfig.unreturnedConnectionTimeout"));

	public Database getDatabase() {
		return Database.valueOf(db_type.toUpperCase());
	}

	public Properties getProperties() {
		Properties PROPERTIES = new Properties();
		// jpaProperties
		PROPERTIES.put("hibernate.dialect", PropertyProcessor.getProperties("hibernate.dialect"));
		PROPERTIES.put("hibernate.temp.use_jdbc_metadata_defaults", false);
		PROPERTIES.put("hibernate.show_sql", false);
		PROPERTIES.put("hibernate.format_sql", false);
		PROPERTIES.put("hibernate.max_fetch_depth", 100);
		PROPERTIES.put("hibernate.jdbc.fetch_size", 3);
		PROPERTIES.put("hibernate.jdbc.batch_size", 20);
		PROPERTIES.put("hibernate.hbm2ddl.auto", "update");

		// PROPERTIES.put("hibernate.c3p0.validate", true);
		// PROPERTIES.put("hibernate.connection.driver_class", dbconfig_driverClass);
		// PROPERTIES.put("hibernate.connection.url", db_url);
		// PROPERTIES.put("hibernate.connection.username", db_uname);
		// PROPERTIES.put("hibernate.connection.password", db_upwd);
		// PROPERTIES.put("hibernate.c3p0.min_size", dbconfig_minPoolSize);
		// PROPERTIES.put("hibernate.c3p0.max_size", dbconfig_maxPoolSize - 5);
		// PROPERTIES.put("hibernate.c3p0.timeout", 1800);
		// PROPERTIES.put("hibernate.c3p0.max_statements", dbconfig_maxStatements);

		PROPERTIES.put("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
		PROPERTIES.put("hibernate.hikari.minimumIdle", dbconfig_maxIdleTime);
		PROPERTIES.put("hibernate.hikari.maximumPoolSize", dbconfig_maxPoolSize);
		PROPERTIES.put("hibernate.hikari.idleTimeout", dbconfig_maxIdleTime);
		PROPERTIES.put("hibernate.hikari.dataSourceClassName", HikariDatabase.valueOf(db_type).getDriverClassName());
		PROPERTIES.put("hibernate.hikari.dataSource.url", db_url);
		PROPERTIES.put("hibernate.hikari.dataSource.user", db_uname);
		PROPERTIES.put("hibernate.hikari.dataSource.password", db_upwd);
		PROPERTIES.put("hibernate.hikari.dataSource.cachePrepStmts", "true");
		PROPERTIES.put("hibernate.hikari.dataSource.prepStmtCacheSize", dbconfig_maxStatements);
		PROPERTIES.put("hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");

		PROPERTIES.put("hibernate.cache.use_second_level_cache", true);
		PROPERTIES.put("hibernate.cache.use_query_cache", true);
		PROPERTIES.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		PROPERTIES.put("hibernate.generate_statistics", true);
		PROPERTIES.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
		PROPERTIES.put("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
		PROPERTIES.put("hibernate.cache.provider_configuration", "/ehcache.xml");
		return PROPERTIES;
	}

	@Bean
	@Description("HikariDataSource")
	public DataSource dataSource() {
		// HikariConfig config = new HikariConfig();

		// config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

		// config.addDataSourceProperty("serverName", "localhost");
		// config.addDataSourceProperty("port", "3306");
		// config.addDataSourceProperty("databaseName", "mydb");
		// config.addDataSourceProperty("user", "bart");
		// config.addDataSourceProperty("password", "51mp50n");

		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(dbconfig_driverClass);
		dataSource.setJdbcUrl(db_url);
		dataSource.setUsername(db_uname);
		dataSource.setPassword(db_upwd);
		// dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		// dataSource.setAutoCommit(dbconfig_autoCommitOnClose);
		// dataSource.setConnectionTimeout(dbconfig_checkoutTimeout);
		dataSource.setIdleTimeout(Long.parseLong(dbconfig_maxIdleTime) * 1000L);
		dataSource.setMaximumPoolSize(Integer.parseInt(dbconfig_maxPoolSize));
		// dataSource.setJdbcUrl(db_url);
		// dataSource.setUser(db_uname);
		// dataSource.setPassword(db_upwd);
		// 关闭连接时，是否提交未提交的事务，默认为false，即关闭连接，回滚未提交的事务
		// dataSource.setAutoCommitOnClose(dbconfig_autoCommitOnClose);
		// 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException，如设为0则无限期等待。单位毫秒。默认: 0
		// dataSource.setCheckoutTimeout(dbconfig_checkoutTimeout);
		// 初始化连接池中的连接数，取值应在minPoolSize与maxPoolSize之间，默认为3
		// dataSource.setInitialPoolSize(dbconfig_initialPoolSize);
		// 连接池中保留的最小连接数，默认为：3
		// dataSource.setMinPoolSize(dbconfig_minPoolSize);
		// 连接池中保留的最大连接数。默认值: 15
		// dataSource.setMaxPoolSize(dbconfig_maxPoolSize);
		// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。默认值: 3
		// dataSource.setAcquireIncrement(dbconfig_acquireIncrement);

		// dataSource.setAcquireRetryAttempts(dbconfig_acquireRetryAttempts);
		// 重新尝试的时间间隔，默认为：1000毫秒
		// dataSource.setAcquireRetryDelay(dbconfig_acquireRetryDelay);
		// 如果为false，则获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常，但是数据源仍有效保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试获取连接失败后该数据源将申明已断开并永久关闭。默认: false
		// dataSource.setBreakAfterAcquireFailure(dbconfig_breakAfterAcquireFailure);
		// dataSource.setMaxConnectionAge(dbconfig_maxConnectionAge);
		// 最大空闲时间，60秒内未使用则连接被丢弃。若为0则永不丢弃。默认值: 0
		// dataSource.setMaxIdleTime(dbconfig_maxIdleTime);

		// dataSource.setMaxIdleTimeExcessConnections(dbconfig_maxIdleTimeExcessConnections);
		// c3p0全局的PreparedStatements缓存的大小。如果maxStatements与maxStatementsPerConnection均为0，则缓存不生效，只要有一个不为0，则语句的缓存就能生效。如果默认值: 0
		// dataSource.setMaxStatements(dbconfig_maxStatements);
		// maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。默认值: 0 -->
		// 每60秒检查所有连接池中的空闲连接。默认值: 0，不检查
		// dataSource.setIdleConnectionTestPeriod(dbconfig_idleConnectionTestPeriod);
		// 如果设为true那么在取得连接的同时将校验连接的有效性。Default: false
		// dataSource.setTestConnectionOnCheckin(dbconfig_testConnectionOnCheckin);
		// dataSource.setTestConnectionOnCheckout(dbconfig_testConnectionOnCheckout);
		// dataSource.setUnreturnedConnectionTimeout(dbconfig_unreturnedConnectionTimeout);
		return dataSource;
	}

	@Bean
	public PersistenceProvider persistenceProvider() {
		return new HibernatePersistenceProvider();
	}

	/**  
	 * @Methods jpaDialect  
	 * 
	 * @return 
	 * 
	 * @Description 让JPA支持Hibernate事务
	 */
	@Bean
	public JpaDialect jpaDialect() {
		return new HibernateJpaIsolationSupportDialect();
	}

	// @Bean
	// public JpaVendorAdapter jpaVendorAdapter() {
	// HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
	// adapter.setDatabase(getDatabase());
	// return adapter;
	// }

	// @Bean
	// public LocalSessionFactoryBean localSessionFactoryBean() {
	// LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
	// bean.setDataSource(dataSource());
	// bean.setPackagesToScan(ApplicationConfiguration.PACKAGE_BEANS);
	// bean.setHibernateProperties(getProperties());
	// bean.setCacheRegionFactory(cacheRegionFactory());
	// return bean;
	// }

	// @Bean
	// public RegionFactory cacheRegionFactory() {
	// return new SingletonEhCacheRegionFactory();
	// }

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPersistenceUnitName(ApplicationConfiguration.APP_NAME);
		factory.setPersistenceProvider(persistenceProvider());
		factory.setJpaProperties(getProperties());
		factory.setJpaDialect(jpaDialect());
		// factory.setJpaVendorAdapter(jpaVendorAdapter());
		factory.setPackagesToScan(ApplicationConfiguration.PACKAGE_BEANS);
		return factory;
	}

	@Bean(name = "transactionManager")
	public JpaTransactionManager transactionManager() {
		while (entityManagerFactory().getObject() == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		JpaTransactionManager manager = new JpaTransactionManager(entityManagerFactory().getObject());
		return manager;
	}

	@Bean
	public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
		while (entityManagerFactory().getObject() == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
		interceptor.setEntityManagerFactory(entityManagerFactory().getObject());
		return interceptor;
	}

	// @Bean
	// public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
	// return new PersistenceAnnotationBeanPostProcessor();
	// }
}
