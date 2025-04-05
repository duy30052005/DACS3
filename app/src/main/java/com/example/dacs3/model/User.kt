package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class User(
    val userId: String = "",
    val roleId: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = ""
)
{
    fun getRole(db: FirebaseFirestore, callback: (Role?) -> Unit) {
        db.collection("roles").document(roleId)
            .get()
            .addOnSuccessListener { doc ->
                callback(doc.toObject(Role::class.java))
            }
            .addOnFailureListener { callback(null) }
    }
}