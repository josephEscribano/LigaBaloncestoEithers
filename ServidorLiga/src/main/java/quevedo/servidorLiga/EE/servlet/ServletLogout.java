package quevedo.servidorLiga.EE.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import quevedo.servidorLiga.EE.utils.ConstantesRest;

import java.io.IOException;

@WebServlet(name = ConstantesRest.SERVLET_NAME_LOGOUT, value = ConstantesRest.SERVLET_VALUE_LOGOUT)
public class ServletLogout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute(ConstantesRest.PARAMETER_USERNAME,null);
        request.getSession().setAttribute(ConstantesRest.PARAMETER_TIPO,null);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
