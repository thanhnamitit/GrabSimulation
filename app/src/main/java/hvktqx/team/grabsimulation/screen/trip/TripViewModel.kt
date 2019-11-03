package hvktqx.team.grabsimulation.screen.trip

import com.airbnb.mvrx.MvRxState
import hvktqx.team.grabsimulation.base.BaseViewModel
import hvktqx.team.grabsimulation.data.model.Trip

data class TripState(val trip: Trip) : MvRxState {

}

class TripViewModel(initialState: TripState) : BaseViewModel<TripState>(initialState)