package com.luche.myandroidcase.adapter

import android.content.Context
import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luche.myandroidcase.R
import com.luche.myandroidcase.extensions.getFormattedDecimal
import com.luche.myandroidcase.model.Lancamento

class LancamentoAdapter(
    private val context: Context,
    private val lancamentos: MutableList<Lancamento>,
    private val onLancamentoClicked : (posicao: Int, lancamento: Lancamento) -> Unit
    ) : RecyclerView.Adapter<LancamentoAdapter.LancamentoVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LancamentoVH {
        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.lancamento_vh_item,
                parent,
                false
        )
        //
        return LancamentoVH(view)
    }

    override fun onBindViewHolder(holder: LancamentoVH, position: Int) {
        val lancamento = lancamentos[position] as Lancamento
        holder.bindData(lancamento,onLancamentoClicked)
    }

    override fun getItemCount(): Int {
        return lancamentos.size
    }

    fun atualizaLista(listaLancamentos: List<Lancamento>){
        this.lancamentos.clear()
        this.lancamentos.addAll(listaLancamentos)
        notifyDataSetChanged()
    }

    inner class LancamentoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOrigem: TextView = itemView.findViewById(R.id.lancamento_item_tv_origem)
        private val tvValor: TextView = itemView.findViewById(R.id.lancamento_item_tv_valor)

        fun bindData(lancemento : Lancamento, onLancamentoClicked : (mPosicao: Int, lancamento: Lancamento) -> Unit ){
            itemView.setOnClickListener {
                onLancamentoClicked(adapterPosition,lancemento)
            }
            tvValor.text = lancemento.valor.getFormattedDecimal()
            tvOrigem.text = lancemento.origem
        }
    }
}