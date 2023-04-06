package com.example.ifood.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat.getExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifood.R
import com.example.ifood.adapter.AdapterProduto
import com.example.ifood.databinding.ActivityCardapioBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.model.Empresa
import com.example.ifood.model.Produto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CardapioActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCardapioBinding
    private lateinit var empresaSelecioanda : Empresa
    private  var listProduto = ArrayList<Produto>()
    private lateinit var adapterProduto : AdapterProduto
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var idEmpresa: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardapioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Cardapio"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseRef = FirebaseConfig.getInstanceDataBase()
        adapterProduto = AdapterProduto(listProduto, this)

        getDataExtras()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        with(binding.recyclerCardapio){
            layoutManager = LinearLayoutManager(this@CardapioActivity, RecyclerView.VERTICAL, false)
            adapter = adapterProduto
            setHasFixedSize(true)

            recuperarProduto()


        }
    }

    private fun getDataExtras() {
        val bundle = intent.extras
            if (bundle!= null){
                empresaSelecioanda = bundle.getSerializable("empresa") as Empresa
                binding.textNomeEmpresaCardapio.text = empresaSelecioanda.nome

                idEmpresa = empresaSelecioanda.idUser.toString()
                val url = empresaSelecioanda.urlImagem

                Picasso.get().load(url).into(binding.imageEmpresaCardapio)

            }

    }

    fun recuperarProduto(){
        val produtosRef = firebaseRef
            .child("produtos")
            .child(idEmpresa)
        produtosRef.addValueEventListener(object : ValueEventListener {
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
}