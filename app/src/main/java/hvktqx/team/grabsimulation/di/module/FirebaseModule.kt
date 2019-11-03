package hvktqx.team.grabsimulation.di.module

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.Module
import dagger.Provides
import hvktqx.team.grabsimulation.data.remote.CarsProvider
import hvktqx.team.grabsimulation.data.remote.PriceProvider
import javax.inject.Singleton

@Module
class FirebaseModule {
  @Provides
  fun provideRealtimeDataBase(): FirebaseDatabase {
    return FirebaseDatabase.getInstance()
  }

  @Provides
  @Singleton
  fun providePriceProvider(database: FirebaseDatabase): PriceProvider {
    val provider = PriceProvider()
    database.getReference("price").addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        Log.i("databasechange", dataSnapshot.toString())

        val price = dataSnapshot.getValue(Long::class.java)
        price?.let { provider.price = it }
      }

      override fun onCancelled(p0: DatabaseError) {
      }
    })
    return provider
  }

  @Provides
  @Singleton
  fun provideCarsProvider(database: FirebaseDatabase): CarsProvider {
    val provider = CarsProvider()

    database.getReference("car").addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.children.map {
          try {
            it.getValue(String::class.java)?.split(",")?.let {
              LatLng(it[0].toDouble(), it[1].toDouble())
            }
          } catch (e: Exception) {
            null
          }
        }.filterNotNull()
        provider.carsLatLng.value = Pair(data, provider.carsLatLng.value?.second)
      }

      override fun onCancelled(p0: DatabaseError) {
      }
    })

    database.getReference("ratio").addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val ratio = dataSnapshot.getValue(Long::class.java)
        ratio?.let { provider.carsLatLng.value = Pair(provider.carsLatLng.value?.first, ratio) }
      }

      override fun onCancelled(p0: DatabaseError) {
      }
    })
    return provider
  }
}

//export FIREBASE_TOKEN=1//0ggY6XKM8UrRgCgYIARAAGBASNgF-L9Ir3cNNJuOY4KGxkRGY1RQNwfJ3MLRB3S9Q19uvli4X55WCoDt2yGWqnHzwHZgb9Zle5Q