package quevedo.ClienteLiga.dao;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.retrofit.RetrofitJornadas;
import quevedo.common.modelos.Jornada;

import javax.inject.Inject;
import java.util.List;

public class DAOJornadas extends DAOGenerics {

    private final RetrofitJornadas retrofitJornadas;

    @Inject
    public DAOJornadas(RetrofitJornadas retrofitJornadas) {
        this.retrofitJornadas = retrofitJornadas;
    }

    public Either<String, List<Jornada>> getAll() {
        return safeApiCall(retrofitJornadas.getJornadas());
    }

    public Either<String, Jornada> insertJornada(Jornada jornada) {
        return safeApiCall(retrofitJornadas.insertJornada(jornada));
    }

    public Either<String, Jornada> updateJornada(Jornada jornada) {
        return safeApiCall(retrofitJornadas.updateJornada(jornada));
    }

    public Either<String, String> deleteJornada(String id) {
        return safeApiCall(retrofitJornadas.deleteJornada(id));
    }
}
