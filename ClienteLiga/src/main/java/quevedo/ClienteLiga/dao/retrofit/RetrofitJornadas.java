package quevedo.ClienteLiga.dao.retrofit;

import quevedo.ClienteLiga.dao.utils.ConstantesPath;
import quevedo.common.modelos.Jornada;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitJornadas {

    @GET(ConstantesPath.PATH_API_JORNADAS)
    Call<List<Jornada>> getJornadas();

    @POST(ConstantesPath.PATH_API_JORNADAS)
    Call<Jornada> insertJornada(@Body Jornada jornada);

    @PUT(ConstantesPath.PATH_API_JORNADAS)
    Call<Jornada> updateJornada(@Body Jornada jornada);

    @DELETE(ConstantesPath.PATH_DELETE_JORNADAS)
    Call<String> deleteJornada(@Path(ConstantesPath.PATH_PARAMETER_ID) String id);


}
