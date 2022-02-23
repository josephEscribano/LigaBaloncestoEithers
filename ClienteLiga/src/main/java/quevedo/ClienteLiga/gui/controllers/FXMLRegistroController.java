package quevedo.ClienteLiga.gui.controllers;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import quevedo.ClienteLiga.service.ServiceUsuarios;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;

import javax.inject.Inject;

public class FXMLRegistroController {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServiceUsuarios serviceUsuarios;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfPass;
    private FXMLPrincipalController principal;

    @Inject
    public FXMLRegistroController(ServiceUsuarios serviceUsuarios) {
        this.serviceUsuarios = serviceUsuarios;
    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        this.principal = fxmlPrincipalController;
    }

    public void saveUsuario() {

        Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> serviceUsuarios.saveUsuario(new UsuarioRegistroDTO(tfUserName.getText(), tfCorreo.getText(), tfPass.getText(), "1")))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(usuarioDTO -> {
                            alert.setContentText(ConstantesGUI.MENSAJE_CORREO + usuarioDTO.getUserName() + ConstantesGUI.MENSAJE_CORREO2);
                            alert.showAndWait();
                        })
                        .peekLeft(error -> {
                            alert.setContentText(error);
                            alert.showAndWait();
                        }),
                throwable -> {
                    alert.setContentText(throwable.getMessage());
                    alert.showAndWait();
                });

        principal.getRoot().setCursor(Cursor.WAIT);
    }
}
