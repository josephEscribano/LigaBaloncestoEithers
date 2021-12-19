package quevedo.servidorLiga.dao;


import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Usuario;

import java.net.ConnectException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Log4j2
public class DAOUsuarios {
    private final DBConnectionPool dbConnectionPool;

    @Inject
    public DAOUsuarios(DBConnectionPool dbConnectionPool) {
        this.dbConnectionPool = dbConnectionPool;
    }

    public Either<ApiError, List<Usuario>> getAll(){
        Either<ApiError,List<Usuario>> resultado;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        try{
            resultado = Either.right(jdbcTemplate.query("select * from usuarios", BeanPropertyRowMapper.newInstance(Usuario.class)));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultado = Either.left(new ApiError("Ha ocurrido un error al conectarse a la base de datos", LocalDate.now()));
        }
        return resultado;
    }
}
