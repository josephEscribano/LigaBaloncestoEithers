package quevedo.servidorLiga.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

import jakarta.inject.Singleton;
import quevedo.servidorLiga.config.Configuration;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

@Singleton
public class DBConnectionPool {

    private DataSource hikariDataSource = null ;

    private Configuration configuration;

    @Inject
    public DBConnectionPool(Configuration configuration){
        this.configuration = configuration;
        hikariDataSource = getDataSourceHikari();
    }

    private DataSource getDataSourceHikari() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(this.configuration.getRuta());
        config.setUsername(this.configuration.getUser());
        config.setPassword(this.configuration.getPassword());
        config.setDriverClassName(this.configuration.getDriver());
        config.setMaximumPoolSize(1);

        config.addDataSourceProperty(ConstantesDao.CACHE_PREP_STMTS, ConstantesDao.VALUE_CACHE);
        config.addDataSourceProperty(ConstantesDao.PREP_STMT_CACHE_SIZE, ConstantesDao.VALUE_PREP_SIZE);
        config.addDataSourceProperty(ConstantesDao.PREP_STMT_CACHE_SQL_LIMIT, ConstantesDao.VALUE_PREP_LIMIT);

        return new HikariDataSource(config);
    }

    public DataSource getHikariDataSource() {
        return hikariDataSource;
    }

    @PreDestroy
    public void closePool()
    {
        ((HikariDataSource)hikariDataSource).close();
    }
}
