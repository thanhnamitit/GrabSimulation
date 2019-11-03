package hvktqx.team.grabsimulation.data.remote

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

class CarsProvider {
  val carsLatLng = MutableLiveData<Pair<List<LatLng>?, Long?>>()
}