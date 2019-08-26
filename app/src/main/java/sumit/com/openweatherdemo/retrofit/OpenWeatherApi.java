package sumit.com.openweatherdemo.retrofit;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sumit.com.openweatherdemo.model.pojos.OpenWeatherResponse;

public interface OpenWeatherApi {

    @GET("forecast/daily")
    Single<OpenWeatherResponse> getExtendedWeather(@Query("q") String location, @Query("mode") String mode, @Query("units") String unit, @Query("cnt") int count, @Query("appid") String appId);


}
