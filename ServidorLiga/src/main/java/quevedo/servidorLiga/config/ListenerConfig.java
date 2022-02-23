package quevedo.servidorLiga.config;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener()
public class ListenerConfig implements ServletContextListener {

    private final Configuration config;

    @Inject
    public ListenerConfig(Configuration configuration) {
        config = configuration;
    }


    public void contextInitialized(ServletContextEvent sce) {
        config.cargarConfig(sce.getServletContext().getResourceAsStream(ConstantesConfig.RUTA_CONFIGURACION));
    }
}
