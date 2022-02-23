package quevedo.ClienteLiga.gui.controllers;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteLiga.gui.controllers.pantallasAcciones.FXMLEquiposController;
import quevedo.ClienteLiga.gui.controllers.pantallasAcciones.FXMLJornadasController;
import quevedo.ClienteLiga.gui.controllers.pantallasAcciones.FXMLPartidosController;
import quevedo.ClienteLiga.gui.controllers.pantallasAcciones.FXMLUsuariosController;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import quevedo.ClienteLiga.service.ServiceUsuarios;
import quevedo.common.modelos.UsuarioDTO;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLPrincipalController implements Initializable {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final FXMLLoader loaderWelcome;
    private final FXMLLoader loaderEquipos;
    private final FXMLLoader loaderJornadas;
    private final FXMLLoader loaderPartidos;
    private final FXMLLoader loaderLogin;
    private final FXMLLoader loaderRegistro;
    private final FXMLLoader loaderUsuarios;
    private final FXMLLoader loaderUsuarioNormal;
    private final ServiceUsuarios serviceUsuarios;
    private UsuarioDTO usuarioDTO;
    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuApp;
    private AnchorPane welcome;
    private FXMLWelcomeController fxmlWelcomeController;
    private AnchorPane equipo;
    private FXMLEquiposController fxmlEquiposController;
    private AnchorPane jornada;
    private FXMLJornadasController fxmlJornadasController;
    private AnchorPane partido;
    private FXMLPartidosController fxmlPartidosController;
    private AnchorPane login;
    private FXMLLoginController fxmlLoginController;
    private AnchorPane registro;
    private FXMLRegistroController fxmlRegistroController;
    private AnchorPane pantallaUsuario;
    private AnchorPane pantallaUsuarioNormal;
    private FXMLUsuariosController fxmlUsuariosController;


    @Inject
    public FXMLPrincipalController(FXMLLoader loaderWelcome, FXMLLoader loaderEquipos, FXMLLoader loaderJornadas, FXMLLoader loaderPartidos, FXMLLoader loaderLogin, FXMLLoader loaderRegistro, FXMLLoader loaderUsuarios, FXMLLoader loaderUsuarioNormal, ServiceUsuarios serviceUsuarios) {
        this.loaderWelcome = loaderWelcome;
        this.loaderEquipos = loaderEquipos;
        this.loaderJornadas = loaderJornadas;
        this.loaderPartidos = loaderPartidos;
        this.loaderLogin = loaderLogin;
        this.loaderRegistro = loaderRegistro;
        this.loaderUsuarios = loaderUsuarios;
        this.loaderUsuarioNormal = loaderUsuarioNormal;
        this.serviceUsuarios = serviceUsuarios;
    }


    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public BorderPane getRoot() {
        return root;
    }


    public void doLogout() {
        Single<Either<String, String>> single = Single.fromCallable(serviceUsuarios::doLogout)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> root.setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(mensaje -> chargeLogin())
                        .peekLeft(error -> {
                            alert.setContentText(error);
                            alert.showAndWait();
                        }),
                throwable -> {
                    alert.setContentText(throwable.getMessage());
                    alert.showAndWait();
                });

        root.setCursor(Cursor.WAIT);

    }

    //PRELOAD

    public void preloadLogin() {
        try {
            if (login == null) {
                login = loaderLogin.load(getClass().getResourceAsStream("/fxml/FXMLLogin.fxml"));
                fxmlLoginController = loaderLogin.getController();
                fxmlLoginController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadRegistro() {
        try {
            if (registro == null) {
                registro = loaderRegistro.load(getClass().getResourceAsStream("/fxml/FXMLRegistro.fxml"));
                fxmlRegistroController = loaderRegistro.getController();
                fxmlRegistroController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadWelcome() {
        try {
            if (welcome == null) {
                welcome = loaderWelcome.load(getClass().getResourceAsStream("/fxml/FXMLWelcome.fxml"));
                fxmlWelcomeController = loaderWelcome.getController();
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadEquipos() {
        try {
            if (equipo == null) {
                equipo = loaderEquipos.load(getClass().getResourceAsStream("/fxml/FXMLEquipos.fxml"));
                fxmlEquiposController = loaderEquipos.getController();
                fxmlEquiposController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadJornadas() {
        try {
            if (jornada == null) {
                jornada = loaderJornadas.load(getClass().getResourceAsStream("/fxml/FXMLJornadas.fxml"));
                fxmlJornadasController = loaderJornadas.getController();
                fxmlJornadasController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadPartidos() {
        try {
            if (partido == null) {
                partido = loaderPartidos.load(getClass().getResourceAsStream("/fxml/FXMLPartidos.fxml"));
                fxmlPartidosController = loaderPartidos.getController();
                fxmlPartidosController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadUsuarios() {
        try {
            if (pantallaUsuario == null) {
                pantallaUsuario = loaderUsuarios.load(getClass().getResourceAsStream("/fxml/FXMLUsuarios.fxml"));
                fxmlUsuariosController = loaderUsuarios.getController();
                fxmlUsuariosController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preloadUsuariosNormales() {
        try {
            if (pantallaUsuarioNormal == null) {
                pantallaUsuarioNormal = loaderUsuarioNormal.load(getClass().getResourceAsStream("/fxml/FXMLUsuarioNormal.fxml"));
                fxmlUsuariosController = loaderUsuarioNormal.getController();
                fxmlUsuariosController.setPrincipal(this);
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //CHARGE

    public void chargeLogin() {
        menuApp.getMenus().get(0).setVisible(false);
        menuApp.getMenus().get(1).setVisible(false);
        menuApp.getMenus().get(2).setVisible(true);
        root.setCenter(login);

    }

    public void chargeRegistro() {
        root.setCenter(registro);

    }

    public void chargeWelcome() {
        fxmlWelcomeController.setLogin(this.getUsuarioDTO());
        if (!usuarioDTO.getIdTipoUsuario().equals(ConstantesGUI.DOS)) {
            menuApp.getMenus().get(0).setVisible(false);
            menuApp.getMenus().get(1).setVisible(true);
            menuApp.getMenus().get(2).setVisible(false);
        } else {
            menuApp.getMenus().get(0).setVisible(true);
            menuApp.getMenus().get(1).setVisible(false);
            menuApp.getMenus().get(2).setVisible(false);
        }

        root.setCenter(welcome);
    }

    public void chargeEquipos() {
        root.setCenter(equipo);
        fxmlEquiposController.loadEquipos();
    }

    public void chargeJornadas() {
        root.setCenter(jornada);
        fxmlJornadasController.loadJornadas();
    }

    public void chargePartidos() {
        root.setCenter(partido);
        if (usuarioDTO.getIdTipoUsuario().equals(ConstantesGUI.DOS)) {
            fxmlPartidosController.getBtAdd().setVisible(true);
            fxmlPartidosController.getBtDel().setVisible(true);
            fxmlPartidosController.getBtUpdate().setVisible(true);
            fxmlPartidosController.getLvJornadas().setVisible(true);
            fxmlPartidosController.getLvEquiposLocal().setVisible(true);
            fxmlPartidosController.getLvEquiposVis().setVisible(true);
            fxmlPartidosController.getTfResultados().setVisible(true);
            fxmlPartidosController.getLEquiVis().setVisible(true);
            fxmlPartidosController.getLResultados().setVisible(true);
            fxmlPartidosController.getLEquiLo().setVisible(true);
            fxmlPartidosController.getLJornadas().setVisible(true);

        } else {
            fxmlPartidosController.getBtAdd().setVisible(false);
            fxmlPartidosController.getBtDel().setVisible(false);
            fxmlPartidosController.getBtUpdate().setVisible(false);
            fxmlPartidosController.getLvJornadas().setVisible(false);
            fxmlPartidosController.getLvEquiposLocal().setVisible(false);
            fxmlPartidosController.getLvEquiposVis().setVisible(false);
            fxmlPartidosController.getTfResultados().setVisible(false);
            fxmlPartidosController.getLEquiVis().setVisible(false);
            fxmlPartidosController.getLResultados().setVisible(false);
            fxmlPartidosController.getLEquiLo().setVisible(false);
            fxmlPartidosController.getLJornadas().setVisible(false);
        }
        fxmlPartidosController.loadEquipos();
        fxmlPartidosController.loadJornadas();
        fxmlPartidosController.loadPartidos();

    }

    public void chargeUsuarios() {

        if (!usuarioDTO.getIdTipoUsuario().equals(ConstantesGUI.DOS)) {
            preloadUsuariosNormales();
            root.setCenter(pantallaUsuarioNormal);
        } else {
            preloadUsuarios();
            root.setCenter(pantallaUsuario);
            fxmlUsuariosController.loadTipos();
        }

        fxmlUsuariosController.loadUsuarios();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadLogin();
        preloadRegistro();
        preloadWelcome();
        preloadEquipos();
        preloadJornadas();
        preloadPartidos();

        chargeLogin();
    }
}
