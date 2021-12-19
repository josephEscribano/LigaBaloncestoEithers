package quevedo.servidorLiga.EE;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Usuario;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.service.UsuarioService;

import java.util.List;

@Path(ConstantesRest.PATH_USUARIOS)
@Produces({MediaType.APPLICATION_JSON})

@Consumes(MediaType.APPLICATION_JSON)
public class RestUsuarios {

    private final UsuarioService usuarioService;

    @Inject
    public RestUsuarios(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GET
    public Response getAll(){
        Response response;
        Either<ApiError, List<Usuario>> resultado = usuarioService.getAll();

        if (resultado.isRight()){
            response = Response.status(Response.Status.OK)
                    .entity(resultado.get())
                    .build();
        }else{
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }
}
