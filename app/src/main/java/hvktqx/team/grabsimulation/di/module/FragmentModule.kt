package com.example.epoxytut.di.module

import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import hvktqx.team.grabsimulation.data.usecase.FetchPredictionPlacesUseCase
import hvktqx.team.grabsimulation.screen.input.LocationInputFragment
import hvktqx.team.grabsimulation.screen.input.PredictionPlacesController
import hvktqx.team.grabsimulation.screen.map.MapsFragment
import hvktqx.team.grabsimulation.screen.splash.SplashFragment
import hvktqx.team.grabsimulation.screen.trip.TripFragment

@Module
abstract class FragmentModule {

  @ContributesAndroidInjector
  abstract fun mapsFragment(): MapsFragment

  @ContributesAndroidInjector
  abstract fun splashFragment(): SplashFragment

  @ContributesAndroidInjector(modules = [LocationInputModule::class])
  abstract fun locationInputFragment(): LocationInputFragment

  @ContributesAndroidInjector
  abstract fun tripFragment(): TripFragment

  @Module
  object LocationInputModule {

    @Provides
    @JvmStatic
    fun provideController(fragment: LocationInputFragment) =
      PredictionPlacesController(fragment, fragment.viewModel)

    @Provides
    @JvmStatic
    fun provideFetchPredictionPlacesUseCase(
      fragment: LocationInputFragment,
      placesClient: PlacesClient
    ): FetchPredictionPlacesUseCase {
      return FetchPredictionPlacesUseCase(
//                (fragment.arguments!!.getSerializable(MvRx.KEY_ARG) as Spot).latLng,
        placesClient
      )
    }
  }
}