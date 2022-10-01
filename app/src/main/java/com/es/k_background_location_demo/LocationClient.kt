package com.es.k_background_location_demo

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {

    fun getLocationUpdates(inter: Long): Flow<Location>

    class LocationException( message: String): Exception()


}