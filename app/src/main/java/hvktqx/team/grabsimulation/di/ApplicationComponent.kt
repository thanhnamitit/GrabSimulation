package com.example.epoxytut.di

import com.example.epoxytut.di.module.FragmentModule
import com.example.epoxytut.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import hvktqx.team.grabsimulation.App
import hvktqx.team.grabsimulation.di.module.AssistedInjectModule
import hvktqx.team.grabsimulation.di.module.FirebaseModule
import hvktqx.team.grabsimulation.di.module.LocationModule
import hvktqx.team.grabsimulation.di.module.ResourceModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ResourceModule::class,
        NetworkModule::class,
        LocationModule::class,
        FragmentModule::class,
        FirebaseModule::class,
        AssistedInjectModule::class
    ]

)
interface ApplicationComponent : AndroidInjector<App> {
    override fun inject(instance: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}