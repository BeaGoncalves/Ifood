package com.example.ifood.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.SearchView


import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifood.R
import com.example.ifood.RecyclerItemClickListener
import com.example.ifood.adapter.AdapterEmpresa
import com.example.ifood.databinding.ActivityHomeBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.model.Empresa
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var autenticacao : FirebaseAuth
    private var listEmpresas = ArrayList<Empresa>()
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var adapterEmpresa: AdapterEmpresa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autenticacao = FirebaseConfig.getInstanceAuth()
        firebaseRef = FirebaseConfig.getInstanceDataBase()
        adapterEmpresa = AdapterEmpresa(listEmpresas)


        setRecycler()
        recuperarEmpresa()


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Ifood"
        setSupportActionBar(toolbar)


    }



    private fun pesquisarEmpresa(text: String?) {
        val empresaRef = firebaseRef.child("empresas")
        val query = empresaRef.orderByChild("nome")
            .startAt(text)
            .endAt(text + "\uf8ff")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listEmpresas.clear()
                for (ds : DataSnapshot in snapshot.children){
                    val novaEmpresa = ds.getValue(Empresa::class.java)
                    listEmpresas.add(novaEmpresa!!)
                }
                adapterEmpresa.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun recuperarEmpresa() {
        val empresasRef = firebaseRef
            .child("empresas")
            empresasRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listEmpresas.clear()
                    for (ds : DataSnapshot in snapshot.children){
                        val novaEmpresa = ds.getValue(Empresa::class.java)
                        listEmpresas.add(novaEmpresa!!)
                    }
                    adapterEmpresa.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun setRecycler() {
        with(binding.recyclerLojas) {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = adapterEmpresa
            setListenerRecycler(this)
        }

    }

    private fun setListenerRecycler(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
               val empresaSelecionada = listEmpresas[position]
                val intent = Intent(this@HomeActivity, CardapioActivity::class.java)
                intent.putExtra("empresa", empresaSelecionada)
                startActivity(intent)
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onLongItemClick(view: View?, position: Int) {

            }

        }))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_usuario, menu)


        val busca = menu.findItem(R.id.pesquisa)
        val searchView : SearchView = busca.actionView as SearchView
        searchView.queryHint = "Pesquisar em todo ifood"
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pesquisarEmpresa(newText)
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