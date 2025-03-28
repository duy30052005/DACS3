package com.example.dacs3.data  // Nếu bạn tạo trong thư mục "data"

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    fun addUser(userId: String, name: String, email: String) {
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                println("User added successfully")
            }
            .addOnFailureListener { e ->
                println("Error adding user: $e")
            }
    }
}
