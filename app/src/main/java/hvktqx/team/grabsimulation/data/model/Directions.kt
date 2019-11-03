package hvktqx.team.grabsimulation.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Directions(val routes: Array<Route>, val status: String) {

    class Route(
        @SerializedName("overview_polyline") val overviewPolyline: OverviewPolyline,
        val legs: Array<Leg>
    )

    class Leg(
        @SerializedName("end_address") val endAddress: String,
        @SerializedName("end_location") val endLocation: Location,
        @SerializedName("start_address") val startAddress: String,
        @SerializedName("start_location") val startLocation: Location,
        @SerializedName("distance") val distance: Value,
        @SerializedName("duration") val duration: Value
    )


    class OverviewPolyline {
        @SerializedName("points")
        @Expose
        var points: String = ""
    }

    data class Location(val lat: Double, val lng: Double) {
        fun toLatLng() = LatLng(lat, lng)
    }

    class Value {
        @SerializedName("text")
        @Expose
        var text: String = ""
        @SerializedName("value")
        @Expose
        var value: Long = 1

    }

}


