package hvktqx.team.grabsimulation.screen

import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxActivity
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.screen.splash.SplashFragment

class MainActivity : BaseMvRxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        if (savedInstanceState == null) {
            val fragment = SplashFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }
    }
}
