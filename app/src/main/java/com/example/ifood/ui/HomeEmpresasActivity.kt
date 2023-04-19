package com.example.ifood.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.ifood.R
import com.example.ifood.RecyclerItemClickListener
import com.example.ifood.adapter.AdapterProduto
import com.example.ifood.databinding.ActivityHomeEmpresasBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.example.ifood.model.Produto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class HomeEmpresasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeEmpresasBinding
    private lateinit var autenticacao : FirebaseAuth
    private lateinit var adapterProduto : AdapterProduto
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var idUserLogged : String
    private var listProduto = ArrayList<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeEmpresasBinding.inflate(layoutInflater)
        setContentView(binding.root)


        autenticacao = FirebaseConfig.getInstanceAuth()
        adapterProduto = AdapterProduto(listProduto, this)
        firebaseRef = FirebaseConfig.getInstanceDataBase()
        idUserLogged = UserFirebase.getIdUser()

        with(binding.recyclerProdutos){
            layoutManager = LinearLayoutManager(this@HomeEmpresasActivity, VERTICAL, false)
            adapter = adapterProduto
            setHasFixedSize(true)

            setLongCLickRecycler(this)

            recuperarProduto()


        }


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Ifood - empresa"
        setSupportActionBar(toolbar)

    }

    private fun setLongCLickRecycler(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                Toast.makeText(this@HomeEmpresasActivity, "voce clicou", Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@HomeEmpresasActivity, "voce clicou", Toast.LENGTH_SHORT).show()
            }

            override fun onLongItemClick(view: View?, position: Int) {
                val produtoSelecionado = listProduto[position]
                produtoSelecionado.remover()
            }

        }))

    }

    fun recuperarProduto(){
        val produtosRef = firebaseRef
            .child("produtos")
            .child(idUserLogged)
            produtosRef.addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    listProduto.clear()
                    for (ds : DataSnapshot in snapshot.children){
                        val novoproduto = ds.getValue(Produto::class.java)
                        listProduto.add(novoproduto!!)

                    }
                    adapterProduto.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
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
            R.id.pedidos_menu -> startActivity(Intent(this, PedidosActivity::class.java))
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