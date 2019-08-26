package sumit.com.openweatherdemo.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sumit.com.openweatherdemo.App;
import sumit.com.openweatherdemo.retrofit.OpenWeatherApi;

@Module
public class NetworkModule {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory convertorFactory, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .addConverterFactory(convertorFactory)
                .build();
    }

    @Provides
    @Singleton
    OpenWeatherApi provideWeatherService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherApi.class);
    }

    @Provides
    @Singleton
    GsonConverterFactory provideConverterFactory(Gson gson) {
        return  GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Gson proviesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder().cache(cache).build();
    }

    @Provides
    @Singleton
    Cache providesCache(App application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

}
