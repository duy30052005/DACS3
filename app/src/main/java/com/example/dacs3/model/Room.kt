package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Room(
    val roomId: String = "",
    val name: String = "",
    val seatCount: Int = 0
)
{
    fun getSeats(db: FirebaseFirestore, callback: (List<Seat>) -> Unit) {
        db.collection("seats")
            .whereEqualTo("roomId", roomId)
            .get()
            .addOnSuccessListener { docs ->
                val seats = docs.mapNotNull { it.toObject(Seat::class.java) }
                callback(seats)
            }
    }
}
