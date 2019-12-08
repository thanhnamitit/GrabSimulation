package hvktqx.team.grabsimulation.data.repository

import hvktqx.team.grabsimulation.data.model.Routes
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleRepository {
    @GET("maps/api/directions/json?key=xx")
    fun getRoutes(
        @Query("startLatLng") origin: String,
        @Query("endLatLng") destination: String
    )
            : Observable<Routes>
}
