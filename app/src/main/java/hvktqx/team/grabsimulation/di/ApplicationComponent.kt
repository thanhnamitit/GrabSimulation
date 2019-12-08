package hvktqx.team.grabsimulation.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import hvktqx.team.grabsimulation.App
import hvktqx.team.grabsimulation.di.module.FirebaseModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        FirebaseModule::class
    ]

)
interface ApplicationComponent : AndroidInjector<App> {
    override fun inject(instance: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): ApplicationComponent
    }
}