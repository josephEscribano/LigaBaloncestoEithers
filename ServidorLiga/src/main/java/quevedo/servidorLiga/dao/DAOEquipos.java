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
import quevedo.common.modelos.Equipo;
import quevedo.servidorLiga.dao.conexionDB.DBConnectionPool;
import quevedo.servidorLiga.dao.utils.ConstantesDao;
import quevedo.servidorLiga.dao.utils.Querys;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Log4j2
public class DAOEquipos {

    private final DBConnectionPool dbConnectionPool;

    @Inject
    public DAOEquipos(DBConnectionPool dbConnectionPool) {
        this.dbConnectionPool = dbConnectionPool;
    }

    public Either<ApiError, List<Equipo>> getAll() {
        Either<ApiError, List<Equipo>> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.query(Querys.SELECT_FROM_EQUIPOS, BeanPropertyRowMapper.newInstance(Equipo.class)));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;

    }

    public Either<ApiError, Equipo> saveEquipo(String nombre) {
        Either<ApiError, Equipo> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());


            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con
                        .prepareStatement(Querys.INSERT_EQUIPO,
                                Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, nombre);
                return preparedStatement;
            }, keyHolder);

            Equipo equipo = new Equipo();
            equipo.setIdEquipo(keyHolder.getKey().toString());
            equipo.setNombreEquipo(nombre);

            resultado = Either.right(equipo);
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;


    }

    public Either<ApiError, Equipo> updateEquipo(Equipo equipo) {

        Either<ApiError, Equipo> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            int actualizado = jdbcTemplate.update(Querys.UPDATE_EQUIPO, equipo.getNombreEquipo(), equipo.getIdEquipo());

            if (actualizado > 0) {
                Equipo equipoDB = jdbcTemplate.queryForObject(Querys.SELECT_EQUIPO_POR_ID, BeanPropertyRowMapper.newInstance(Equipo.class), equipo.getIdEquipo());
                resultado = Either.right(equipoDB);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.EQUIPO_NO_ENCONTRADO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;

    }

    public Either<ApiError, String> deleteEquipo(String id) {

        Either<ApiError, String> resultado;
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dbConnectionPool.getHikariDataSource());
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            jdbcTemplate.update(Querys.DELETE_PARTIDO_POR_EQUIPO, id, id);
            int actualizado = jdbcTemplate.update(Querys.DELETE_EQUIPO, id);

            if (actualizado > 0) {
                resultado = Either.right(id);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.DELETE_FAIL_EQUIPO));
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
