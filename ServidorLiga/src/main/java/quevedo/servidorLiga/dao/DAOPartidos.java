package quevedo.servidorLiga.dao;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Partido;
import quevedo.servidorLiga.dao.conexionDB.DBConnectionPool;
import quevedo.servidorLiga.dao.mappers.PartidoMapper;
import quevedo.servidorLiga.dao.utils.ConstantesDao;
import quevedo.servidorLiga.dao.utils.Querys;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Log4j2
public class DAOPartidos {

    private final DBConnectionPool dbConnectionPool;
    private final PartidoMapper partidoMapper;

    @Inject
    public DAOPartidos(DBConnectionPool dbConnectionPool, PartidoMapper partidoMapper) {
        this.dbConnectionPool = dbConnectionPool;
        this.partidoMapper = partidoMapper;
    }


    public Either<ApiError, List<Partido>> getAll() {
        Either<ApiError, List<Partido>> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.query(Querys.SELECT_FROM_PARTIDOS, partidoMapper));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;

    }

    public Either<ApiError, Partido> savePartido(Partido partido) {
        Either<ApiError, Partido> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con
                        .prepareStatement(Querys.INSERT_PARTIDO,
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, partido.getIdJornada());
                preparedStatement.setString(2, partido.getEquipoLocal().getIdEquipo());
                preparedStatement.setString(3, partido.getEquipoVisitante().getIdEquipo());
                preparedStatement.setString(4, partido.getResultado());
                return preparedStatement;
            }, keyHolder);

            partido.setIdPartido(keyHolder.getKey().toString());

            resultado = Either.right(partido);
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;


    }

    public Either<ApiError, Partido> updatePartido(Partido partido) {

        Either<ApiError, Partido> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            int actualizado = jdbcTemplate.update(Querys.UPDATE_PARTIDO, partido.getIdJornada(), partido.getEquipoLocal().getIdEquipo()
                    , partido.getEquipoVisitante().getIdEquipo(), partido.getResultado(), partido.getIdPartido());

            if (actualizado > 0) {
                Partido partidoDB = jdbcTemplate.queryForObject(Querys.SELECT_PARTIDO_POR_ID, partidoMapper, partido.getIdPartido());
                resultado = Either.right(partidoDB);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.EQUIPO_NO_ENCONTRADO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;

    }

    public Either<ApiError, String> deletePartido(String id) {

        Either<ApiError, String> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            int actualizado = jdbcTemplate.update(Querys.DELETE_PARTIDO, id);

            if (actualizado > 0) {
                resultado = Either.right(id);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.DELETE_FAIL_PARTIDO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;
    }

    public Either<ApiError, List<Partido>> filtrarEquipo(String equipo) {
        Either<ApiError, List<Partido>> resultado;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.query(Querys.FILTRO_EQUIPO, partidoMapper, equipo, equipo));

        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;
    }

    public Either<ApiError, List<Partido>> filtrarJornada(String jornada) {
        Either<ApiError, List<Partido>> resultado;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.query(Querys.FILTRO_JORNADA, partidoMapper, jornada));

        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;
    }
}
