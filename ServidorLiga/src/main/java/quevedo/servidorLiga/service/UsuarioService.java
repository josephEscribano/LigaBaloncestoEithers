package quevedo.servidorLiga.service;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Usuario;
import quevedo.servidorLiga.dao.DAOUsuarios;

import java.util.List;

public class UsuarioService {

    private final DAOUsuarios daoUsuarios;

    @Inject
    public UsuarioService(DAOUsuarios daoUsuarios) {
        this.daoUsuarios = daoUsuarios;
    }

    public Either<ApiError, List<Usuario>> getAll(){
        return daoUsuarios.getAll();
    }
}
