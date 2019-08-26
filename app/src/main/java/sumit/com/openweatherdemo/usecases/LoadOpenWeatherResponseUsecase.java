package sumit.com.openweatherdemo.usecases;

import javax.inject.Inject;

import io.reactivex.Single;
import sumit.com.openweatherdemo.model.pojos.OpenWeatherResponse;
import sumit.com.openweatherdemo.repository.OpenWeatherRepository;

public class LoadOpenWeatherResponseUsecase {

    private final OpenWeatherRepository mOpenWeatherRepository;

    @Inject
    public LoadOpenWeatherResponseUsecase(OpenWeatherRepository openWeatherRepository) {
        this.mOpenWeatherRepository = openWeatherRepository;
    }

    /**
     *  Use case for getting city name. Prefer to call from secondary thread.
     * @param city
     * @return
     */
    public Single<OpenWeatherResponse> execute(String city) {
        return mOpenWeatherRepository.getExtendedWeather(city);
    }

}
