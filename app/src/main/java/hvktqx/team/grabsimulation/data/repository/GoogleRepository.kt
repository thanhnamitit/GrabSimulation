package hvktqx.team.grabsimulation.data.repository

import hvktqx.team.grabsimulation.data.model.Directions
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleRepository {
    @GET("maps/api/directions/json?key=x")
    fun getRoutes(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    )
            : Observable<Directions>
}
