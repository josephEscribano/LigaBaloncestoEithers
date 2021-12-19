package quevedo.servidorLiga.config;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import quevedo.servidorLiga.dao.DBConnectionPool;

@WebListener()
public class ListenerConfig implements ServletContextListener {

    private final Configuration configuration;
    private final DBConnectionPool dbConnectionPool;

    @Inject
    public ListenerConfig(Configuration configuration, DBConnectionPool dbConnectionPool) {
        this.configuration = configuration;
        this.dbConnectionPool = dbConnectionPool;
    }

    public void contextInitialized(ServletContextEvent sce) {

        configuration.cargarConfig(sce.getServletContext().getResourceAsStream(ConstantesConfig.RUTA_CONFIGURACION));
    }

    public void contextDestroyed(ServletContextEvent sce) {
        dbConnectionPool.closePool();
    }

}
