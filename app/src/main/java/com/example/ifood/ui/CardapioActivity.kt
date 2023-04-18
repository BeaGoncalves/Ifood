package com.example.ifood.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat.getExtras
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifood.R
import com.example.ifood.RecyclerItemClickListener
import com.example.ifood.adapter.AdapterProduto
import com.example.ifood.databinding.ActivityCardapioBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.example.ifood.model.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import kotlin.properties.Delegates

class CardapioActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCardapioBinding
    private lateinit var empresaSelecioanda : Empresa
    private var listProduto = ArrayList<Produto>()
    private var itensCarrinho = ArrayList<ItemPedido>()
    private lateinit var adapterProduto : AdapterProduto
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var idEmpresa: String
    private lateinit var idUser : String
    private lateinit var user : User
    private var pedidoRecuperado : Pedido? = null
    private lateinit var dialog : AlertDialog
    private var qtdItemCarrinnho by Delegates.notNull<Int>()
    private var totalCarrinho by Delegates.notNull<Double>()
    private var metodoPagamento : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardapioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Cardapio"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseRef = FirebaseConfig.getInstanceDataBase()
        idUser = UserFirebase.getIdUser()
        adapterProduto = AdapterProduto(listProduto, this)

        getDataExtras()
        setRecyclerView()
        recuperarDadosUsuario()
        setListenerRecyclerView()
    }

    private fun recuperarDadosUsuario() {
//        dialog = AlertDialog.Builder(this)
//            .setMessage("Carregando dados")
//            .setCancelable(false)
//            .show()
        val userRef = firebaseRef
            .child("usuarios")
            .child(idUser)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null){
                     user = snapshot.getValue(User::class.java)!!
                }
                recuperarPedido()
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun recuperarPedido() {
            val pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(idEmpresa)
                .child(idUser)
        pedidoRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                qtdItemCarrinnho = 0
                totalCarrinho = 0.0
                itensCarrinho = ArrayList()
                if (snapshot.value != null){
                    pedidoRecuperado = snapshot.getValue(Pedido::class.java)
                    itensCarrinho = pedidoRecuperado!!.itens

                    for (item in itensCarrinho){
                        val qtde = item.quantidade
                        val preco = item.preco

                        totalCarrinho += qtde * preco
                        qtdItemCarrinnho += qtde


                    }
                }
                binding.buttonIrParaCarrinho.text =
                    "Ir para carrinho \n Quantidade: $qtdItemCarrinnho   Total: R$ ${totalCarrinho}"


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setListenerRecyclerView(){
        binding.recyclerCardapio.addOnItemTouchListener(RecyclerItemClickListener(this, binding.recyclerCardapio, object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                confirmaQuantidade(position)
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                confirmaQuantidade(position)
            }

            override fun onLongItemClick(view: View?, position: Int) {
                TODO("Not yet implemented")
            }

        }))
    }

    private fun confirmaQuantidade(position: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_edit_text, null)
        builder.setView(dialogView)
        builder.setTitle("Quantidade")
        builder.setMessage("Digite a quantidade")

        val editTextQuantidade : EditText = dialogView.findViewById(R.id.edit_text_quantidade)
        editTextQuantidade.setText("1")
        builder.setPositiveButton("Confirmar") { dialog, which ->
                val quantidade = editTextQuantidade.text.toString()
                val produtoSelecionado = listProduto[position]
                val itemPedido = ItemPedido()
            itemPedido.idProduto = produtoSelecionado.idProdutos
                 itemPedido.nomeProduto = produtoSelecionado.nome
                 itemPedido.preco = produtoSelecionado.preco
                 itemPedido.quantidade = quantidade.toInt()

            itensCarrinho.add(itemPedido)
            if (pedidoRecuperado == null) {
                pedidoRecuperado = Pedido(idUsuario = idUser, idEmpresa = idEmpresa)
            }


            pedidoRecuperado?.nome = user.nome
            pedidoRecuperado?.endereco = user.endereco
            pedidoRecuperado?.itens = itensCarrinho
            pedidoRecuperado?.salvar()



        }
        builder.setNegativeButton("Cancelar", OnClickListener { dialog, which ->

        })
       val dialog : AlertDialog = builder.create()
        dialog.show()


    }

    private fun setRecyclerView() {
        with(binding.recyclerCardapio){
            layoutManager = LinearLayoutManager(this@CardapioActivity, RecyclerView.VERTICAL, false)
            adapter = adapterProduto
            setHasFixedSize(true)
        }
        recuperarProduto()
    }

    private fun getDataExtras() {
        val bundle = intent.extras
            if (bundle!= null){
                empresaSelecioanda = bundle.getSerializable("empresa") as Empresa
                binding.textNomeEmpresaCardapio.text = empresaSelecioanda.nome

                idEmpresa = empresaSelecioanda.idUser.toString()
                val url = empresaSelecioanda.urlImagem
                val imageEmpresa = binding.imageEmpresaCardapio

                Picasso.get().load(url).into(imageEmpresa)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_cardapio, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menuPedido -> {
                confirmarPedido()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmarPedido() {
        val buider = AlertDialog.Builder(this)

        val editTextObservacao = EditText(this)
        editTextObservacao.setText("Observação")
        buider.setView(editTextObservacao)
        buider.setTitle("Selecione um método de pagamento")
        var itens = arrayOf<CharSequence>("Dinheiro", "Cartao", "Pix")
        buider.setSingleChoiceItems(itens, 0, object : OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
               metodoPagamento = which
            }

        })

        buider.setPositiveButton("Confirmar") { _, _ ->
            val observacao = editTextObservacao.text.toString()
            pedidoRecuperado?.metodoPagamento = metodoPagamento!!
            pedidoRecuperado?.observacao = observacao
            pedidoRecuperado?.status = "Confirmado"
            pedidoRecuperado?.confirmar()
        }

        buider.setNegativeButton("Cancelar") { _, _ ->

        }
        val dialog = buider.create()
        dialog.show()

    }
}