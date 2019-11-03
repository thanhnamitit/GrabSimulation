package hvktqx.team.grabsimulation.screen.input

import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.withState
import hvktqx.team.grabsimulation.epoxy.predictionPlace

class PredictionPlacesController(
  private val fragment: LocationInputFragment,
  private val viewModel: LocationInputViewModel
) :
  EpoxyController() {
  override fun buildModels() {
    withState(viewModel) { state ->
      state.spots()?.forEach { spot ->
        predictionPlace {
          id(spot.id)
          spot(spot)
          onClickListener { _ -> fragment.onSpotSelected(spot) }
        }
      }
    }
  }
}