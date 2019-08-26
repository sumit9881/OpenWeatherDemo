package sumit.com.openweatherdemo.di;



import dagger.Module;
import dagger.Provides;
import sumit.com.openweatherdemo.providers.CityNameProvider;
import sumit.com.openweatherdemo.repository.OpenWeatherRepository;
import sumit.com.openweatherdemo.retrofit.OpenWeatherApi;
import sumit.com.openweatherdemo.rxjava.SchedulersFacade;
import sumit.com.openweatherdemo.usecases.LoadOpenWeatherResponseUsecase;
import sumit.com.openweatherdemo.viewmodel.OpenWeatherViewModelFactory;

/**
 * Define LobbyActivity-specific dependencies here.
 */
@Module
public class LobbyModule {

    @Provides
    OpenWeatherRepository provideOpenWeatherRepository(OpenWeatherApi api) {
        return new OpenWeatherRepository(api);
    }

    @Provides
    OpenWeatherViewModelFactory provideOpenWeatherViewModelFactory(LoadOpenWeatherResponseUsecase loadOpenWeatherResponseUsecase,
                                                                   SchedulersFacade schedulersFacade,
                                                                   CityNameProvider cityNameProvider) {
        return new OpenWeatherViewModelFactory(loadOpenWeatherResponseUsecase, schedulersFacade, cityNameProvider);
    }

}