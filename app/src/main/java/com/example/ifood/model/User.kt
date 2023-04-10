package com.example.ifood.model

import com.example.ifood.helper.FirebaseConfig

data class User(
    val idUser: String = "",
    val nome: String = "",
    val telefone: String = "",
    val endereco: String= ""
) {


    fun salvar() {
        val firebaseRef = FirebaseConfig.getInstanceDataBase()
        val userRef = firebaseRef
            .child("usuarios")
            .child(idUser)
        userRef.setValue(this)
    }
}