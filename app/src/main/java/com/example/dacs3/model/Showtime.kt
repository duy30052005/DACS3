package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Showtime(
    val showtimeId: String = "",
    val movieId: String = "",
    val roomId: String = "",
    val showDate: Long = 0L,
    val startTime: String = "",
    val endTime: String = ""
)
