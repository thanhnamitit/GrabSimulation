package hvktqx.team.grabsimulation.di.module

import android.content.Context
import dagger.Binds
import dagger.Module
import hvktqx.team.grabsimulation.App

@Module
abstract class ResourceModule {
  @Binds
  abstract fun bindContext(app: App): Context
}