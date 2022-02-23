package quevedo.ClienteLiga.dao.retrofit;

import quevedo.ClienteLiga.dao.utils.ConstantesPath;
import quevedo.common.modelos.Equipo;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitEquipos {

    @GET(ConstantesPath.PATH_API_EQUIPOS)
    Call<List<Equipo>> getEquipos();

    @POST(ConstantesPath.PATH_INSERT_EQUIPOS)
    Call<Equipo> insertEquipo(@Path(ConstantesPath.PATH_PARAMETER_NAME) String nombre);

    @PUT(ConstantesPath.PATH_API_EQUIPOS)
    Call<Equipo> updateEquipo(@Body Equipo equipo);

    @DELETE(ConstantesPath.PATH_DELETE_EQUIPOS)
    Call<String> deleteEquipo(@Path(ConstantesPath.PATH_PARAMETER_ID) String id);
}
