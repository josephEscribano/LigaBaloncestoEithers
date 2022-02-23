package quevedo.ClienteLiga.service;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.DAOEquipos;
import quevedo.common.modelos.Equipo;

import javax.inject.Inject;
import java.util.List;

public class ServiceEquipos {
    private final DAOEquipos daoEquipos;

    @Inject
    public ServiceEquipos(DAOEquipos daoEquipos) {
        this.daoEquipos = daoEquipos;
    }

    public Either<String, List<Equipo>> getAll() {
        return daoEquipos.getAll();
    }

    public Either<String, Equipo> insertEquipo(String nombre) {
        return daoEquipos.insertEquipo(nombre);
    }

    public Either<String, Equipo> updateEquipo(Equipo equipo) {
        return daoEquipos.updateEquipo(equipo);
    }

    public Either<String, String> deleteEquipo(String id) {
        return daoEquipos.deleteEquipo(id);
    }
}
