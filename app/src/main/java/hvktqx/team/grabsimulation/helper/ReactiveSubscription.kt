package hvktqx.team.grabsimulation.helper

import hvktqx.team.grabsimulation.functional.Wrapper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface ReactiveSubscription {
  val compositeDisposable: CompositeDisposable
  fun Disposable.disposeOnDestroy() {
    compositeDisposable.add(this)
  }

  fun <T> Observable<T>.safeSubscribe(callBack: (T) -> Unit) {
    subscribe {
      callBack.invoke(it)
    }.disposeOnDestroy()
  }

  fun <T> Observable<Wrapper<T>>.safeSubscribeWrapper(calback: (T?) -> Unit) {
    subscribe {
      calback.invoke(it.value)
    }.disposeOnDestroy()
  }

  fun disposeAll() {
    compositeDisposable.dispose()
  }
}