package hvktqx.team.grabsimulation.screen.input

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import hvktqx.team.grabsimulation.base.BaseViewModel
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.data.model.Trip
import hvktqx.team.grabsimulation.data.usecase.FetchPredictionPlacesUseCase
import hvktqx.team.grabsimulation.data.usecase.GetRouteUseCase
import hvktqx.team.grabsimulation.functional.OneTimeValue
import hvktqx.team.grabsimulation.functional.Wrapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.Serializable

data class LocationInputState(
  val currentSpot: Spot,
  val spots: Async<List<Spot>> = Uninitialized
) : Serializable, MvRxState

class LocationInputViewModel @AssistedInject constructor(
  @Assisted initialState: LocationInputState,
  private val fetchPredictionPlaces: FetchPredictionPlacesUseCase,
  private val getRouteUseCase: GetRouteUseCase
) : BaseViewModel<LocationInputState>(initialState) {
  val TAG = "LocationInputViewModel"

  val originSpotPublisher = BehaviorSubject.create<Wrapper<Spot>>()
  val destinationSpotPublisher = BehaviorSubject.create<Wrapper<Spot>>()
  val tripNavigator = MutableLiveData<OneTimeValue<Trip>>()

  private val spotsPairObservable = Observable.combineLatest(originSpotPublisher, destinationSpotPublisher,
    BiFunction<Wrapper<Spot>, Wrapper<Spot>, Pair<Spot?, Spot?>> { first, second ->
      Pair(first.value, second.value)
    })

  init {
    originSpotPublisher.onNext(Wrapper(initialState.currentSpot))
    spotsPairObservable.safeSubscribe {
      it.takeIf {
        it.first != null && it.second != null
      }?.let { pair ->
        onRouteSelected(pair.first!!, pair.second!!)
      }
    }
  }

  fun clearOrigin() {
    originSpotPublisher.onNext(Wrapper.empty())
  }

  fun clearDestination() {
    destinationSpotPublisher.onNext(Wrapper.empty())
  }

  private fun onRouteSelected(origin: Spot, destination: Spot) {
    Log.i(TAG, origin.toString())
    Log.i(TAG, destination.toString())
    getRouteUseCase(origin.id, destination.id)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread()).execute {
        if (it is Success) {
          tripNavigator.postValue(OneTimeValue(it()!!))
        }
        this
      }
  }

  fun fetchPlaces(address: String) = withState {
    fetchPredictionPlaces(address, it.currentSpot.latLng!!).execute { spots ->
      copy(spots = spots)
    }
  }

  fun onSpotSelected(forOrigin: Boolean, spot: Spot) {
    if (forOrigin) {
      originSpotPublisher
    } else {
      destinationSpotPublisher
    }.onNext(Wrapper(spot))
  }

  @AssistedInject.Factory
  interface Factory {
    fun create(initialState: LocationInputState): LocationInputViewModel
  }

  companion object : MvRxViewModelFactory<LocationInputViewModel, LocationInputState> {
    override fun create(
      viewModelContext: ViewModelContext,
      state: LocationInputState
    ): LocationInputViewModel? {
      return ((viewModelContext as FragmentViewModelContext).fragment as LocationInputFragment).viewModelFactory.create(
        state
      )
    }

    override fun initialState(viewModelContext: ViewModelContext): LocationInputState? {
      val currentSpot =
        ((viewModelContext as FragmentViewModelContext).fragment as LocationInputFragment).arguments?.getParcelable<Spot>(
          MvRx.KEY_ARG
        )!!
      return LocationInputState(currentSpot, Uninitialized)
    }
  }
}