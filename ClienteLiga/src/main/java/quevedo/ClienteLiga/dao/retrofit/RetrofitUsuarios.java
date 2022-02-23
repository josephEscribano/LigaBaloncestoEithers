package quevedo.ClienteLiga.dao.retrofit;

import quevedo.ClienteLiga.dao.utils.ConstantesDAO;
import quevedo.ClienteLiga.dao.utils.ConstantesPath;
import quevedo.common.modelos.ApiRespuesta;
import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.common.modelos.UsuarioUpdateDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitUsuarios {

    @GET(ConstantesPath.PATH_DO_LOGIN)
    Call<UsuarioDTO> doLogin(@Query(ConstantesDAO.PARAMETER_USER) String user, @Query(ConstantesDAO.PARAMETER_PASS) String pass);

    @GET(ConstantesPath.PATH_LOGOUT)
    Call<String> doLogout();

    @PUT(ConstantesPath.PATH_REENVIAR_CORREO)
    Call<ApiRespuesta> reenviarCorreo(@Query(ConstantesPath.PATH_PARAMETER_USER) String userName);

    @GET(ConstantesPath.PATH_API_USUARIOS)
    Call<List<UsuarioDTO>> getUsuarios();

    @POST(ConstantesPath.PATH_API_USUARIOS)
    Call<UsuarioDTO> saveUsuario(@Body UsuarioRegistroDTO usuarioRegistroDTO);

    @POST(ConstantesDAO.PATH_API_USUARIOS_INSERADMIN)
    Call<UsuarioDTO> saveAdmin(@Body UsuarioRegistroDTO usuarioRegistroDTO);

    @PUT(ConstantesPath.PATH_API_USUARIOS)
    Call<UsuarioDTO> updateUsuario(@Body UsuarioUpdateDTO usuarioUpdateDTO);

    @DELETE(ConstantesPath.PATH_UPDATE_USUARIOS)
    Call<String> deleteUsuario(@Path(ConstantesPath.PATH_PARAMETER_ID) String id);

    @PUT(ConstantesPath.PATH_API_USUARIOS_CAMBIOPASS)
    Call<UsuarioDTO> cambiarPass(@Body UsuarioDTO usuarioDTO);


}
