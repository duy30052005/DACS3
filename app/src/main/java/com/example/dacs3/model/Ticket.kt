package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Ticket(
    val ticketId: String = "",
    val userId: String = "",
    val showtimeId: String = "",
    val seatId: String = "",
    val bookingDate: Long = 0L,
    val price: Double = 0.0
)
{
    fun getShowtime(db: FirebaseFirestore, callback: (Showtime?) -> Unit) {
        db.collection("showtimes").document(showtimeId)
            .get()
            .addOnSuccessListener { doc -> callback(doc.toObject(Showtime::class.java)) }
            .addOnFailureListener { callback(null) }
    }

    fun getSeat(db: FirebaseFirestore, callback: (Seat?) -> Unit) {
        db.collection("seats").document(seatId)
            .get()
            .addOnSuccessListener { doc -> callback(doc.toObject(Seat::class.java)) }
            .addOnFailureListener { callback(null) }
    }
}
