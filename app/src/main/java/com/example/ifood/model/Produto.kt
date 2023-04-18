package com.example.ifood.model

import com.example.ifood.helper.FirebaseConfig

data class Produto(
    val idUser: String = "",
    var idProdutos : String = "",
    var nome : String = "",
    var descricao: String = "",
    var preco: Double = 0.0
){

    init{
        val firebaseRef = FirebaseConfig.getInstanceDataBase()
        val produtoRef = firebaseRef
            .child("produtos")
        idProdutos = produtoRef.push().key!!

    }

    fun salvar() {
        val firebaseRef = FirebaseConfig.getInstanceDataBase()
        val produtoRef = firebaseRef
            .child("produtos")
            .child(idUser!!)
            .child(idProdutos!!)
        produtoRef.setValue(this)
    }

    fun remover() {
        val firebaseRef = FirebaseConfig.getInstanceDataBase()
        val produtoRef = firebaseRef
            .child("produtos")
            .child(idUser!!)
            .child(idProdutos!!)
        produtoRef.removeValue()
    }
}