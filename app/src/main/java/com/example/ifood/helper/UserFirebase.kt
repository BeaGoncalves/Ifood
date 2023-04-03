package com.example.ifood.helper

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserFirebase {

    companion object {
        fun getIdUser() : String {
            val autenticacao = FirebaseConfig.getInstanceAuth()
            return autenticacao.currentUser!!.uid

        }


        fun updateUserPhoto(url : Uri) {
            try {
                val user : FirebaseUser = getUserAtual()
                val profileChangeRequest = UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri(url)
                    .build()

                user.updateProfile(profileChangeRequest).addOnCompleteListener {
                    if (!it.isSuccessful){
                        Log.e(ContentValues.TAG, "updatePhotoName: ${it.exception?.message}", )
                    }
                }
            } catch (exeception : Exception) {
                exeception.printStackTrace()
            }
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