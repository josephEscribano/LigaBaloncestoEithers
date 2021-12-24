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

@WebServlet(name = ConstantesRest.SERVLET_NAME_ACTIVACION, value = ConstantesRest.SERVLET_VALUE_ACTIVACION)
@Log4j2
public class ServletActivacion extends HttpServlet {
    private final UsuarioService usuarioService;
    private final MandarMail mandarMail;

    @Inject
    public ServletActivacion(UsuarioService usuarioService, MandarMail mandarMail) {
        this.usuarioService = usuarioService;
        this.mandarMail = mandarMail;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codigo = request.getParameter(ConstantesRest.ACTIVACION_CODIGO);
        if (usuarioService.checkTime(LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)),codigo)){
            if (usuarioService.activacion(codigo) > 0){
                request.setAttribute(ConstantesRest.ACTIVACION_NAME,usuarioService.getName(codigo));
                request.getRequestDispatcher(ConstantesRest.BIENVENIDA_JSP).forward(request,response);
            }else{
                request.getRequestDispatcher(ConstantesRest.ERROR_ACTIVACION_HTML).forward(request,response);
            }
        }else{

            request.setAttribute(ConstantesRest.ACTIVACION_CODIGO,codigo);
            request.getRequestDispatcher(ConstantesRest.TIEMPO_EXCEDIDO_JSP).forward(request,response);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
