package hvktqx.team.grabsimulation.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import dagger.android.support.AndroidSupportInjection
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.delegation.ReactiveSubscriptionDelegation
import hvktqx.team.grabsimulation.helper.ReactiveSubscription
import hvktqx.team.grabsimulation.screen.MainViewModel

abstract class BaseFragment : BaseMvRxFragment(), ReactiveSubscription by ReactiveSubscriptionDelegation() {

  override fun onAttach(context: Context) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  protected val parentViewModel: MainViewModel by activityViewModel()

  override fun invalidate() {
  }

  override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
//
//    activity?.onBackPressedDispatcher?.addCallback(this) {
//      if (!handleBackPress()) {
//        remove()
//      }
//    }
  }

  open fun handleBackPress(): Boolean = false

  protected fun navigate(fragment: Fragment, addToBackStack: Boolean = true) {
    fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.apply {
      if (addToBackStack) {
        addToBackStack(fragment.javaClass.simpleName)
      }
    }?.commit()
  }

  override fun onDestroy() {
    super.onDestroy()
    disposeAll()
  }
}