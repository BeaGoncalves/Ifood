package com.example.ifood.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView


import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import com.example.ifood.R
import com.example.ifood.databinding.ActivityHomeBinding
import com.example.ifood.helper.FirebaseConfig
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var autenticacao : FirebaseAuth
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autenticacao = FirebaseConfig.getInstanceAuth()

        inicializarComponentes()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Ifood"
        setSupportActionBar(toolbar)


    }

    private fun inicializarComponentes() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_usuario, menu)

        val busca = menu.findItem(R.id.pesquisa)
        val searchView : SearchView = busca.actionView as SearchView
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })

        return super.onCreateOptionsMenu(menu)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sair_usuario -> deslogarUser()
            R.id.settings_usuario -> abrirConfiguracoes()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun abrirConfiguracoes() {
        startActivity(Intent(this, SettingUserActivity::class.java))
    }

    private fun deslogarUser() {
        try {
            autenticacao.signOut()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}