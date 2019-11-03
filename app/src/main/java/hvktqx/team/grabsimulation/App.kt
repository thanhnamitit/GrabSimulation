package hvktqx.team.grabsimulation

import android.content.Context
import androidx.multidex.MultiDex
import com.example.epoxytut.di.DaggerApplicationComponent
import com.google.android.libraries.places.api.Places
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        Places.initialize(this, "AIzaSyAZvMaswzgg-slgmsx1ODq-ma5NOD6m0d4")

        return DaggerApplicationComponent.builder().application(this).build().apply {
            inject(this@App)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}