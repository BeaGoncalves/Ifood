package com.example.ifood.model

import com.example.ifood.helper.FirebaseConfig

data class Empresa(
    val idUser : String? = "",
    val urlImagem : String? = "",
    val nome: String? = "",
    val tempo: String? = "",
    val categoria : String? = "",
    val precoEntrega : Double? = 0.0
) {

    fun salvar(){
        val firebaseRef = FirebaseConfig.getInstanceDataBase()
        val empresaRef = firebaseRef.child("empresas")
            .child(idUser!!)
        empresaRef.setValue(this)
    }


}