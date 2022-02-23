package quevedo.ClienteLiga.service;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.DAOUsuarios;
import quevedo.common.modelos.ApiRespuesta;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.common.modelos.UsuarioUpdateDTO;

import javax.inject.Inject;
import java.util.List;

public class ServiceUsuarios {

    private final DAOUsuarios daoUsuarios;

    @Inject
    public ServiceUsuarios(DAOUsuarios daoUsuarios) {
        this.daoUsuarios = daoUsuarios;
    }

    public Either<String, UsuarioDTO> doLogin(String user, String pass) {
        return daoUsuarios.doLogin(user, pass);
    }

    public Either<String, String> doLogout() {
        return daoUsuarios.doLogout();
    }

    public Either<String, List<UsuarioDTO>> getAll() {
        return daoUsuarios.getAll();
    }

    public Either<String, UsuarioDTO> saveUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        return daoUsuarios.saveUsuario(usuarioRegistroDTO);
    }

    public Either<String, UsuarioDTO> saveAdmin(UsuarioRegistroDTO usuarioRegistroDTO) {
        return daoUsuarios.saveAdmin(usuarioRegistroDTO);
    }

    public Either<String, ApiRespuesta> reenviarCorreo(String username) {
        return daoUsuarios.reenviarCorreo(username);
    }

    public Either<String, UsuarioDTO> cambiarPass(UsuarioDTO usuarioDTO) {
        return daoUsuarios.cambiarPass(usuarioDTO);
    }

    public Either<String, UsuarioDTO> updateUsuario(UsuarioUpdateDTO usuarioUpdateDTO) {
        return daoUsuarios.updateUsuario(usuarioUpdateDTO);
    }

    public Either<String, String> deleteUsuario(String id) {
        return daoUsuarios.deleteUsuario(id);
    }
}
