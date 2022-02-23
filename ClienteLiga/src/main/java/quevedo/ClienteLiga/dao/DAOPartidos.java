package quevedo.ClienteLiga.dao;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.retrofit.RetrofitPartidos;
import quevedo.common.modelos.Partido;

import javax.inject.Inject;
import java.util.List;

public class DAOPartidos extends DAOGenerics {
    private final RetrofitPartidos retrofitPartidos;

    @Inject
    public DAOPartidos(RetrofitPartidos retrofitPartidos) {
        this.retrofitPartidos = retrofitPartidos;
    }

    public Either<String, List<Partido>> getAll() {
        return safeApiCall(retrofitPartidos.getPartidos());
    }

    public Either<String, List<Partido>> filtroEquipos(String equipo) {
        return safeApiCall(retrofitPartidos.filtroEquipos(equipo));
    }

    public Either<String, List<Partido>> filtrosJornadas(String jornada) {
        return safeApiCall(retrofitPartidos.filtroJornadas(jornada));
    }

    public Either<String, Partido> savePartido(Partido partido) {
        return safeApiCall(retrofitPartidos.savePartido(partido));
    }

    public Either<String, Partido> updatePartido(Partido partido) {
        return safeApiCall(retrofitPartidos.updatePartido(partido));
    }

    public Either<String, String> deletePartido(String id) {
        return safeApiCall(retrofitPartidos.deletePartido(id));
    }

}
