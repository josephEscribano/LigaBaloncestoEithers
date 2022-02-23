package quevedo.ClienteLiga.service;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.DAOJornadas;
import quevedo.common.modelos.Jornada;

import javax.inject.Inject;
import java.util.List;

public class ServiceJornadas {

    private final DAOJornadas daoJornadas;

    @Inject
    public ServiceJornadas(DAOJornadas daoJornadas) {
        this.daoJornadas = daoJornadas;
    }

    public Either<String, List<Jornada>> getAll() {
        return daoJornadas.getAll();
    }

    public Either<String, Jornada> insertJornada(Jornada jornada) {
        return daoJornadas.insertJornada(jornada);
    }

    public Either<String, Jornada> updateJornada(Jornada jornada) {
        return daoJornadas.updateJornada(jornada);
    }

    public Either<String, String> deleteJornada(String id) {
        return daoJornadas.deleteJornada(id);
    }
}
