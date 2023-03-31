package com.example.ifood.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.ifood.R
import com.example.ifood.databinding.ActivityHomeEmpresasBinding
import com.example.ifood.helper.FirebaseConfig
import com.google.firebase.auth.FirebaseAuth

class HomeEmpresasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeEmpresasBinding
    private lateinit var autenticacao : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeEmpresasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autenticacao = FirebaseConfig.getInstanceAuth()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Ifood - empresa"
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_empresas, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sair_empresas -> deslogarUser()
            R.id.settings -> abrirConfiguracoes()
            R.id.adicionar -> abrirNovoProduto()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun abrirNovoProduto() {
       startActivity(Intent(this, NewProductActivity::class.java))
    }

    private fun abrirConfiguracoes() {
        startActivity(Intent(this, SettingEmpresaActivity::class.java))
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