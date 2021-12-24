package quevedo.servidorLiga.EE;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.log4j.Log4j2;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.dao.mappers.UsuarioMapper;
import quevedo.servidorLiga.dao.modelos.Usuario;
import quevedo.servidorLiga.service.MandarMail;
import quevedo.servidorLiga.service.UsuarioService;
import quevedo.servidorLiga.utils.CreateHash;
import quevedo.servidorLiga.utils.Utils;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Path(ConstantesRest.PATH_USUARIOS)
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class RestUsuarios {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final CreateHash createHash;
    private final MandarMail mandarMail;

    @Inject
    public RestUsuarios(UsuarioService usuarioService, UsuarioMapper usuarioMapper, CreateHash createHash, MandarMail mandarMail) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.createHash = createHash;
        this.mandarMail = mandarMail;
    }

    @GET
    public Response getAll() {
        Response response;
        Either<ApiError, List<Usuario>> resultado = usuarioService.getAll();

        if (resultado.isRight()) {
            response = Response.status(Response.Status.OK)
                    .entity(resultado.get().stream().map(usuarioMapper::usuarioDTOMapper).collect(Collectors.toList()))
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    @POST
    public Response insertUsuario(UsuarioRegistroDTO usuarioRegistroDTO)  {
        Response response;
        if (usuarioService.checkUserNameAndEmail(usuarioRegistroDTO.getUserName(),usuarioRegistroDTO.getCorreo()) == 0){

            String passHasheada = createHash.hashearPass(usuarioRegistroDTO.getPass());
            String codActivacion = Utils.randomCode();
            Usuario usuario = new Usuario(usuarioRegistroDTO.getUserName(),usuarioRegistroDTO.getCorreo(),passHasheada
                    ,codActivacion,false, LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1),usuarioRegistroDTO.getIdTipoUsuario());
            Either<ApiError,Usuario> resultado = usuarioService.saveUsuario(usuario);

            if (resultado.isRight()){
                try {

                    mandarMail.generateAndSendEmail(ConstantesRest.CORREO_RECIBIR,"<html>Haz click en el siguiente enlace para confirmar el correo: <a href=\"http://localhost:8080/ServidorLiga-1.0-SNAPSHOT/activacion?codigo="+codActivacion+"\" >Activacion</a></html>"
                            , ConstantesRest.ASUNTO);
                } catch (MessagingException e) {
                    log.error(e.getMessage(),e);
                }
                response = Response.status(Response.Status.OK)
                        .entity(usuarioMapper.usuarioDTOMapper(resultado.get()))
                        .build();
            }else{
                response = Response.status(Response.Status.OK)
                        .entity(resultado.getLeft())
                        .build();
            }
        }else{
            response = Response.status(Response.Status.CONFLICT)
                    .entity(ConstantesRest.DATOS_REPETIDOS)
                    .build();
        }




        return response;
    }

    @PUT
    public Response updateUsuario(UsuarioDTO usuarioDTO){
        Response response;
        Either<ApiError,Usuario> resultado = usuarioService.updateUsuario(usuarioDTO);
        if (resultado.isRight()){
            response = Response.status(Response.Status.CREATED)
                    .entity(usuarioMapper.usuarioDTOMapper(resultado.get()))
                    .build();
        }else{
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    @DELETE
    @Path(ConstantesRest.PATH_ID)
    public Response deleteUsuario(@PathParam(ConstantesRest.PARAM_ID) String id){
        Response response;
        Either<String,String> resultado = usuarioService.deleteUsuario(id);

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
