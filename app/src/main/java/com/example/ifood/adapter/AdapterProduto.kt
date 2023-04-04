package com.example.ifood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ifood.R
import com.example.ifood.model.Produto
import java.lang.Double

class AdapterProduto(var produtos : List<Produto>, var context: Context) : Adapter<ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
       return ProdutoViewHolder(LayoutInflater.from(parent.context).inflate(
           R.layout.adapter_produto, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.nome.text = produto.nome
        holder.descricao.text = produto.descricao
        holder.valor.text = "R$ "+produto.preco.toString()
    }

    override fun getItemCount(): Int {
       return produtos.size
    }
}

class ProdutoViewHolder(itemView : View) : ViewHolder(itemView){


           var  nome = itemView.findViewById<TextView>(R.id.textNomeRefeicao)
            var descricao = itemView.findViewById<TextView>(R.id.textDescricaoRefeicao)
            var valor = itemView.findViewById<TextView>(R.id.textPreco)



}
