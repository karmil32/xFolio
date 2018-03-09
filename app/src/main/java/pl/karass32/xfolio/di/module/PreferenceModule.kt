package pl.karass32.xfolio.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepositoryImpl
import javax.inject.Singleton

/**
 * Created by karas on 08.03.2018.
 */

@Module
class PreferenceModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferencesRepository = SharedPreferencesRepositoryImpl(application)
}