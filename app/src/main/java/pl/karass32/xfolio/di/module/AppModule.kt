package pl.karass32.xfolio.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by karas on 20.02.2018.
 */
@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext() = application
}