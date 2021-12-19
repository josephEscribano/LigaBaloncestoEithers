package quevedo.servidorLiga.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import quevedo.servidorLiga.config.Configuration;

import javax.sql.DataSource;

@ApplicationScoped
@Log4j2
public class DBConnectionPool {

    private final DataSource hikariDataSource;
//    @Inject
//    private Configuration configuration;

    public DBConnectionPool(){
        hikariDataSource = getDataSourceHikari();
    }

    private DataSource getDataSourceHikari() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://dam2.mysql.iesquevedo.es:3335/joseph_escribano_baloncesto?serverTimeZone=GMT+1&useSSL=false");
        config.setUsername("root");
        config.setPassword("quevedo2dam");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(1);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        config.setJdbcUrl(configuration.getRuta());
//        config.setUsername(configuration.getUser());
//        config.setPassword(configuration.getPassword());
//        config.setDriverClassName(configuration.getDriver());
//        config.setMaximumPoolSize(1);
//
//        config.addDataSourceProperty(ConstantesDao.CACHE_PREP_STMTS, ConstantesDao.VALUE_CACHE);
//        config.addDataSourceProperty(ConstantesDao.PREP_STMT_CACHE_SIZE, ConstantesDao.VALUE_PREP_SIZE);
//        config.addDataSourceProperty(ConstantesDao.PREP_STMT_CACHE_SQL_LIMIT, ConstantesDao.VALUE_PREP_LIMIT);

        return new HikariDataSource(config);
    }

    public DataSource getHikariDataSource() {
        return hikariDataSource;
    }

    public void closePool()
    {
        ((HikariDataSource)hikariDataSource).close();
    }
}
