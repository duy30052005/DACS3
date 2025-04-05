package com.example.dacs3.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

data class BillTicket(
    val billTicketId: String = "",
    val billId: String = "",
    val ticketId: String = "",
    val totalPrice: Double = 0.0
)
