package quevedo.servidorLiga.EE.filtros;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import quevedo.common.errores.ApiError;
import quevedo.servidorLiga.EE.filtros.anotaciones.Admin;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.dao.modelos.Usuario;

import java.io.IOException;

@Provider
@Admin
public class FiltroAdmin implements ContainerRequestFilter {

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Usuario usuario = (Usuario) httpServletRequest.getSession().getAttribute(ConstantesRest.QUERY_PARAM_USER);
        if (usuario == null || !usuario.getIdTipoUsuario().equals(ConstantesRest.DOS)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiError(ConstantesRest.MENSAJE_SOLOADMIN))
                    .type(MediaType.APPLICATION_JSON_TYPE).build());
        }

    }
}
