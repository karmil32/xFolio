package pl.karass32.xfolio.di.module

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import pl.karass32.xfolio.repository.db.AppDatabase
import javax.inject.Singleton

/**
 * Created by karas on 21.02.2018.
 */
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "database")
            .allowMainThreadQueries() //TODO remove
            .build()
}