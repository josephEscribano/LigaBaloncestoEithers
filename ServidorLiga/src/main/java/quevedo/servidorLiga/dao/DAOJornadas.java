package quevedo.servidorLiga.dao;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Jornada;
import quevedo.servidorLiga.dao.conexionDB.DBConnectionPool;
import quevedo.servidorLiga.dao.utils.ConstantesDao;
import quevedo.servidorLiga.dao.utils.Querys;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Log4j2
public class DAOJornadas {

    private final DBConnectionPool dbConnectionPool;


    @Inject
    public DAOJornadas(DBConnectionPool dbConnectionPool) {
        this.dbConnectionPool = dbConnectionPool;
    }

    public Either<ApiError, List<Jornada>> getAll() {
        Either<ApiError, List<Jornada>> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.query(Querys.SELECT_FROM_JORNADAS, BeanPropertyRowMapper.newInstance(Jornada.class)));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;

    }

    public Either<ApiError, Jornada> saveJornada(Jornada jornada) {
        Either<ApiError, Jornada> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());


            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con
                        .prepareStatement(Querys.INSERT_JORNADA,
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, jornada.getIdJornada());
                preparedStatement.setObject(2, jornada.getFechaJornada());
                return preparedStatement;
            }, keyHolder);

            jornada.setIdJornada(keyHolder.getKey().toString());

            resultado = Either.right(jornada);
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;


    }

    public Either<ApiError, Jornada> updateJornada(Jornada jornada) {

        Either<ApiError, Jornada> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            int actualizado = jdbcTemplate.update(Querys.UPDATE_JORNADA, jornada.getFechaJornada(), jornada.getIdJornada());

            if (actualizado > 0) {
                Jornada joranadaDB = jdbcTemplate.queryForObject(Querys.SELECT_JORNADA_POR_ID, BeanPropertyRowMapper.newInstance(Jornada.class), jornada.getIdJornada());
                resultado = Either.right(joranadaDB);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.EQUIPO_NO_ENCONTRADO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;

    }

    public Either<ApiError, String> deleteJornada(String id) {

        Either<ApiError, String> resultado;
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dbConnectionPool.getHikariDataSource());
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            jdbcTemplate.update(Querys.DELETE_PARTIDO_POR_JORNADA, id);
            int actualizado = jdbcTemplate.update(Querys.DELETE_JORNADA, id);

            if (actualizado > 0) {
                resultado = Either.right(id);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.DELETE_FAIL_JORNADA));
            }

            transactionManager.commit(txStatus);
        } catch (CannotGetJdbcConnectionException e) {
            transactionManager.rollback(txStatus);
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;
    }
}
