package quevedo.servidorLiga.service;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.servidorLiga.dao.DAOUsuarios;
import quevedo.servidorLiga.dao.modelos.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioService {

    private final DAOUsuarios daoUsuarios;

    @Inject
    public UsuarioService(DAOUsuarios daoUsuarios) {
        this.daoUsuarios = daoUsuarios;
    }

    public Either<ApiError, List<Usuario>> getAll() {
        return daoUsuarios.getAll();
    }

    public Either<ApiError, Usuario> saveUsuario(Usuario usuario) {
        return daoUsuarios.saveUsuario(usuario);
    }

    public Either<String, Integer> activacion(String codigo) {
        return daoUsuarios.activacion(codigo);
    }

    public Either<String, String> getName(String codigo) {
        return daoUsuarios.getName(codigo);
    }

    public Either<String, Integer> checkUserNameAndEmail(String userName, String correo) {
        return daoUsuarios.checkUserNameAndEmail(userName, correo);
    }

    public boolean checkTime(LocalDateTime fecha, String codigo) {
        return daoUsuarios.checkTime(fecha, codigo);
    }

    public Either<String, Integer> changeDate(LocalDateTime fecha, String codigo) {
        return daoUsuarios.changeDate(fecha, codigo);
    }

    public Either<ApiError, Usuario> updateUsuario(UsuarioDTO usuarioDTO) {
        return daoUsuarios.updateUsuario(usuarioDTO);
    }

    public Either<String, String> deleteUsuario(String id) {
        return daoUsuarios.deleteUsuario(id);
    }

    public Either<ApiError, Usuario> insertCodCambio(Usuario usuario) {
        return daoUsuarios.insertCodCambio(usuario);
    }

    public Either<String, Integer> changePass(String pass, String codigo) {
        return daoUsuarios.changePass(pass, codigo);
    }

    public boolean checkTimeCambioPass(LocalDateTime fecha, String codigo) {
        return daoUsuarios.checkTimeCambioPass(fecha, codigo);
    }

    public String getCorreo(String codigo) {
        return daoUsuarios.getCorreo(codigo);
    }

    public Either<ApiError, Usuario> doLogin(String user, String pass) {
        return daoUsuarios.doLogin(user, pass);
    }


}
