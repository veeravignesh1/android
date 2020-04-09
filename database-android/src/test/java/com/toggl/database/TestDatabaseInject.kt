package com.toggl.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [
    TestRoomDatabaseModule::class,
    TestDatabaseModule::class
])
interface TestComponent {
    fun inject(test: TimeEntryDaoTest)
}

@Module
class TestRoomDatabaseModule {
    @Provides
    @Singleton
    fun appDatabase(applicationContext: Context): TogglDatabase =
        Room.inMemoryDatabaseBuilder(
            applicationContext,
            TogglRoomDatabase::class.java
        )
            .allowMainThreadQueries().build()
}

@Module(includes = [
    TestRoomDatabaseModule::class
    // DatabaseDaoModule::class
])
class TestDatabaseModule {
    @Provides
    fun provideContext(): Context = ApplicationProvider.getApplicationContext()
}
