package com.example.lesson_12_fokin.di.applicationComponent.modules

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

/** Главный модуль, содержащий в себе все остальные модули */
@Module(
    includes = [
        ApiServiceModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        RepositoryModule::class,
        AndroidSupportInjectionModule::class,
    ]
)
interface AppModule {
}