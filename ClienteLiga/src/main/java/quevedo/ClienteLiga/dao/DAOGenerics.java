package quevedo.ClienteLiga.dao;

import com.google.gson.Gson;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import quevedo.ClienteLiga.dao.utils.ConstantesDAO;
import quevedo.common.errores.ApiError;
import retrofit2.Call;
import retrofit2.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

@Log4j2
abstract class DAOGenerics {
    @Inject
    private Gson gson;

    public <T> Either<String, T> safeApiCall(Call<T> call) {
        Either<String, T> resultado;

        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                resultado = Either.right(response.body());
            } else {
                if (Objects.equals(response.errorBody().contentType(), MediaType.get(ConstantesDAO.MEDIA_TYPE)) || Objects.equals(response.errorBody().contentType(), MediaType.get(ConstantesDAO.MEIDA_TYPE_ALT))) {
                    ApiError apiError = gson.fromJson(response.errorBody().string(), ApiError.class);
                    resultado = Either.left(apiError.getMessage());
                } else {
                    resultado = Either.left(new ApiError(ConstantesDAO.ERROR_AL_CONECTARSE).getMessage());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new ApiError(ConstantesDAO.ERROR_SERVIDOR).getMessage());
        }

        return resultado;
    }
}
