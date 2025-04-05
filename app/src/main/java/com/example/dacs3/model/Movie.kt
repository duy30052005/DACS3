package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Movie(
    val movieId: String = "",
    val title: String = "",
    val description: String = "",
    val poster: String = "",
    val duration: Int = 0,  // thời lượng phim (phút)
    val releaseDate: Long = 0L,
    val rating: Double = 0.0
)
{
    fun getTypes(db: FirebaseFirestore, callback: (List<Type>) -> Unit) {
        db.collection("type_movies")
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnSuccessListener { result ->
                val typeIds = result.documents.mapNotNull { it["typeId"] as? String }
                db.collection("types")
                    .whereIn("typeId", typeIds)
                    .get()
                    .addOnSuccessListener { docs ->
                        val types = docs.mapNotNull { it.toObject(Type::class.java) }
                        callback(types)
                    }
            }
    }
}