package quevedo.ClienteLiga.dao;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.retrofit.RetrofitUsuarios;
import quevedo.common.modelos.ApiRespuesta;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.common.modelos.UsuarioUpdateDTO;

import javax.inject.Inject;
import java.util.List;

public class DAOUsuarios extends DAOGenerics {

    private final RetrofitUsuarios retrofitUsuarios;

    @Inject
    public DAOUsuarios(RetrofitUsuarios retrofitUsuarios) {
        this.retrofitUsuarios = retrofitUsuarios;
    }

    public Either<String, UsuarioDTO> doLogin(String user, String pass) {
        return safeApiCall(retrofitUsuarios.doLogin(user, pass));
    }

    public Either<String, String> doLogout() {
        return safeApiCall(retrofitUsuarios.doLogout());
    }

    public Either<String, List<UsuarioDTO>> getAll() {
        return safeApiCall(retrofitUsuarios.getUsuarios());
    }

    public Either<String, UsuarioDTO> saveUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        return safeApiCall(retrofitUsuarios.saveUsuario(usuarioRegistroDTO));
    }

    public Either<String, UsuarioDTO> saveAdmin(UsuarioRegistroDTO usuarioRegistroDTO) {
        return safeApiCall(retrofitUsuarios.saveAdmin(usuarioRegistroDTO));
    }

    public Either<String, ApiRespuesta> reenviarCorreo(String username) {
        return safeApiCall(retrofitUsuarios.reenviarCorreo(username));
    }

    public Either<String, UsuarioDTO> cambiarPass(UsuarioDTO usuarioDTO) {
        return safeApiCall(retrofitUsuarios.cambiarPass(usuarioDTO));
    }

    public Either<String, UsuarioDTO> updateUsuario(UsuarioUpdateDTO usuarioUpdateDTO) {
        return safeApiCall(retrofitUsuarios.updateUsuario(usuarioUpdateDTO));
    }

    public Either<String, String> deleteUsuario(String id) {
        return safeApiCall(retrofitUsuarios.deleteUsuario(id));
    }


}
