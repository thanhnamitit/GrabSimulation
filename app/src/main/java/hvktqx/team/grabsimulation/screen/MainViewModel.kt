package hvktqx.team.grabsimulation.screen

import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.data.model.Trip

data class MainState(val currentLocation: Async<Spot> = Uninitialized) : MvRxState

class MainViewModel(initState: MainState) :
  BaseMvRxViewModel<MainState>(initState, debugMode = true) {

  val trip = MutableLiveData<Trip?>()

  fun receiveNewCurrentLocation(spot: Spot) {
    setState {
      copy(currentLocation = Success(spot))
    }
  }
}