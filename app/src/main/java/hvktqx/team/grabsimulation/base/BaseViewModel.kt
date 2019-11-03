package hvktqx.team.grabsimulation.base

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.MvRxState
import hvktqx.team.grabsimulation.delegation.ReactiveSubscriptionDelegation
import hvktqx.team.grabsimulation.helper.ReactiveSubscription
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel<STATE : MvRxState>(state: STATE) :
    BaseMvRxViewModel<STATE>(state, debugMode = BuildConfig.DEBUG),
    ReactiveSubscription by ReactiveSubscriptionDelegation() {
    override fun onCleared() {
        super.onCleared()
        disposeAll()
    }

    fun <T> Observable<T>.executeWithoutSetState(callback: (T) -> Unit) {
        this.compose {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }.subscribe { callback.invoke(it) }.disposeOnDestroy()
    }
}