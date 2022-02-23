package quevedo.servidorLiga.EE.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import quevedo.servidorLiga.EE.utils.ConstantesRest;

import java.io.IOException;

@WebServlet(name = ConstantesRest.SERVLET_NAME_PRE_CAMBIO, value = ConstantesRest.SERRVLET_VALUE_PRE_CAMBIO)
public class ServletPreCambio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ConstantesRest.ACTIVACION_CODIGO, request.getParameter(ConstantesRest.ACTIVACION_CODIGO));
        request.getRequestDispatcher(ConstantesRest.CAMBIO_PASS_JSP).forward(request, response);
    }


}
