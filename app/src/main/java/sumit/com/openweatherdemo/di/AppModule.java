package sumit.com.openweatherdemo.di;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sumit.com.openweatherdemo.App;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

}
