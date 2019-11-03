package hvktqx.team.grabsimulation.extension

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun <T : View> View.bind(@IdRes id: Int) = lazy<T>(LazyThreadSafetyMode.NONE) {
    this@bind.findViewById(id)
}

fun <T : View> Fragment.bind(@IdRes id: Int) = lazy<T?>(LazyThreadSafetyMode.NONE) {
    this@bind.view?.findViewById(id)
}