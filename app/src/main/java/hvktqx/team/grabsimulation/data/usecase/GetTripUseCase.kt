package hvktqx.team.grabsimulation.data.usecase

import hvktqx.team.grabsimulation.data.model.Trip
import hvktqx.team.grabsimulation.data.remote.PriceProvider
import hvktqx.team.grabsimulation.data.repository.GoogleRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetTripUseCase @Inject constructor(
  private val repository: GoogleRepository,
  private val priceProvider: PriceProvider
) {
  operator fun invoke(startId: String, endId: String): Observable<Trip> {
    return repository.getRoutes("place_id:$startId", "place_id:$endId").map {
      it.routes.first().let { route ->
        route.legs.first().let { leg ->
          Trip(
            leg.startLocation.toLatLng(),
            leg.endLocation.toLatLng(),
            leg.duration.value,
            leg.distance.value,
            leg.startAddress,
            leg.endAddress,
            route.overviewPolyline.points,
            "${(leg.distance.value / 1000f * priceProvider.price / 1000).toInt()}K"
          )
        }
      }
    }
  }
}