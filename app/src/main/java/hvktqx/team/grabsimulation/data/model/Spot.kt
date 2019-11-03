package hvktqx.team.grabsimulation.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.android.gms.maps.model.LatLng

data class Spot(val id: String, val latLng: LatLng?, val address: String) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString()!!,
    parcel.readParcelable(LatLng::class.java.classLoader),
    parcel.readString()!!)

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeParcelable(latLng, flags)
    parcel.writeString(address)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<Spot> {
    override fun createFromParcel(parcel: Parcel): Spot {
      return Spot(parcel)
    }

    override fun newArray(size: Int): Array<Spot?> {
      return arrayOfNulls(size)
    }
  }
}