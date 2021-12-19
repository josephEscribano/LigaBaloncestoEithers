package quevedo.servidorLiga.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
@Log4j2
@ApplicationScoped
public class Configuration {

    private String ruta;
    private String password;
    private String user;
    private String driver;

    void cargarConfig(InputStream file){

        try {
            Yaml yaml = new Yaml();
            Iterable<Object> it;

            it = yaml.loadAll(file);

            Map<String,String> m = (Map) it.iterator().next();

            this.ruta = m.get(ConstantesConfig.RUTA);
            this.password = m.get(ConstantesConfig.PASSWORD);
            this.user = m.get(ConstantesConfig.USER);
            this.driver = m.get(ConstantesConfig.DRIVER);

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
