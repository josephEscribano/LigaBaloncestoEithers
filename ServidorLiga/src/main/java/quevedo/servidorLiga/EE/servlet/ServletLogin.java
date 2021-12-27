package quevedo.servidorLiga.EE.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import quevedo.servidorLiga.EE.utils.ConstantesRest;

import java.io.IOException;

@WebServlet(name = ConstantesRest.SERVLET_NAME_LOGIN, value = ConstantesRest.SERVLET_VALUE_LOGIN)
public class ServletLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter(ConstantesRest.PARAMETER_USERNAME);
        String tipoUsuario = request.getParameter(ConstantesRest.PARAMETER_TIPO);

        request.getSession().setAttribute(ConstantesRest.PARAMETER_USERNAME,userName);
        request.getSession().setAttribute(ConstantesRest.PARAMETER_TIPO,tipoUsuario);

    }

}
