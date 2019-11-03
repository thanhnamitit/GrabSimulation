package hvktqx.team.grabsimulation.screen.splash

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.base.BaseFragment
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.screen.input.LocationInputFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class SplashFragment : BaseFragment() {

  companion object {
    const val REQUEST_CODE_ACCESS_FINE_LOCATION = 6626
  }

  @Inject
  lateinit var viewModelFactory: SplashViewModel.Factory
  private val viewModel: SplashViewModel by fragmentViewModel()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_splash, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    EasyPermissions.requestPermissions(
      this,
      "",
      REQUEST_CODE_ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION
    )
    viewModel.selectSubscribe(this, SplashState::spot) {
      if (it.complete) {
        it()?.let { spot ->
          parentViewModel.receiveNewCurrentLocation(spot)
          navigateToNextFragment(spot)
        }
      }
    }
  }

  @AfterPermissionGranted(REQUEST_CODE_ACCESS_FINE_LOCATION)
  fun onPermissionGranted() {
    viewModel.fetchCurrentPlace()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
  }

  private fun navigateToNextFragment(spot: Spot) {
    navigate(LocationInputFragment().apply {
      arguments = bundleOf(
        MvRx.KEY_ARG to spot
      )
    }, addToBackStack = false)
  }
}