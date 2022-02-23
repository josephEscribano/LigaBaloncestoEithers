package quevedo.ClienteLiga.gui;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import quevedo.ClienteLiga.config.ConfigurationSingletonClient;
import quevedo.ClienteLiga.dao.cookie.JavaCookieJar;
import quevedo.ClienteLiga.dao.retrofit.RetrofitEquipos;
import quevedo.ClienteLiga.dao.retrofit.RetrofitJornadas;
import quevedo.ClienteLiga.dao.retrofit.RetrofitPartidos;
import quevedo.ClienteLiga.dao.retrofit.RetrofitUsuarios;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DI {

    @Produces
    @Singleton
    public CookieManager getCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }

    @Produces
    @Singleton
    public OkHttpClient getOKHttpClient() {
        return new OkHttpClient.Builder()
                .protocols(List.of(Protocol.HTTP_1_1))
                .readTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .callTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .connectTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .cookieJar(new JavaCookieJar(getCookieManager()))
                .build();
    }

    @Produces
    @Singleton
    @Named(ConstantesGUI.PATHBASE)
    public String getPathBase(ConfigurationSingletonClient configurationSingletonClient) {
        return configurationSingletonClient.getPathbase();
    }

    @Produces
    @Singleton
    public Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, jsonDeserializationContext) ->
                        LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                        (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.toString()))
                .create();
    }

    @Produces
    @Singleton
    public Retrofit createRetrofit(OkHttpClient client, @Named(ConstantesGUI.PATHBASE) String pathBase) {
        return new Retrofit.Builder()
                .baseUrl(pathBase)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Produces
    public RetrofitUsuarios apiUsuarios(Retrofit retrofit) {
        return retrofit.create(RetrofitUsuarios.class);
    }

    @Produces
    public RetrofitEquipos apiEquipos(Retrofit retrofit) {
        return retrofit.create(RetrofitEquipos.class);
    }

    @Produces
    public RetrofitJornadas apiJornadas(Retrofit retrofit) {
        return retrofit.create(RetrofitJornadas.class);
    }

    @Produces
    public RetrofitPartidos apiPartidos(Retrofit retrofit) {
        return retrofit.create(RetrofitPartidos.class);
    }

}
