package com.example.ifood.model

data class EnderecoUsuario(
    private var logradouro : String = "",
    private var numero : String = "",
    private var bairro : String = "",
    private var cep : String = "",
    private var cidade : String = "",
    private var estado: String = ""

) {
}