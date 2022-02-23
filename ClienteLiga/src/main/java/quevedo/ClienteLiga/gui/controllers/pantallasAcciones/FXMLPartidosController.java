package quevedo.ClienteLiga.gui.controllers.pantallasAcciones;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import lombok.Getter;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteLiga.gui.controllers.FXMLPrincipalController;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import quevedo.ClienteLiga.service.ServiceEquipos;
import quevedo.ClienteLiga.service.ServiceJornadas;
import quevedo.ClienteLiga.service.ServicePartidos;
import quevedo.common.modelos.Equipo;
import quevedo.common.modelos.Jornada;
import quevedo.common.modelos.Partido;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FXMLPartidosController {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServicePartidos servicePartidos;
    private final ServiceEquipos serviceEquipo;
    private final ServiceJornadas serviceJornadas;
    @FXML
    private Label lJornadas;
    @FXML
    private Label lEquiLo;
    @FXML
    private Label lResultados;
    @FXML
    private Label lEquiVis;
    @FXML
    private Button btAdd;
    @FXML
    private Button btDel;
    @FXML
    private Button btUpdate;
    @FXML
    private ListView<Equipo> lvEquiposVis;
    @FXML
    private ListView<Partido> lvPartidos;
    @FXML
    private ListView<Jornada> lvJornadas;
    @FXML
    private ListView<Equipo> lvEquiposLocal;
    @FXML
    private TextField tfResultados;
    @FXML
    private ComboBox<String> cbEquipos;
    @FXML
    private ComboBox<String> cbJornadas;
    private FXMLPrincipalController principal;


    @Inject
    public FXMLPartidosController(ServicePartidos servicePartidos, ServiceEquipos serviceEquipo, ServiceJornadas serviceJornadas) {
        this.servicePartidos = servicePartidos;
        this.serviceEquipo = serviceEquipo;
        this.serviceJornadas = serviceJornadas;
    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        this.principal = fxmlPrincipalController;
    }

    public void loadPartidos() {
        Single<Either<String, List<Partido>>> single = Single.fromCallable(servicePartidos::getAll)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(list -> lvPartidos.getItems().setAll(list))
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


    public void loadEquipos() {
        Single<Either<String, List<Equipo>>> single = Single.fromCallable(serviceEquipo::getAll)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(list -> {
                            lvEquiposLocal.getItems().setAll(list);
                            cbEquipos.getItems().setAll(list.stream().map(Equipo::getNombreEquipo).collect(Collectors.toList()));
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

    public void loadJornadas() {

        Single<Either<String, List<Jornada>>> single = Single.fromCallable(serviceJornadas::getAll)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

        single.subscribe(result -> result
                        .peek(list -> {
                            lvJornadas.getItems().setAll(list);
                            cbJornadas.getItems().setAll(list.stream().map(Jornada::getIdJornada).collect(Collectors.toList()));
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

    public void savePartido() {
        Equipo equipoLocal = lvEquiposLocal.getSelectionModel().getSelectedItem();
        Jornada jornada = lvJornadas.getSelectionModel().getSelectedItem();
        Equipo equipoVisitante = lvEquiposVis.getSelectionModel().getSelectedItem();
        if (equipoLocal != null && jornada != null && equipoVisitante != null && !tfResultados.getText().isEmpty()) {
            Single<Either<String, Partido>> single = Single.fromCallable(() -> servicePartidos.savePartido(new Partido(jornada.getIdJornada(), equipoLocal, equipoVisitante, tfResultados.getText())))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(partido -> lvPartidos.getItems().add(partido))
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
            alert.setContentText(ConstantesGUI.SELECCION_ELEMENTOS_PARTIDOS);
            alert.showAndWait();
        }

    }

    public void deletePartido() {
        Partido partido = lvPartidos.getSelectionModel().getSelectedItem();
        if (partido != null) {
            Single<Either<String, String>> single = Single.fromCallable(() -> servicePartidos.deletePartido(partido.getIdPartido()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(id -> lvPartidos.getItems().removeIf(partido1 -> partido1.getIdPartido().equals(id)))
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

    public void showData() {
        Partido partido = lvPartidos.getSelectionModel().getSelectedItem();
        tfResultados.setText(partido.getResultado());
    }

    public void actualizarPartido() {

        Partido partido = lvPartidos.getSelectionModel().getSelectedItem();
        if (partido != null) {
            partido.setResultado(tfResultados.getText());
            int index = lvPartidos.getItems().indexOf(partido);
            Single<Either<String, Partido>> single = Single.fromCallable(() -> servicePartidos.updatePartido(partido))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            single.subscribe(result -> result
                            .peek(partidoUpdate -> lvPartidos.getItems().set(index, partidoUpdate))
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
            alert.setContentText(ConstantesGUI.SELECCIONA_UN_PARTIDO);
            alert.showAndWait();
        }
    }

    public void chargeEquipoVisitante() {

        List<Equipo> aux = new ArrayList<>(lvEquiposLocal.getItems());
        aux.removeIf(equipo -> equipo.getIdEquipo().equals(lvEquiposLocal.getSelectionModel().getSelectedItem().getIdEquipo()));
        lvEquiposVis.getItems().setAll(aux);
    }


    public void filtroEquipos() {
        String equipo = cbEquipos.getSelectionModel().getSelectedItem();
        if (equipo != null) {

            Single<Either<String, List<Partido>>> single = Single.fromCallable(() -> servicePartidos.filtroEquipos(equipo))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            filtroCommon(single);
        } else {
            alert.setContentText(ConstantesGUI.MENSAJE_FILTROS);
            alert.showAndWait();
        }

    }

    public void filtroJornadas() {

        String jornada = cbJornadas.getSelectionModel().getSelectedItem();
        if (jornada != null) {

            Single<Either<String, List<Partido>>> single = Single.fromCallable(() -> servicePartidos.filtrosJornadas(jornada))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> principal.getRoot().setCursor(Cursor.DEFAULT));

            filtroCommon(single);
        } else {
            alert.setContentText(ConstantesGUI.SELECCIONA_UNA_JORNADA);
            alert.showAndWait();
        }
    }

    private void filtroCommon(Single<Either<String, List<Partido>>> single) {
        single.subscribe(result -> result

                        .peek(list -> lvPartidos.getItems().setAll(list))
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
