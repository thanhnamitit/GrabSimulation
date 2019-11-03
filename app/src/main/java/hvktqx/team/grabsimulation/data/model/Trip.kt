package hvktqx.team.grabsimulation.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.android.gms.maps.model.LatLng

data class Trip(
  val origin: LatLng,
  val destination: LatLng,
  val duration: Long,
  val distance: Long,
  val startAddress: String,
  val endAddress: String,
  val polyline: String,
  val fare: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readParcelable(LatLng::class.java.classLoader)!!,
    parcel.readParcelable(LatLng::class.java.classLoader)!!,
    parcel.readLong(),
    parcel.readLong(),
    parcel.readString()!!,
    parcel.readString()!!,
    parcel.readString()!!,
    parcel.readString()!!)

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeParcelable(origin, flags)
    parcel.writeParcelable(destination, flags)
    parcel.writeLong(duration)
    parcel.writeLong(distance)
    parcel.writeString(startAddress)
    parcel.writeString(endAddress)
    parcel.writeString(polyline)
    parcel.writeString(fare)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<Trip> {
    override fun createFromParcel(parcel: Parcel): Trip {
      return Trip(parcel)
    }

    override fun newArray(size: Int): Array<Trip?> {
      return arrayOfNulls(size)
    }
  }
}