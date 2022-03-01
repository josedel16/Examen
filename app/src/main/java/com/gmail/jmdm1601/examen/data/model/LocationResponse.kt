package com.gmail.jmdm1601.examen.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class LocationResponse(val date: Timestamp? = null, val location: GeoPoint? = null)
