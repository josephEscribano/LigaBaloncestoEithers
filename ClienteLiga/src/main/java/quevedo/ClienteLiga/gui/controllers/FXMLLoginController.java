package quevedo.ClienteLiga.gui.controllers;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteLiga.service.ServiceUsuarios;
import quevedo.common.modelos.ApiRespuesta;
import quevedo.common.modelos.UsuarioDTO;

import javax.inject.Inject;

public class FXMLLoginController {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServiceUsuarios serviceUsuarios;
    @FXML
    private TextField tfUser;
    @FXML
    private TextField tfPass;
    @FXML
    private Label errorBox;
    private FXMLPrincipalController principal;

    @Inject
    public FXMLLoginController(ServiceUsuarios serviceUsuarios) {
        this.serviceUsuarios = serviceUsuarios;
    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        this.principal = fxmlPrincipalController;
    }

    public void doLogin() {
        Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> serviceUsuarios.doLogin(tfUser.getText(), tfPass.getText()))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(usuarioDTO -> {
                            principal.setUsuarioDTO(usuarioDTO);
                            principal.chargeWelcome();
                        })
                        .peekLeft(error -> errorBox.setText(error)),
                throwable -> {
                    alert.setContentText(throwable.getMessage());
                    alert.showAndWait();
                });

        principal.getRoot().setCursor(Cursor.WAIT);

    }

    public void reenviarCorreo() {
        Single<Either<String, ApiRespuesta>> single = Single.fromCallable(() -> serviceUsuarios.reenviarCorreo(tfUser.getText()))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(apiRespuesta -> {
                            alert.setContentText(apiRespuesta.getMessage());
                            alert.showAndWait();
                        })
                        .peekLeft(error -> errorBox.setText(error)),
                throwable -> {
                    alert.setContentText(throwable.getMessage());
                    alert.showAndWait();
                });

        principal.getRoot().setCursor(Cursor.WAIT);
    }


}
