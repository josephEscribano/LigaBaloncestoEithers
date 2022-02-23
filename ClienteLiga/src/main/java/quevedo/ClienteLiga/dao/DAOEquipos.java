package quevedo.ClienteLiga.dao;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.retrofit.RetrofitEquipos;
import quevedo.common.modelos.Equipo;

import javax.inject.Inject;
import java.util.List;

public class DAOEquipos extends DAOGenerics {
    private final RetrofitEquipos retrofitEquipos;

    @Inject
    public DAOEquipos(RetrofitEquipos retrofitEquipos) {
        this.retrofitEquipos = retrofitEquipos;
    }


    public Either<String, List<Equipo>> getAll() {
        return safeApiCall(retrofitEquipos.getEquipos());
    }

    public Either<String, Equipo> insertEquipo(String nombre) {
        return safeApiCall(retrofitEquipos.insertEquipo(nombre));
    }

    public Either<String, Equipo> updateEquipo(Equipo equipo) {
        return safeApiCall(retrofitEquipos.updateEquipo(equipo));
    }

    public Either<String, String> deleteEquipo(String id) {
        return safeApiCall(retrofitEquipos.deleteEquipo(id));
    }

}
