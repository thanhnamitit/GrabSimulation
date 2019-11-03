package hvktqx.team.grabsimulation.screen.splash

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import hvktqx.team.grabsimulation.base.BaseViewModel
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.data.usecase.FetchCurrentPlaceUseCase

data class SplashState(
    val spot: Async<Spot> = Uninitialized
) : MvRxState

class SplashViewModel @AssistedInject constructor(
    @Assisted initialState: SplashState,
    private val fetchCurrentPlaceUseCase: FetchCurrentPlaceUseCase
) :
    BaseViewModel<SplashState>(initialState) {

    fun fetchCurrentPlace() {
        fetchCurrentPlaceUseCase().execute { spot ->
            copy(spot = spot)
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SplashState): SplashViewModel
    }

    companion object : MvRxViewModelFactory<SplashViewModel, SplashState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SplashState
        ): SplashViewModel? {
            return ((viewModelContext as FragmentViewModelContext).fragment as SplashFragment).viewModelFactory.create(
                state
            )
        }
    }
}