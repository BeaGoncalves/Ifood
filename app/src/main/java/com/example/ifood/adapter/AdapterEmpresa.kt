package com.example.ifood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ifood.R
import com.example.ifood.model.Empresa
import com.squareup.picasso.Picasso

class AdapterEmpresa(var listEmpresa : ArrayList<Empresa>) : Adapter<ViewHolderEmpresa>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEmpresa {
        return ViewHolderEmpresa(LayoutInflater.from(parent.context).inflate(R.layout.adapter_empresa, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderEmpresa, position: Int) {
        val empresa = listEmpresa[position]
        with(holder){
            nomeEmpresa.text = empresa.nome
            categoria.text = empresa.categoria
            tempo.text = empresa.tempo
            entrega.text = empresa.precoEntrega.toString()
            val urlImagem = empresa.urlImagem
            Picasso.get().load(urlImagem).into(holder.imagemEmpresa)
        }
    }

    override fun getItemCount(): Int {
       return listEmpresa.size
    }
}

class ViewHolderEmpresa(view : View) : ViewHolder(view) {
        var nomeEmpresa = view.findViewById<TextView>(R.id.textNomeEmpresa)
        var categoria = view.findViewById<TextView>(R.id.textCategoriaEmpresa)
        var tempo = view.findViewById<TextView>(R.id.textTempoEmpresa)
        var entrega = view.findViewById<TextView>(R.id.textEntregaEmpresa)
        var imagemEmpresa = view.findViewById<ImageView>(R.id.imageEmpresa)
}
