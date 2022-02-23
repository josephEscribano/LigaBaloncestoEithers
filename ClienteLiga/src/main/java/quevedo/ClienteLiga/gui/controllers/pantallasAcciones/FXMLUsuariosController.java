package quevedo.ClienteLiga.gui.controllers.pantallasAcciones;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteLiga.gui.controllers.FXMLPrincipalController;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import quevedo.ClienteLiga.service.ServiceUsuarios;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.common.modelos.UsuarioUpdateDTO;

import javax.inject.Inject;
import java.util.List;


public class FXMLUsuariosController {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServiceUsuarios serviceUsuarios;
    @FXML
    private TextField tfPass;
    @FXML
    private ListView<UsuarioDTO> lvUsuarios;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfCorreo;
    @FXML
    private ComboBox<String> cbTipo;
    private FXMLPrincipalController principal;

    @Inject
    public FXMLUsuariosController(ServiceUsuarios serviceUsuarios) {
        this.serviceUsuarios = serviceUsuarios;
    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        this.principal = fxmlPrincipalController;
    }

    public void loadTipos() {
        cbTipo.getItems().setAll(ConstantesGUI.TIPO_ADMINISTRADOR, ConstantesGUI.TIPO_USUARIO);
    }

    public void loadUsuarios() {
        if (principal.getUsuarioDTO().getIdTipoUsuario().equals(ConstantesGUI.DOS)) {
            Single<Either<String, List<UsuarioDTO>>> single = Single.fromCallable(serviceUsuarios::getAll)
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(list -> lvUsuarios.getItems().setAll(list))
                            .peekLeft(error -> {
                                alert.setContentText(error);
                                alert.showAndWait();
                            }),
                    throwable -> {
                        alert.setContentText(throwable.getMessage());
                        alert.showAndWait();
                    });

            principal.getRoot().setCursor(Cursor.WAIT);
        } else {
            tfUsername.setText(principal.getUsuarioDTO().getUserName());
            tfCorreo.setText(principal.getUsuarioDTO().getCorreo());
        }
    }

    public void cambioPass() {
        if (principal.getUsuarioDTO().getIdTipoUsuario().equals(ConstantesGUI.DOS)) {
            UsuarioDTO usuarioDTO = lvUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioDTO != null) {
                Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> serviceUsuarios.cambiarPass(usuarioDTO))
                        .subscribeOn(Schedulers.io())
                        .observeOn(JavaFxScheduler.platform())
                        .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

                single.subscribe(result -> result
                                .peek(usuario -> {
                                    alert.setContentText(ConstantesGUI.MENSAJE_CONFIRMACION_PASS + usuarioDTO.getUserName());
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
            } else {
                alert.setContentText(ConstantesGUI.SELECCIONA_UN_USUARIO);
                alert.showAndWait();
            }

        } else {
            Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> serviceUsuarios.cambiarPass(principal.getUsuarioDTO()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(usuario -> {
                                alert.setContentText(ConstantesGUI.MENSAJE_CONFIRMACION_PASS + principal.getUsuarioDTO().getUserName());
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
        }

    }


    public void saveAdmin() {
        Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> {
                    Either<String, UsuarioDTO> resultado;
                    if (cbTipo.getValue().equals(ConstantesGUI.TIPO_ADMINISTRADOR)) {
                        resultado = serviceUsuarios.saveAdmin(new UsuarioRegistroDTO(tfUsername.getText(), tfCorreo.getText(), tfPass.getText(), ConstantesGUI.DOS));
                    } else {
                        resultado = serviceUsuarios.saveUsuario(new UsuarioRegistroDTO(tfUsername.getText(), tfCorreo.getText(), tfPass.getText(), ConstantesGUI.UNO));
                    }
                    return resultado;
                })
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

    public void showData(MouseEvent mouseEvent) {
        tfPass.clear();
        cbTipo.setValue(null);
        UsuarioDTO usuario = lvUsuarios.getSelectionModel().getSelectedItem();
        tfUsername.setText(usuario.getUserName());
        tfCorreo.setText(usuario.getCorreo());

    }

    public void updateUsuario() {
        if (principal.getUsuarioDTO().getIdTipoUsuario().equals(ConstantesGUI.DOS)) {
            UsuarioDTO usuario = lvUsuarios.getSelectionModel().getSelectedItem();
            if (usuario != null) {

                int index = lvUsuarios.getItems().indexOf(usuario);
                Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> serviceUsuarios.updateUsuario(new UsuarioUpdateDTO(usuario.getIdUsuario(), tfUsername.getText(), tfCorreo.getText())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(JavaFxScheduler.platform())
                        .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

                single.subscribe(result -> result
                                .peek(usuarioDTO -> lvUsuarios.getItems().set(index, usuarioDTO))
                                .peekLeft(error -> {
                                    alert.setContentText(error);
                                    alert.showAndWait();
                                }),
                        throwable -> {
                            alert.setContentText(throwable.getMessage());
                            alert.showAndWait();
                        });

                principal.getRoot().setCursor(Cursor.WAIT);
            } else {
                alert.setContentText(ConstantesGUI.SELECCIONA_UN_EQUIPO);
                alert.showAndWait();
            }
        } else {

            Single<Either<String, UsuarioDTO>> single = Single.fromCallable(() -> serviceUsuarios.updateUsuario(new UsuarioUpdateDTO(principal.getUsuarioDTO().getIdUsuario(), tfUsername.getText(), tfCorreo.getText())))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(usuarioDTO -> {
                                alert.setContentText(ConstantesGUI.MENSAJE_UPDATE);
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

    public void deleteUsuario() {

        UsuarioDTO usuario = lvUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            Single<Either<String, String>> single = Single.fromCallable(() -> serviceUsuarios.deleteUsuario(usuario.getIdUsuario()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(id -> lvUsuarios.getItems().removeIf(usuarioDTO -> usuarioDTO.getIdUsuario().equals(id)))
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


}
