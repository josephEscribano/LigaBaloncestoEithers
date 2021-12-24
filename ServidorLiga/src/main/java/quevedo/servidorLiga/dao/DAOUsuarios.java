package quevedo.servidorLiga.dao;


import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.servidorLiga.dao.modelos.Usuario;
import quevedo.servidorLiga.dao.utils.Querys;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
public class DAOUsuarios {
    private final DBConnectionPool dbConnectionPool;


    @Inject
    public DAOUsuarios(DBConnectionPool dbConnectionPool) {
        this.dbConnectionPool = dbConnectionPool;

    }

    public Either<ApiError, List<Usuario>> getAll() {
        Either<ApiError, List<Usuario>> resultado;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        try {
            resultado = Either.right(jdbcTemplate.query(Querys.SELECT_FROM_USUARIOS, BeanPropertyRowMapper.newInstance(Usuario.class)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION, LocalDate.now()));
        }
        return resultado;
    }

    public Either<ApiError,Usuario> saveUsuario(Usuario usuario) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());

        Either<ApiError,Usuario> resultado;
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con
                        .prepareStatement(Querys.INSERT_USUARIO_REGISTRO,
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1,usuario.getUserName());
                preparedStatement.setString(2,usuario.getCorreo());
                preparedStatement.setString(3,usuario.getPass());
                preparedStatement.setString(4,usuario.getCodActivacion());
                preparedStatement.setBoolean(5,usuario.isConfirmacion());
                preparedStatement.setString(6,usuario.getIdTipoUsuario());
                preparedStatement.setObject(7,usuario.getFechaLimite());
                return preparedStatement;
            },keyHolder);

            usuario.setIdUsuario(keyHolder.getKey().toString());

            resultado = Either.right(usuario);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION, LocalDate.now()));
        }


        return resultado;


    }

    public int activacion(String codigo){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        return jdbcTemplate.update(Querys.UPDATE_CONFIRMACION
                ,1,codigo);

    }

    public String getName(String codigo){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        return jdbcTemplate.queryForObject(Querys.SELECT_NAME_POR_CODIGO, String.class,codigo);
    }

    public int checkUserNameAndEmail(String userName, String correo){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());

        return jdbcTemplate.queryForObject(Querys.CHECK_USERNAME_CORREO, Integer.class,userName,correo);

    }

    public boolean checkTime(LocalDateTime fecha,String codigo){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        LocalDateTime fechaLimite = jdbcTemplate.queryForObject(Querys.SELECT_FECHA_LIMITE,LocalDateTime.class,codigo);

        return fecha.isBefore(fechaLimite);
    }

    public int changeDate(LocalDateTime fecha,String codigo){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        return jdbcTemplate.update(Querys.CAMBIAR_FECHA,fecha,codigo);
    }

    public Either<ApiError, Usuario> updateUsuario(UsuarioDTO usuarioDTO){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        Either<ApiError,Usuario> resultado;
        int actualizado = jdbcTemplate.update(Querys.UPDATE_USUARIOS,usuarioDTO.getUserName(),usuarioDTO.getCorreo(),usuarioDTO.getIdUsuario());

        if (actualizado > 0){
            Usuario usuario = jdbcTemplate.queryForObject(Querys.SELECT_USUARIO_POR_ID,BeanPropertyRowMapper.newInstance(Usuario.class),usuarioDTO.getIdUsuario());
            resultado = Either.right(usuario);
        }else{
            resultado = Either.left(new ApiError(ConstantesDao.USUARIO_NO_ENCONTRADO,LocalDate.now()));
        }

        return resultado;

    }

    public Either<String,String> deleteUsuario(String id){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
        Either<String,String> resultado;
        int actualizado = jdbcTemplate.update(Querys.DELETE_USUARIO,id);

        if (actualizado > 0){
            resultado = Either.right(id);
        }else{
            resultado = Either.left(ConstantesDao.USUARIO_NO_ELIMINADO);
        }

        return resultado;
    }

}
