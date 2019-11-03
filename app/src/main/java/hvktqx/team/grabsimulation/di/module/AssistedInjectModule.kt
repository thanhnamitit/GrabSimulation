package hvktqx.team.grabsimulation.di.module

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import hvktqx.team.grabsimulation.screen.splash.SplashViewModel
import hvktqx.team.grabsimulation.screen.splash.SplashViewModel_AssistedFactory

@AssistedModule
@Module(includes = [AssistedInject_AssistedInjectModule::class])
abstract class AssistedInjectModule {
  abstract fun bindSplashViewModelFactory(factory: SplashViewModel_AssistedFactory): SplashViewModel.Factory
}
