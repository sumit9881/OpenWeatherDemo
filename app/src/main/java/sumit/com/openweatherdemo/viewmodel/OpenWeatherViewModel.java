package sumit.com.openweatherdemo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.VisibleForTesting;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import sumit.com.openweatherdemo.Exceptions.PermissionNotGrantedException;
import sumit.com.openweatherdemo.model.pojos.List;
import sumit.com.openweatherdemo.model.pojos.Weather;
import sumit.com.openweatherdemo.providers.CityNameProvider;
import sumit.com.openweatherdemo.rxjava.SchedulersFacade;
import sumit.com.openweatherdemo.usecases.LoadOpenWeatherResponseUsecase;

public class OpenWeatherViewModel extends ViewModel {



    private final LoadOpenWeatherResponseUsecase mLoadOpenWeatherResponseUsecase;

    private final SchedulersFacade mSchedulersFacade;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Response> mResponse = new MutableLiveData<>();

    SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yy");
    CityNameProvider mCityNameProvider;

    public OpenWeatherViewModel(LoadOpenWeatherResponseUsecase usecase, SchedulersFacade schedulersFacade, CityNameProvider cityNameProvider) {
        mLoadOpenWeatherResponseUsecase = usecase;
        mSchedulersFacade = schedulersFacade;
        mCityNameProvider = cityNameProvider;

    }

    public MutableLiveData<Response> response() {
        return mResponse;
    }


    /**
     * Lodas extended weather of 15 days.
     * @param city  name of city of which weather has to be found.
     *
     */
    public void loadExtendedWeather(final String city) {

        Disposable d = mLoadOpenWeatherResponseUsecase.execute(city)
                .subscribeOn(mSchedulersFacade.io())
                .observeOn(mSchedulersFacade.ui())
                .doOnSubscribe(s -> mResponse.setValue(Response.loading()))
                .toObservable()
                .map(s -> s.getList())
                .flatMap(items -> Observable.fromIterable(items))
                .map(item -> getWeatherDetailModel(item))
                .toList()
                .subscribe(
                        result -> {
                            if (result.isEmpty()) {
                                mResponse.setValue(Response.error("No Result Found"));
                            }
                            mResponse.setValue(Response.success(result));
                        },
                        throwable -> mResponse.setValue(Response.error(throwable))
                );
        mCompositeDisposable.add(d);
    }


    @VisibleForTesting
    private WeatherDetailModel getWeatherDetailModel(List response) {
        WeatherDetailModel weatherDetailModel = new WeatherDetailModel();
        weatherDetailModel.setDayTemprature(response.getTemp().getDay());
        weatherDetailModel.setMinTemprature(response.getTemp().getMin());
        weatherDetailModel.setMaxTemparature(response.getTemp().getMax());
        weatherDetailModel.setDate( mDateFormat.format(new Date(response.getDt() * 1000L)));

        java.util.List<Weather> weathers = response.getWeather();
        Weather weather = weathers.get(0);
        if (weather != null) {
            weatherDetailModel.setDescription(weather.getDescription());
            weatherDetailModel.setMain(weather.getMain());
        }

        return weatherDetailModel;
    }


    /**
     * load current city weather.
     *
     */
    public void loadCurrentCityWeather() {
        Disposable d = mCityNameProvider.getCity().subscribeOn(mSchedulersFacade.io())
                .observeOn(mSchedulersFacade.ui())
                .subscribe(result -> {
                            loadExtendedWeather(result);
                        },
                        throwable -> {
                            if (throwable instanceof PermissionNotGrantedException) {
                                mResponse.setValue(Response.showPermission());
                            } else {
                                mResponse.setValue(Response.error(throwable.getMessage()));
                            }
                        });
        mCompositeDisposable.add(d);
    }

    public void clearErrorUI() {
        mResponse.setValue(Response.clearError());
    }

    @Override
    protected void onCleared() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }

    }


}