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

    public Either<ApiError,Usuario> saveUsuario(Usuario usuario){
        return daoUsuarios.saveUsuario(usuario);
    }

    public int activacion(String codigo){
        return daoUsuarios.activacion(codigo);
    }

    public String getName(String codigo){
        return daoUsuarios.getName(codigo);
    }

    public int checkUserNameAndEmail(String userName,String correo){
        return daoUsuarios.checkUserNameAndEmail(userName,correo);
    }

    public boolean checkTime(LocalDateTime fecha,String codigo){
        return daoUsuarios.checkTime(fecha, codigo);
    }

    public int changeDate(LocalDateTime fecha,String codigo){
        return daoUsuarios.changeDate(fecha, codigo);
    }

    public Either<ApiError,Usuario> updateUsuario(UsuarioDTO usuarioDTO){
        return daoUsuarios.updateUsuario(usuarioDTO);
    }

    public Either<String,String> deleteUsuario(String id){
        return daoUsuarios.deleteUsuario(id);
    }
}
