package quevedo.servidorLiga.EE.rest;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.log4j.Log4j2;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.ApiRespuesta;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.common.modelos.UsuarioUpdateDTO;
import quevedo.servidorLiga.EE.filtros.anotaciones.Admin;
import quevedo.servidorLiga.EE.filtros.anotaciones.Login;
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
    @Login
    @Admin
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
    @Path(ConstantesRest.PATH_INSERT_ADMIN)
    @Login
    @Admin
    public Response insertAdministrador(UsuarioRegistroDTO usuarioRegistroDTO) {
        Response response;
        Either<String, Integer> checkUserNameAndEmail = usuarioService.checkUserNameAndEmail(usuarioRegistroDTO.getUserName(), usuarioRegistroDTO.getCorreo());
        if (checkUserNameAndEmail.isRight()) {
            if (checkUserNameAndEmail.get() == 0) {
                String passHasheada = createHash.hashearPass(usuarioRegistroDTO.getPass());
                String codActivacion = Utils.randomCode();
                Usuario usuario = new Usuario(usuarioRegistroDTO.getUserName(), usuarioRegistroDTO.getCorreo(), passHasheada
                        , codActivacion, false, LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1), usuarioRegistroDTO.getIdTipoUsuario());
                Either<ApiError, Usuario> resultado = usuarioService.saveAdmin(usuario);
                response = getResponseMail(usuarioRegistroDTO, codActivacion, resultado);
            } else {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiError(ConstantesRest.DATOS_REPETIDOS))
                        .build();
            }

        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(checkUserNameAndEmail.getLeft())
                    .build();
        }


        return response;
    }

    @POST
    public Response insertUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        Response response;
        Either<String, Integer> checkUserNameAndEmail = usuarioService.checkUserNameAndEmail(usuarioRegistroDTO.getUserName(), usuarioRegistroDTO.getCorreo());
        if (checkUserNameAndEmail.isRight()) {
            if (checkUserNameAndEmail.get() == 0) {
                String passHasheada = createHash.hashearPass(usuarioRegistroDTO.getPass());
                String codActivacion = Utils.randomCode();
                Usuario usuario = new Usuario(usuarioRegistroDTO.getUserName(), usuarioRegistroDTO.getCorreo(), passHasheada
                        , codActivacion, false, LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1), usuarioRegistroDTO.getIdTipoUsuario());
                Either<ApiError, Usuario> resultado = usuarioService.saveUsuario(usuario);

                response = getResponseMail(usuarioRegistroDTO, codActivacion, resultado);
            } else {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiError(ConstantesRest.DATOS_REPETIDOS))
                        .build();
            }

        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(checkUserNameAndEmail.getLeft())
                    .build();
        }


        return response;
    }

    @PUT
    @Path(ConstantesRest.PATH_CAMBIO_CODIGO)
    public Response reenviarCorreo(@QueryParam(ConstantesRest.QUERY_PARAM_USER) String user) {
        Response response;
        String codigo = Utils.randomCode();
        Either<ApiError, Integer> resultado = usuarioService.reenviarCorreo(user, codigo, LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1));
        if (resultado.isRight()) {
            if (resultado.get() != 0) {
                try {
                    mandarMail.generateAndSendEmail(usuarioService.getCorreo(codigo), "<html>Haz click en el siguiente enlace para confirmar el correo: <a href=\"http://localhost:8080/ServidorLiga-1.0-SNAPSHOT/activacion?codigo=" + codigo + "\" >Activacion</a></html>"
                            , ConstantesRest.ASUNTO_REGISTRO);
                    response = Response.status(Response.Status.OK)
                            .entity(new ApiRespuesta(ConstantesRest.EL_CORREO_HA_SIDO_ENVIADO))
                            .build();
                } catch (MessagingException e) {
                    log.error(e.getMessage(), e);
                    response = Response.status(Response.Status.NOT_FOUND)
                            .entity(new ApiError(ConstantesRest.CORREO_NO_ENVIADO))
                            .build();
                }
            } else {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiError(ConstantesRest.USUARIO_NO_ENCONTRADO))
                        .build();
            }


        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(resultado.getLeft())
                    .build();
        }
        return response;
    }

    @PUT
    @Login
    public Response updateUsuario(UsuarioUpdateDTO usuarioUpdateDTO) {
        Response response;
        Either<ApiError, Usuario> resultado = usuarioService.updateUsuario(usuarioUpdateDTO);
        if (resultado.isRight()) {
            response = Response.status(Response.Status.CREATED)
                    .entity(usuarioMapper.usuarioDTOMapper(resultado.get()))
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_MODIFIED)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    @DELETE
    @Path(ConstantesRest.PATH_ID)
    @Login
    @Admin
    public Response deleteUsuario(@PathParam(ConstantesRest.PARAM_ID) String id) {
        Response response;
        Either<ApiError, String> resultado = usuarioService.deleteUsuario(id);
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
    @Path(ConstantesRest.PATH_CAMBIO_PASS)
    @Login
    public Response actualizarPass(UsuarioDTO usuarioDTO) {
        Response response;
        String codCambio = Utils.randomCode();

        Either<ApiError, Usuario> resultado = usuarioService.insertCodCambio(new Usuario(usuarioDTO.getIdUsuario(), usuarioDTO.getUserName(), usuarioDTO.getCorreo()
                , LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1), usuarioDTO.getIdTipoUsuario(), codCambio));

        if (resultado.isRight()) {
            try {
                mandarMail.generateAndSendEmail(usuarioDTO.getCorreo(), "<html>Haz click en el siguiente enlace para poder cambiar la contraseña: <a href=\"http://localhost:8080/ServidorLiga-1.0-SNAPSHOT/preCambio?codigo=" + codCambio + "\" >Cambio Contraseña</a></html>"
                        , ConstantesRest.ASUNTO_CAMBIO_DE_CONTRASEÑA);

                response = Response.status(Response.Status.OK)
                        .entity(usuarioMapper.usuarioDTOMapper(resultado.get()))
                        .build();
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiError(ConstantesRest.CORREO_NO_ENVIADO))
                        .build();
            }

        } else {
            response = Response.status(Response.Status.NOT_MODIFIED)
                    .entity(resultado.getLeft())
                    .build();
        }

        return response;
    }

    //CODIGO DUPLICADO
    private Response getResponseMail(UsuarioRegistroDTO usuarioRegistroDTO, String codActivacion, Either<ApiError, Usuario> resultado) {
        Response response;
        if (resultado.isRight()) {
            try {
                mandarMail.generateAndSendEmail(usuarioRegistroDTO.getCorreo(), "<html>Haz click en el siguiente enlace para confirmar el correo: <a href=\"http://localhost:8080/ServidorLiga-1.0-SNAPSHOT/activacion?codigo=" + codActivacion + "\" >Activacion</a></html>"
                        , ConstantesRest.ASUNTO_REGISTRO);
                response = Response.status(Response.Status.OK)
                        .entity(usuarioMapper.usuarioDTOMapper(resultado.get()))
                        .build();
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiError(ConstantesRest.CORREO_NO_ENVIADO))
                        .build();
            }

        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(resultado.getLeft())
                    .build();
        }
        return response;
    }


}
