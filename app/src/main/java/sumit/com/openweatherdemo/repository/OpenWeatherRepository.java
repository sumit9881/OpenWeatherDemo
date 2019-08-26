package sumit.com.openweatherdemo.repository;

import javax.inject.Inject;

import io.reactivex.Single;
import sumit.com.openweatherdemo.OpenWeatherConstants;
import sumit.com.openweatherdemo.model.pojos.OpenWeatherResponse;
import sumit.com.openweatherdemo.retrofit.OpenWeatherApi;

public class OpenWeatherRepository {



    @Inject
    public OpenWeatherApi openWeatherApi;

    @Inject
    public OpenWeatherRepository(OpenWeatherApi openWeatherApi) {
        this.openWeatherApi= openWeatherApi;
    }


    /**
     * get extended weather report for searched city.
     *
     * @param city  city name
     * @return response from server.
     */
    public Single<OpenWeatherResponse> getExtendedWeather(String city) {
        return openWeatherApi.getExtendedWeather(city, OpenWeatherConstants.FORMAT_JSON, OpenWeatherConstants.UNITS_METRIC, OpenWeatherConstants.COUNT, OpenWeatherConstants.APP_KEY);
    }



}
