package com.example.ifood.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ifood.R
import com.example.ifood.model.ItemPedido
import com.example.ifood.model.Pedido

class AdapterPedido(private val pedidos: List<Pedido>) : RecyclerView.Adapter<AdapterPedido.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pedidos, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.nome.text = pedido.nome
        holder.endereco.text = "Endereço: ${pedido.endereco}"
        holder.observacao.text = "Obs: ${pedido.observacao}"

        var itens: List<ItemPedido> = pedido.itens
        var descricaoItens = ""

        var numeroItem = 1
        var total = 0.0
        for (itemPedido in itens) {
            val qtde = itemPedido.quantidade
            val preco = itemPedido.preco
            total += qtde * preco

            val nome = itemPedido.nomeProduto
            descricaoItens += "$numeroItem) $nome / ($qtde x R$ $preco) \n"
            numeroItem++
        }
        descricaoItens += "Total: R$ $total"
        holder.itens.text = descricaoItens

        val metodoPagamento = pedido.metodoPagamento
        val pagamento = if (metodoPagamento == 0) "Dinheiro" else "Máquina cartão"
        holder.pgto.text = "pgto: $pagamento"
    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nome: TextView = itemView.findViewById(R.id.textPedidoNome)
        var endereco: TextView = itemView.findViewById(R.id.textPedidoEndereco)
        var pgto: TextView = itemView.findViewById(R.id.textPedidoPgto)
        var observacao: TextView = itemView.findViewById(R.id.textPedidoObs)
        var itens: TextView = itemView.findViewById(R.id.textPedidoItens)

    }
}