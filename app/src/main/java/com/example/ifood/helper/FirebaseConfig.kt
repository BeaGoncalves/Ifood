package com.example.ifood.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseConfig {

    companion object {
        val FIREBASE_AUTH = FirebaseAuth.getInstance()
        val FIREBASE_DATABASE = FirebaseDatabase.getInstance().reference
        private lateinit var storage : StorageReference

        fun getInstanceAuth() : FirebaseAuth {
            return FIREBASE_AUTH
        }
        fun getInstanceDataBase(): DatabaseReference {
            return FIREBASE_DATABASE
        }
        fun getInstanceStorage() : StorageReference {
            storage = FirebaseStorage.getInstance().reference
            return storage
        }
    }
}