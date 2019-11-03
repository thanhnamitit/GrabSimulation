package hvktqx.team.grabsimulation.screen.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.base.BaseFragment
import hvktqx.team.grabsimulation.data.model.Trip
import kotlinx.android.synthetic.main.fragment_trip.*

class TripFragment : BaseFragment() {
  private val trip by args<Trip>()
  private val viewModel: TripViewModel by fragmentViewModel()
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_trip, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    parentViewModel.trip.value = trip
  }

  override fun onDestroy() {
    super.onDestroy()
    parentViewModel.trip.value = null
  }

  override fun invalidate() = withState(viewModel) {
    tv_start_address.text = trip.startAddress
    tv_end_address.text = trip.endAddress
    tv_fare.text = trip.fare
  }
}