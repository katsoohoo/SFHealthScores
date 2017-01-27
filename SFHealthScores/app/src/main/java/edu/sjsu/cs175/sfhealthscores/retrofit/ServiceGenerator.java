package edu.sjsu.cs175.sfhealthscores.retrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service generator for SF Open Data Food Inspection data.
 */
public class ServiceGenerator {

    private static final String API_BASE_URL = "https://data.sfgov.org/resource/sipz-fjte.json/";
    private static final String TOKEN_PARAM = "$$app_token";
    private static final String APP_TOKEN = "EHP9cc60VqCgdC7Nze8lhuELO";


    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder().addQueryParameter(
                    TOKEN_PARAM, APP_TOKEN).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        }
    }).build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    /**
     * Creates and returns service.
     *
     * @param serviceClass request service
     * @param <S>          type of service
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(okHttpClient).build();
        return retrofit.create(serviceClass);
    }
}