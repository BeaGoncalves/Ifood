package com.example.ifood.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.ifood.R
import com.example.ifood.RecyclerItemClickListener
import com.example.ifood.adapter.AdapterPedido
import com.example.ifood.databinding.ActivityPedidosBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.example.ifood.model.Pedido
import com.google.firebase.database.*

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var adapterPedido : AdapterPedido
    private  var listPedidos = ArrayList<Pedido>()
  //  private lateinit var alertDialog: AlertDialog
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var idEmpresa: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Pedidos"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseRef = FirebaseConfig.getInstanceDataBase()
        idEmpresa = UserFirebase.getIdUser()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        with(binding.recyclerPedidos){
            layoutManager = LinearLayoutManager(this@PedidosActivity,
                RecyclerView.VERTICAL, false)
            adapterPedido = AdapterPedido(listPedidos)
            adapter = adapterPedido
            setHasFixedSize(true)
            recuperarPedidos()
        }
        setListenerRecyclerView()
    }

    private fun setListenerRecyclerView() {
        binding.recyclerPedidos.addOnItemTouchListener(
            RecyclerItemClickListener(this, binding.recyclerPedidos, object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {

            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onLongItemClick(view: View?, position: Int) {
                val pedido = listPedidos[position]
                pedido.status = "Finalizado"
                pedido.atualizarStatus()
            }

        }))
    }

    private fun recuperarPedidos() {
//            alertDialog = AlertDialog.Builder(this)
//            .setMessage("Carregando dados")
//            .setCancelable(false)
//            .show()

        val pedidoRef = firebaseRef
            .child("pedidos")
            .child(idEmpresa)
        val pedidoPesquisa : Query = pedidoRef.orderByChild("status")
            .equalTo("Confirmado")
        pedidoPesquisa.addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                listPedidos.clear()
                if (snapshot.value != null){
                    for (ds: DataSnapshot in snapshot.children){
                        val pedido = ds.getValue(Pedido::class.java)
                        if (pedido!= null){
                        listPedidos.add(pedido)
                        }else {
                            Toast.makeText(this@PedidosActivity, "algo errado aconteceu", Toast.LENGTH_SHORT).show()
                        }
                        Log.d(TAG, "onDataChange: entrou aqui")
                    }
                    adapterPedido.notifyDataSetChanged()
                 //   alertDialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}