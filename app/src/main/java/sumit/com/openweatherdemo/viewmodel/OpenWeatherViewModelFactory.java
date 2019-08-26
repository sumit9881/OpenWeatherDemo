package sumit.com.openweatherdemo.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import sumit.com.openweatherdemo.providers.CityNameProvider;
import sumit.com.openweatherdemo.rxjava.SchedulersFacade;
import sumit.com.openweatherdemo.usecases.LoadOpenWeatherResponseUsecase;

public class OpenWeatherViewModelFactory implements ViewModelProvider.Factory {

    private final LoadOpenWeatherResponseUsecase mLoadOpenWeatherResponseUsecase;
    private final SchedulersFacade mSchedulerFacade;
    private final CityNameProvider mCityNameProvider;


    public OpenWeatherViewModelFactory(LoadOpenWeatherResponseUsecase loadGrouponResponseUseCase, SchedulersFacade schedulersFacade, CityNameProvider cityNameProvider) {
        mLoadOpenWeatherResponseUsecase = loadGrouponResponseUseCase;
        mSchedulerFacade = schedulersFacade;
        mCityNameProvider = cityNameProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OpenWeatherViewModel.class)) {
            return (T) new OpenWeatherViewModel(mLoadOpenWeatherResponseUsecase, mSchedulerFacade, mCityNameProvider);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
