package com.luche.myandroidcase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luche.myandroidcase.R
import com.luche.myandroidcase.extensions.getFormattedDecimal
import com.luche.myandroidcase.model.Lancamento
import java.util.*

class LancamentoAdapter(
    private val context: Context,
    private val lancamentos: MutableList<Lancamento>,
    private val onLancamentoClicked : (posicao: Int, lancamento: Lancamento) -> Unit,
    private val onFiltroAplicado: (lancamentosDoMes: List<Lancamento>) -> Unit
    ) : RecyclerView.Adapter<LancamentoAdapter.LancamentoVH>(), Filterable {

    private var lancamentosFiltrados = lancamentos
    private val lancamentoFiltro = LancamentoFiltro()

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
        val lancamento = lancamentosFiltrados[position] as Lancamento
        holder.bindData(lancamento,onLancamentoClicked)
    }

    override fun getItemCount(): Int {
        return lancamentosFiltrados.size
    }

    fun atualizaLista(listaLancamentos: List<Lancamento>){
        this.lancamentos.clear()
        this.lancamentos.addAll(listaLancamentos)
        this.lancamentosFiltrados.clear()
        this.lancamentosFiltrados = lancamentos
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

    override fun getFilter(): Filter {
        return lancamentoFiltro
    }

    inner class LancamentoFiltro : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if(constraint.isNullOrEmpty() || constraint == "0"){
                lancamentosFiltrados = lancamentos
            }else{
                val filtrados = lancamentos.filter {
                    constraint == it.mes_lancamento.toString()
                }
                //
                lancamentosFiltrados = filtrados as MutableList<Lancamento>
            }
            //
            val filterResult = FilterResults()
            filterResult.values = lancamentosFiltrados
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            lancamentosFiltrados = (results?.values ?: lancamentos) as MutableList<Lancamento>
            //
            onFiltroAplicado(lancamentosFiltrados)
            //
            notifyDataSetChanged()
        }

    }


}