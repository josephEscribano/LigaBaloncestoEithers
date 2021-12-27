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

import java.io.IOException;
import java.time.LocalDate;

@Provider
@Admin
public class FiltroTipoUsuario implements ContainerRequestFilter {

    @Context
    private HttpServletRequest httpServletRequest;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!httpServletRequest.getSession().getAttribute(ConstantesRest.PARAMETER_TIPO).equals(ConstantesRest.UNO)){
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiError(ConstantesRest.MENSAJE_SOLOADMIN, LocalDate.now()))
                    .type(MediaType.APPLICATION_JSON_TYPE).build());
        }

    }
}
