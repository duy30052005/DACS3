package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Seat(
    val seatId: String = "",
    val roomId: String = "",
    val seatStatus: String = ""  // Trạng thái ghế (đã đặt, trống, hỏng, ...)
)
