package sumit.com.openweatherdemo.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sumit.com.openweatherdemo.ui.MainActivity;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = LobbyModule.class)
    abstract MainActivity bindMainActivity();

    // Add bindings for other sub-components here
}