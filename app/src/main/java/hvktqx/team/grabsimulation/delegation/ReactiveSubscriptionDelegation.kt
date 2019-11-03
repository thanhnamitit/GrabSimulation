package hvktqx.team.grabsimulation.delegation

import hvktqx.team.grabsimulation.helper.ReactiveSubscription
import io.reactivex.disposables.CompositeDisposable

class ReactiveSubscriptionDelegation : ReactiveSubscription {
  override val compositeDisposable: CompositeDisposable
    get() = CompositeDisposable()
}