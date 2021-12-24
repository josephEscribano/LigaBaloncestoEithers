package quevedo.servidorLiga.EE.servlet;

import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.extern.log4j.Log4j2;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.service.MandarMail;
import quevedo.servidorLiga.service.UsuarioService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@WebServlet(name = "ServletMail", value = "/mail")
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
        String codigo = request.getParameter(ConstantesRest.ACTIVACION_CODIGO);
        try {
            if (usuarioService.changeDate(LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)).plusMinutes(1),codigo) > 0){
                mandarMail.generateAndSendEmail(ConstantesRest.CORREO_RECIBIR,"<html>Haz click en el siguiente enlace para confirmar el correo: <a href=\"http://localhost:8080/ServidorLiga-1.0-SNAPSHOT/activacion?codigo="+codigo+"\" >Activacion</a></html>"
                        , ConstantesRest.ASUNTO);
                request.getRequestDispatcher(ConstantesRest.EMAIL_ENVIADO_HTML).forward(request,response);
            }else{
                request.getRequestDispatcher(ConstantesRest.ERROR_ACTIVACION_HTML).forward(request,response);
            }

        }catch (MessagingException e) {
            log.error(e.getMessage(),e);
            request.getRequestDispatcher(ConstantesRest.ERROR_ACTIVACION_HTML).forward(request,response);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
