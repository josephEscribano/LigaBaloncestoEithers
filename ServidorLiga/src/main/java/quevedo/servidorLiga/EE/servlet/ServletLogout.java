package quevedo.servidorLiga.EE.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import quevedo.servidorLiga.EE.utils.ConstantesRest;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = ConstantesRest.SERVLET_NAME_LOGOUT, value = ConstantesRest.SERVLET_VALUE_LOGOUT)
public class ServletLogout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute(ConstantesRest.QUERY_PARAM_USER, null);
        PrintWriter out = response.getWriter();
        response.setContentType(MediaType.TEXT_PLAIN);
        response.setCharacterEncoding(ConstantesRest.UTF_8);
        out.print(ConstantesRest.DESCONECTADO);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }
}
