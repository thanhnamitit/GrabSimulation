package hvktqx.team.grabsimulation.screen.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.base.BaseFragment
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.functional.OneTimeValue
import hvktqx.team.grabsimulation.screen.trip.TripFragment
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_location_input.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationInputFragment : BaseFragment() {

  @Inject
  lateinit var viewModelFactory: LocationInputViewModel.Factory
  @Inject
  lateinit var controller: PredictionPlacesController

  val viewModel: LocationInputViewModel by fragmentViewModel()

  private val selectingOriginPublisher = PublishSubject.create<Boolean>()
  private val findingAddressPublisher = PublishSubject.create<String>()
  private val selectedSpotPublisher = PublishSubject.create<OneTimeValue<Spot>>()
  private val selectedSpot = Observable.combineLatest(selectingOriginPublisher, selectedSpotPublisher,
    BiFunction<Boolean, OneTimeValue<Spot>, Pair<Boolean, Spot?>> { selectingOrigin, spot ->
      Pair(selectingOrigin, spot())
    })

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_location_input, container, false)
  }

  private fun EditText.configToFetchPredictions(forOrigin: Boolean, onCleared: () -> Unit) {
    doOnTextChanged { text, _, _, _ ->
      if (text.isNullOrEmpty()) onCleared.invoke()
      findingAddressPublisher.onNext(text.toString())
    }
    setOnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        findingAddressPublisher.onNext(text.toString())
        onRequestToInputAddress(forOrigin)
      }
    }
    setOnClickListener {
      selectAll()
      onRequestToInputAddress(forOrigin)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    edt_origin?.configToFetchPredictions(true) {
      viewModel.clearOrigin()
    }
    edt_destination?.configToFetchPredictions(false) {
      viewModel.clearDestination()
    }
    findingAddressPublisher.debounce(200, TimeUnit.MILLISECONDS).safeSubscribe { address ->
      viewModel.fetchPlaces(address)
    }
    selectedSpot.safeSubscribe {
      it.second?.let { spot ->
        viewModel.onSpotSelected(it.first, spot)
      }
    }
    recycler_view?.apply {
      layoutManager = LinearLayoutManager(context)
      setController(controller)
    }
    viewModel.originSpotPublisher.safeSubscribeWrapper {
      if (it != null)
        edt_origin?.setText(it.address)
    }
    viewModel.destinationSpotPublisher.safeSubscribeWrapper {
      if (it != null)
        edt_destination?.setText(it.address)
    }
    viewModel.tripNavigator.observe(this, Observer {
      it()?.let { trip ->
        navigate(TripFragment().apply {
          arguments = bundleOf(
            MvRx.KEY_ARG to trip
          )
        })
      }
    })
    viewModel.fetchPlaces("laocai")
  }

  private fun onRequestToInputAddress(forOrigin: Boolean) {
    selectingOriginPublisher.onNext(forOrigin)
    if (motion_location_input?.currentState == R.id.start) {
      motion_location_input?.transitionToState(R.id.end)
    }
  }

  fun onSpotSelected(spot: Spot) {
    selectedSpotPublisher.onNext(OneTimeValue(spot))
  }

  override fun invalidate() = withState(viewModel) { state ->
    controller.requestModelBuild()
    progress_circular?.isVisible = state.spots is Loading
  }
}