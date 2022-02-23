package quevedo.servidorLiga.dao;


import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.UsuarioUpdateDTO;
import quevedo.servidorLiga.dao.conexionDB.DBConnectionPool;
import quevedo.servidorLiga.dao.modelos.Usuario;
import quevedo.servidorLiga.dao.utils.ConstantesDao;
import quevedo.servidorLiga.dao.utils.Querys;
import quevedo.servidorLiga.utils.CreateHash;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
public class DAOUsuarios {
    private final DBConnectionPool dbConnectionPool;
    private final CreateHash createHash;


    @Inject
    public DAOUsuarios(DBConnectionPool dbConnectionPool, CreateHash createHash) {
        this.dbConnectionPool = dbConnectionPool;

        this.createHash = createHash;
    }

    public Either<ApiError, List<Usuario>> getAll() {
        Either<ApiError, List<Usuario>> resultado;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.query(Querys.SELECT_FROM_USUARIOS, BeanPropertyRowMapper.newInstance(Usuario.class)));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }
        return resultado;
    }

    public Either<ApiError, Usuario> doLogin(String username, String passCliente) {
        Either<ApiError, Usuario> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            //compruebo primmero si existe el username ya que sino me daria una excepcion
            //si al contar los usuiarios que coinciden me devuelve 1 busco la contraseña de ese usuario
            //razon por la que hago varias querys, si queryforobject no recibe nada da una excepcion, por tanto tengo que asegurarme de que existe en la base de datos antes.
            int encontrado = jdbcTemplate.queryForObject(Querys.SELECT_EXISTE_USERNAME, Integer.class, username);
            if (encontrado > 0) {
                String pass = jdbcTemplate.queryForObject(Querys.SELECT_PASS, String.class, username);
                if (createHash.verificarPass(passCliente, pass)) {
                    Usuario usuario = jdbcTemplate.queryForObject(Querys.SELECT_LOGIN, BeanPropertyRowMapper.newInstance(Usuario.class), username, pass);
                    if (usuario.isConfirmacion()) {
                        resultado = Either.right(usuario);
                    } else {
                        resultado = Either.left(new ApiError(ConstantesDao.AVISO_CORREO_SIN_CONFIRMAR));
                    }

                } else {
                    resultado = Either.left(new ApiError(ConstantesDao.MENSAJE_ERROR_DATOS));
                }
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.MENSAJE_ERROR_DATOS));
            }


        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;
    }

    public Either<ApiError, Integer> reenviarCorreo(String user, String codigo, LocalDateTime fecha) {
        Either<ApiError, Integer> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.update(Querys.UPDATE_CODIGO, codigo, fecha, user, 0));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;
    }

    //REGISTRO

    public Either<ApiError, Usuario> saveUsuario(Usuario usuario) {
        Either<ApiError, Usuario> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());


            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con
                        .prepareStatement(Querys.INSERT_USUARIO_REGISTRO,
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, usuario.getUserName());
                preparedStatement.setString(2, usuario.getCorreo());
                preparedStatement.setString(3, usuario.getPass());
                preparedStatement.setString(4, usuario.getCodActivacion());
                preparedStatement.setBoolean(5, usuario.isConfirmacion());
                preparedStatement.setString(6, usuario.getIdTipoUsuario());
                preparedStatement.setObject(7, usuario.getFechaLimite());
                return preparedStatement;
            }, keyHolder);

            usuario.setIdUsuario(keyHolder.getKey().toString());

            resultado = Either.right(usuario);
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;


    }

    public Either<String, Integer> activacion(String codigo) {
        Either<String, Integer> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.update(Querys.UPDATE_CONFIRMACION
                    , 1, codigo));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(ConstantesDao.ERROR_CONEXION);
        }

        return resultado;

    }

    public Either<String, String> getName(String codigo) {
        Either<String, String> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.queryForObject(Querys.SELECT_NAME_POR_CODIGO, String.class, codigo));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(ConstantesDao.ERROR_CONEXION);
        }

        return resultado;
    }

    public Either<String, Integer> checkUserNameAndEmail(String userName, String correo) {
        Either<String, Integer> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());

            resultado = Either.right(jdbcTemplate.queryForObject(Querys.CHECK_USERNAME_CORREO, Integer.class, userName, correo));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(ConstantesDao.ERROR_CONEXION);
        }

        return resultado;

    }

    public boolean checkTime(LocalDateTime fecha, String codigo) {
        boolean confirmacion = false;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            LocalDateTime fechaLimite = jdbcTemplate.queryForObject(Querys.SELECT_FECHA_LIMITE, LocalDateTime.class, codigo);
            confirmacion = fecha.isBefore(fechaLimite);
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
        }


        return confirmacion;
    }

    public Either<String, Integer> changeDate(LocalDateTime fecha, String codigo) {
        Either<String, Integer> resultado;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.update(Querys.CAMBIAR_FECHA, fecha, codigo));
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(ConstantesDao.ERROR_CONEXION);
        }

        return resultado;

    }

    public String getCorreo(String codigo) {
        String resultado = "";
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = jdbcTemplate.queryForObject(Querys.SELECT_CORREO, String.class, codigo);
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
        }


        return resultado;
    }


    public Either<ApiError, Usuario> updateUsuario(UsuarioUpdateDTO usuarioUpdateDTO) {

        Either<ApiError, Usuario> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            int actualizado = jdbcTemplate.update(Querys.UPDATE_USUARIOS, usuarioUpdateDTO.getUserName(), usuarioUpdateDTO.getCorreo(), usuarioUpdateDTO.getIdUsuario());

            if (actualizado > 0) {
                Usuario usuario = jdbcTemplate.queryForObject(Querys.SELECT_USUARIO_POR_ID, BeanPropertyRowMapper.newInstance(Usuario.class), usuarioUpdateDTO.getIdUsuario());
                resultado = Either.right(usuario);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.USUARIO_NO_ENCONTRADO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;

    }

    public Either<ApiError, String> deleteUsuario(String id) {

        Either<ApiError, String> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            int actualizado = jdbcTemplate.update(Querys.DELETE_USUARIO, id);

            if (actualizado > 0) {
                resultado = Either.right(id);
            } else {
                resultado = Either.left(new ApiError(ConstantesDao.USUARIO_NO_ELIMINADO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }


        return resultado;
    }

    //CAMBIAR CONTRASEÑA
    public Either<ApiError, Usuario> insertCodCambio(Usuario usuario) {
        Either<ApiError, Usuario> resultado;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());

            int actualizado = jdbcTemplate.update(Querys.UPDATE_CODCAMBIO_USUARIOS, usuario.getCodCambio(), usuario.getFechaLimite(), usuario.getIdUsuario());
            if (actualizado > 0) {
                resultado = Either.right(usuario);

            } else {
                resultado = Either.left(new ApiError(ConstantesDao.USUARIO_NO_ENCONTRADO));
            }
        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;
    }

    public Either<ApiError, Integer> changePass(String pass, String codigo) {
        Either<ApiError, Integer> resultado;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());
            resultado = Either.right(jdbcTemplate.update(Querys.UPDATE_PASS, pass, codigo));

        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDao.ERROR_CONEXION));
        }

        return resultado;
    }

    public boolean checkTimeCambioPass(LocalDateTime fecha, String codigo) {
        boolean confirmacion = false;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());

            LocalDateTime fechaLimite = jdbcTemplate.queryForObject(Querys.SELECT_FECHA_CODCAMBIO, LocalDateTime.class, codigo);

            confirmacion = fecha.isBefore(fechaLimite);

        } catch (CannotGetJdbcConnectionException e) {
            log.error(e.getMessage(), e);
        }

        return confirmacion;
    }


}
