package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Bill(
    val billId: String = "",
    val userId: String = "",
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "",
    val paymentStatus: String = "",
    val createdAt: Long = 0L,
    val paymentAt: Long? = null
)
{
    fun getUser(db: FirebaseFirestore, callback: (User?) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc -> callback(doc.toObject(User::class.java)) }
            .addOnFailureListener { callback(null) }
    }
}