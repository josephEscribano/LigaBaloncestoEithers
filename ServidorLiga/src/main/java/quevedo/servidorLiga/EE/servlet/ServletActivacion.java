package quevedo.servidorLiga.EE.servlet;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.service.UsuarioService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@WebServlet(name = ConstantesRest.SERVLET_NAME_ACTIVACION, value = ConstantesRest.SERVLET_VALUE_ACTIVACION)
@Log4j2
public class ServletActivacion extends HttpServlet {
    private final UsuarioService usuarioService;


    @Inject
    public ServletActivacion(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codigo = request.getParameter(ConstantesRest.ACTIVACION_CODIGO);
        if (usuarioService.checkTime(LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)), codigo)) {
            Either<String, Integer> activacion = usuarioService.activacion(codigo);
            if (activacion.isRight()) {
                if (activacion.get() > 0) {
                    Either<String, String> resultadoName = usuarioService.getName(codigo);
                    if (resultadoName.isRight()) {
                        request.setAttribute(ConstantesRest.ACTIVACION_NAME, resultadoName.get());
                        request.getRequestDispatcher(ConstantesRest.BIENVENIDA_JSP).forward(request, response);
                    } else {
                        request.getRequestDispatcher(ConstantesRest.ERROR_CONEXION_HTML).forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher(ConstantesRest.ERROR_ACTIVACION_HTML).forward(request, response);
                }


            } else {
                request.getRequestDispatcher(ConstantesRest.ERROR_CONEXION_HTML).forward(request, response);
            }
        } else {
            request.setAttribute(ConstantesRest.ACTIVACION_CODIGO, codigo);
            request.getRequestDispatcher(ConstantesRest.TIEMPO_EXCEDIDO_JSP).forward(request, response);
        }


    }

}
