package quevedo.servidorLiga.service;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Equipo;
import quevedo.servidorLiga.dao.DAOEquipos;

import java.util.List;

public class EquipoService {

    private final DAOEquipos daoEquipos;

    @Inject
    public EquipoService(DAOEquipos daoEquipos) {
        this.daoEquipos = daoEquipos;
    }

    public Either<ApiError, List<Equipo>> getAll() {
        return daoEquipos.getAll();
    }

    public Either<ApiError, Equipo> saveEquipo(String nombre) {
        return daoEquipos.saveEquipo(nombre);
    }

    public Either<ApiError, Equipo> updateEquipo(Equipo equipo) {
        return daoEquipos.updateEquipo(equipo);
    }

    public Either<ApiError, String> deleteEquipo(String id) {
        return daoEquipos.deleteEquipo(id);
    }
}
