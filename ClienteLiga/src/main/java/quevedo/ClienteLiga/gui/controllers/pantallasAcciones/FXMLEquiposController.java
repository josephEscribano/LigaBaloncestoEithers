package quevedo.ClienteLiga.gui.controllers.pantallasAcciones;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteLiga.gui.controllers.FXMLPrincipalController;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import quevedo.ClienteLiga.service.ServiceEquipos;
import quevedo.common.modelos.Equipo;

import javax.inject.Inject;
import java.util.List;

public class FXMLEquiposController {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServiceEquipos serviceEquipos;
    @FXML
    private ListView<Equipo> lvEquipos;
    @FXML
    private TextField tfNombre;
    private FXMLPrincipalController principal;

    @Inject
    public FXMLEquiposController(ServiceEquipos serviceEquipos) {
        this.serviceEquipos = serviceEquipos;
    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        this.principal = fxmlPrincipalController;
    }

    public void loadEquipos() {

        Single<Either<String, List<Equipo>>> single = Single.fromCallable(serviceEquipos::getAll)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(list -> lvEquipos.getItems().setAll(list))
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


    public void insertEquipo() {
        Single<Either<String, Equipo>> single = Single.fromCallable(() -> serviceEquipos.insertEquipo(tfNombre.getText()))
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(equipo -> lvEquipos.getItems().add(equipo))
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

    public void showData() {
        Equipo equipo = lvEquipos.getSelectionModel().getSelectedItem();
        tfNombre.setText(equipo.getNombreEquipo());
    }

    public void actualizarEquipo() {
        Equipo equipo = lvEquipos.getSelectionModel().getSelectedItem();
        if (equipo != null) {
            equipo.setNombreEquipo(tfNombre.getText());
            int index = lvEquipos.getItems().indexOf(equipo);
            Single<Either<String, Equipo>> single = Single.fromCallable(() -> serviceEquipos.updateEquipo(equipo))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(equipoUpdate -> lvEquipos.getItems().set(index, equipoUpdate))
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

    }


    public void deleteEquipo() {
        Equipo equipo = lvEquipos.getSelectionModel().getSelectedItem();
        if (equipo != null) {
            Single<Either<String, String>> single = Single.fromCallable(() -> serviceEquipos.deleteEquipo(equipo.getIdEquipo()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(id -> lvEquipos.getItems().removeIf(equipo1 -> equipo1.getIdEquipo().equals(id)))
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
