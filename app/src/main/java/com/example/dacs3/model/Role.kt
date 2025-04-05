package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class Role(
    val roleId: String = "",
    val roleName: String = "",
    val description: String = ""
)
