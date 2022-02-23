package quevedo.ClienteLiga.dao.retrofit;

import quevedo.ClienteLiga.dao.utils.ConstantesPath;
import quevedo.common.modelos.Partido;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitPartidos {

    @GET(ConstantesPath.PATH_PARTIDOS)
    Call<List<Partido>> getPartidos();

    @GET(ConstantesPath.PATH_PARTIDOS_FILTRO_EQUIPOS)
    Call<List<Partido>> filtroEquipos(@Query(ConstantesPath.PARAMETER_EQUIPO) String equipo);

    @GET(ConstantesPath.PATH_PARTIDOS_FILTRO_JORNADA)
    Call<List<Partido>> filtroJornadas(@Query(ConstantesPath.PARAMETER_JORNADA) String jornada);

    @POST(ConstantesPath.PATH_PARTIDOS)
    Call<Partido> savePartido(@Body Partido partido);

    @PUT(ConstantesPath.PATH_PARTIDOS)
    Call<Partido> updatePartido(@Body Partido partido);

    @DELETE(ConstantesPath.PATH_DELETE_PARTIDOS)
    Call<String> deletePartido(@Path(ConstantesPath.PATH_PARAMETER_ID) String id);
}
