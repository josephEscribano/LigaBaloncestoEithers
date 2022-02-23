package quevedo.servidorLiga.service;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Jornada;
import quevedo.servidorLiga.dao.DAOJornadas;

import java.util.List;

public class JornadaService {

    private final DAOJornadas daoJornadas;

    @Inject
    public JornadaService(DAOJornadas daoJornadas) {
        this.daoJornadas = daoJornadas;
    }

    public Either<ApiError, List<Jornada>> getAll() {
        return daoJornadas.getAll();
    }

    public Either<ApiError, Jornada> saveJornada(Jornada jornada) {
        return daoJornadas.saveJornada(jornada);
    }

    public Either<ApiError, Jornada> updateJornada(Jornada jornada) {
        return daoJornadas.updateJornada(jornada);
    }

    public Either<ApiError, String> delteJornada(String id) {
        return daoJornadas.deleteJornada(id);
    }
}
