package quevedo.servidorLiga.EE.servlet;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.service.MandarMail;
import quevedo.servidorLiga.service.UsuarioService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@WebServlet(name = ConstantesRest.SERVLET_NAME_MAIL, value = ConstantesRest.SERVLET_VALUE_MAIL)
@Log4j2
public class ServletMail extends HttpServlet {
    private final UsuarioService usuarioService;
    private final MandarMail mandarMail;

    @Inject
    public ServletMail(UsuarioService usuarioService, MandarMail mandarMail) {
        this.usuarioService = usuarioService;
        this.mandarMail = mandarMail;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //llamo a este servlet cuando el usuario ha excedido el tiempo para confirmar su correo
        //este servlet se llama desde el tiempoExcedido.jsp
        String codigo = request.getParameter(ConstantesRest.ACTIVACION_CODIGO);
        try {
            Either<String, Integer> resultado = usuarioService.changeDate(LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1), codigo);
            if (resultado.isRight()) {
                if (resultado.get() > 0) {
                    mandarMail.generateAndSendEmail(usuarioService.getCorreo(codigo), "<html>Haz click en el siguiente enlace para confirmar el correo: <a href=\"http://localhost:8080/ServidorLiga-1.0-SNAPSHOT/activacion?codigo=" + codigo + "\" >Activacion</a></html>"
                            , ConstantesRest.ASUNTO_REGISTRO);
                    request.getRequestDispatcher(ConstantesRest.EMAIL_ENVIADO_HTML).forward(request, response);
                } else {
                    request.getRequestDispatcher(ConstantesRest.ERROR_ACTIVACION_HTML).forward(request, response);
                }

            } else {
                request.getRequestDispatcher(ConstantesRest.ERROR_CONEXION_HTML).forward(request, response);
            }

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            request.getRequestDispatcher(ConstantesRest.ERROR_ACTIVACION_HTML).forward(request, response);
        }


    }

}
