package com.example.ifood.model

import com.example.ifood.helper.FirebaseConfig

data class Pedido(
        var idUsuario : String = "",
        var idEmpresa : String = "",
        var idPedido : String = "",
        var nome : String = "",
        var endereco : String = "",
        var itens : ArrayList<ItemPedido> = arrayListOf(),
        var total : Double = 0.0,
        var status : String = "pendente",
        var metodoPagamento : Int = 0,
        var observacao : String = ""
) {
        init {
                val firebaseRef = FirebaseConfig.getInstanceDataBase()
                val pedidoRef = firebaseRef
                                .child("pedidos_usuario")
                                .child(idEmpresa)
                                .child(idUsuario)
                        idPedido = pedidoRef.push().key!!
        }
        fun salvar() {
                val firebaseRef = FirebaseConfig.getInstanceDataBase()
                val pedidoRef = firebaseRef
                        .child("pedidos_usuario")
                        .child(idEmpresa)
                        .child(idUsuario)
                pedidoRef.setValue(this)
        }

        fun confirmar() {
                val firebaseRef = FirebaseConfig.getInstanceDataBase()
                val pedidoRef = firebaseRef
                        .child("pedidos")
                        .child(idEmpresa)
                        .child(idPedido)
                pedidoRef.setValue(this)
        }


}

