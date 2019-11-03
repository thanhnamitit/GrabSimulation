package hvktqx.team.grabsimulation.di.module

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

  @Provides
  fun providePlacesClient(context: Context): PlacesClient {
    return Places.createClient(context)
  }
}
