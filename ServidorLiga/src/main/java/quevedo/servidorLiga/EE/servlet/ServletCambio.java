package quevedo.servidorLiga.EE.servlet;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import quevedo.common.errores.ApiError;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.service.UsuarioService;
import quevedo.servidorLiga.utils.CreateHash;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@WebServlet(name = ConstantesRest.SERVLET_NAME_CAMBIO, value = ConstantesRest.SERVLET_VALUE_CAMBIO)
public class ServletCambio extends HttpServlet {
    private final UsuarioService usuarioService;
    private final CreateHash createHash;

    @Inject
    public ServletCambio(UsuarioService usuarioService, CreateHash createHash) {
        this.usuarioService = usuarioService;
        this.createHash = createHash;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pass = createHash.hashearPass(request.getParameter(ConstantesRest.PARAMETER_PASS));
        String codigo = request.getParameter(ConstantesRest.ACTIVACION_CODIGO);
        if (usuarioService.checkTimeCambioPass(LocalDateTime.now(ZoneId.of(ConstantesRest.ZONA_HORARIA)), codigo)) {
            Either<ApiError, Integer> resultado = usuarioService.changePass(pass, codigo);
            if (resultado.isRight()) {
                if (resultado.get() > 0) {
                    request.getRequestDispatcher(ConstantesRest.CONTRASEÑA_CAMBIADA_HTML).forward(request, response);
                } else {
                    request.getRequestDispatcher(ConstantesRest.ERROR_CONTRASEÑA_HTML).forward(request, response);
                }
            } else {
                request.getRequestDispatcher(ConstantesRest.ERROR_CONEXION_HTML).forward(request, response);
            }
        } else {
            request.getRequestDispatcher(ConstantesRest.TIEMPO_EXCEDIDO_CONTRASEÑA_HTML).forward(request, response);
        }

    }


}
