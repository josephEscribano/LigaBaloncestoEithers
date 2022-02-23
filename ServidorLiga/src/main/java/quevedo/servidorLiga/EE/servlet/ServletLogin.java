package quevedo.servidorLiga.EE.servlet;

import com.google.gson.Gson;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import quevedo.common.errores.ApiError;
import quevedo.servidorLiga.EE.utils.ConstantesRest;
import quevedo.servidorLiga.dao.mappers.UsuarioMapper;
import quevedo.servidorLiga.dao.modelos.Usuario;
import quevedo.servidorLiga.service.UsuarioService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = ConstantesRest.SERVLET_NAME_LOGIN, value = ConstantesRest.SERVLET_VALUE_LOGIN)
public class ServletLogin extends HttpServlet {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @Inject
    public ServletLogin(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter(ConstantesRest.PARAMETER_USERNAME);
        String pass = request.getParameter(ConstantesRest.PARAMETER_PASS);
        Either<ApiError, Usuario> resultado = usuarioService.doLogin(userName, pass);
        if (resultado.isRight()) {
            Usuario usuario = resultado.get();
            request.getSession().setAttribute(ConstantesRest.QUERY_PARAM_USER, usuario);
            String usuarioDTO = new Gson().toJson(usuarioMapper.usuarioDTOMapper(resultado.get()));
            PrintWriter out = response.getWriter();
            response.setContentType(MediaType.APPLICATION_JSON);
            response.setCharacterEncoding(ConstantesRest.UTF_8);
            out.print(usuarioDTO);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();

        } else {
            String apiError = new Gson().toJson(resultado.getLeft());
            PrintWriter out = response.getWriter();
            response.setContentType(MediaType.APPLICATION_JSON);
            response.setCharacterEncoding(ConstantesRest.UTF_8);
            out.print(apiError);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.flush();

        }


    }

}
