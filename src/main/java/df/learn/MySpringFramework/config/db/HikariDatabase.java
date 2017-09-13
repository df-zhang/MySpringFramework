package df.learn.MySpringFramework.config.db;
/**
 * @author zdf
 * JDBC连接池:HikariCP
 */
public enum HikariDatabase {
		APACHE("org.apache.derby.jdbc.ClientDataSource"), 
		FIREBIRD("org.firebirdsql.pool.FBSimpleDataSource"), 
		H2("org.h2.jdbcx.JdbcDataSource"), 
		HSQL("org.hsqldb.jdbc.JDBCDataSource"), 
		DB2("com.ibm.db2.jcc.DB2SimpleDataSource"), 
		INFORMIX("com.informix.jdbcx.IfxDataSource"), 
		SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDataSource"), 
		MYSQL("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"), 
		MARIADB("org.mariadb.jdbc.MySQLDataSource"), 
		ORACLE("oracle.jdbc.pool.OracleDataSource"), 
		ORIENTDB("com.orientechnologies.orient.jdbc.OrientDataSource"), 
//				POSTGRESQL("com.impossibl.postgres.jdbc.PGDataSource"), 
		POSTGRESQL("org.postgresql.ds.PGSimpleDataSource"), 
		SAPMAXDB("com.sap.dbtech.jdbc.DriverSapDB"), 
		SQLITE("org.sqlite.SQLiteDataSource"), 
		SYBASE("com.sybase.jdbc4.jdbc.SybDataSource");
		
		private String driverClassName;
		
		private HikariDatabase(String driverClassName) {
			this.driverClassName = driverClassName;
		}
		
		public String getDriverClassName() {
			return driverClassName;
		}
	}