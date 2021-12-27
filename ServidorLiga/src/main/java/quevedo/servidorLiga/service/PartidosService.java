package quevedo.servidorLiga.service;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import quevedo.common.errores.ApiError;
import quevedo.common.modelos.Partido;
import quevedo.servidorLiga.dao.DAOPartidos;

import java.util.List;

public class PartidosService {
    private final DAOPartidos daoPartidos;

    @Inject
    public PartidosService(DAOPartidos daoPartidos) {
        this.daoPartidos = daoPartidos;
    }

    public Either<ApiError, List<Partido>> getAll(){
        return daoPartidos.getAll();
    }

    public Either<ApiError,Partido> savePartido(Partido partido){
        return daoPartidos.savePartido(partido);
    }

    public Either<ApiError,Partido> updatePartido(Partido partido){
        return daoPartidos.updatePartido(partido);
    }

    public Either<String,String> deletePartido(String id){
        return daoPartidos.deletePartido(id);
    }

    public Either<ApiError,List<Partido>> filtrar(String equipo,String jornada){
        return daoPartidos.filtrar(equipo, jornada);
    }

}
