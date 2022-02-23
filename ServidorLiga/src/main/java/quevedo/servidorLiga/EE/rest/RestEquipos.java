package quevedo.servidorLiga.EE.rest;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.log4j.Log4j2;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Equipo;
import quevedo.servidorLiga.EE.filtros.anotaciones.Admin;
import quevedo.servidorLiga.EE.filtros.anotaciones.Login;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.service.EquipoService;

import java.util.List;

@Path(ConstantesRest.PATH_EQUIPOS)
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
@Login

public class RestEquipos {

    private final EquipoService equipoService;

    @Inject
    public RestEquipos(EquipoService equipoService) {
        this.equipoService = equipoService;
    }


    @GET
    public Response getAll() {
        Response response;
        Either<ApiError, List<Equipo>> resultado = equipoService.getAll();

        if (resultado.isRight()) {
            response = Response.status(Response.Status.OK)
                    .entity(resultado.get())
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    @POST
    @Path(ConstantesRest.PATH_NAME)
    @Admin
    public Response saveEquipo(@PathParam(ConstantesRest.PATH_PARAMETER_NAME) String nombre) {
        Response response;
        Either<ApiError, Equipo> resultado = equipoService.saveEquipo(nombre);
        if (resultado.isRight()) {
            response = Response.status(Response.Status.OK)
                    .entity(resultado.get())
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_MODIFIED)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    @PUT
    @Admin
    public Response updateEquipo(Equipo equipo) {
        Response response;
        Either<ApiError, Equipo> resultado = equipoService.updateEquipo(equipo);
        if (resultado.isRight()) {
            response = Response.status(Response.Status.CREATED)
                    .entity(resultado.get())
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_MODIFIED)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    @DELETE
    @Admin
    @Path(ConstantesRest.PATH_ID)
    public Response deleteEquipo(@PathParam(ConstantesRest.PARAM_ID) String id) {
        Response response;
        Either<ApiError, String> resultado = equipoService.deleteEquipo(id);
        if (resultado.isRight()) {
            response = Response.status(Response.Status.OK)
                    .entity(resultado.get())
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_MODIFIED)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }
}
