package com.example.ifood.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserFirebase {

    companion object {
        fun getIdUser() : String {
            val autenticacao = FirebaseConfig.getInstanceAuth()
            return autenticacao.currentUser!!.uid

        }

        fun getUserAtual() : FirebaseUser {
            val usuario : FirebaseAuth = FirebaseConfig.getInstanceAuth()
            return  usuario.currentUser as FirebaseUser
        }

        fun atualizaTipoUser(tipo : String) : Boolean {
            return try{
                val user = getUserAtual()
                val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build()
                user.updateProfile(profile)
                true
            } catch (e : Exception){
                e.printStackTrace()
                false
            }

        }
    }
}