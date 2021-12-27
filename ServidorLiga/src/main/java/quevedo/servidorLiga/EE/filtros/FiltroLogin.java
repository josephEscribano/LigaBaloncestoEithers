package quevedo.servidorLiga.EE.filtros;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import quevedo.common.errores.ApiError;
import quevedo.servidorLiga.EE.filtros.anotaciones.Login;
import quevedo.servidorLiga.EE.utils.ConstantesRest;

import java.io.IOException;
import java.time.LocalDate;

@Provider
@Login
public class FiltroLogin implements ContainerRequestFilter {
    @Context
    private HttpServletRequest httpServletRequest;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (httpServletRequest.getSession().getAttribute(ConstantesRest.PARAMETER_USERNAME) == null){
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity(new ApiError(ConstantesRest.NO_LOGUEADO, LocalDate.now()))
                    .type(MediaType.APPLICATION_JSON_TYPE).build());
        }

    }
}
