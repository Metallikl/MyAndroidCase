package com.luche.myandroidcase.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.luche.myandroidcase.R
import com.luche.myandroidcase.databinding.ActivityLancamentoDetalhesBinding
import com.luche.myandroidcase.extensions.getFormattedDecimal
import com.luche.myandroidcase.model.Lancamento
import com.luche.myandroidcase.repository.CategoriaRepository
import com.luche.myandroidcase.retrofit.webclient.CategoriaClient

class LancamentoDetalhes : AppCompatActivity() {

    private lateinit var binding: ActivityLancamentoDetalhesBinding
    private val lancamento: Lancamento by lazy{
       intent?.extras?.getSerializable(MainActivity.LANCAMENTO_SAFE_PARAM) as Lancamento
    }
    private val viewModel: LancamentoDetalhesViewModel by lazy {
        ViewModelProvider(
                this,
                LancamentoDetalhesViewModel.LancamentoDetalhesViewModelFactory(CategoriaRepository(CategoriaClient()))
        ).get(LancamentoDetalhesViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLancamentoDetalhesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actLancamentoDetalhesToolbar.root)
        setaToolbarLayout()
        //
        setaValores()

    }

    private fun setaToolbarLayout() {
        binding.actLancamentoDetalhesToolbar.toolbarTvTtl.text = getString(R.string.lancamento_detalhe_act_ttl)
        binding.actLancamentoDetalhesToolbar.toolbarGdStart.visibility = View.GONE
        binding.actLancamentoDetalhesToolbar.toolbarTvBalancoVal.visibility = View.GONE
        binding.actLancamentoDetalhesToolbar.toolbarTvBalancoLbl.visibility = View.GONE
    }

    private fun setaValores() {
        lancamento.let { lancamento->
            binding.actLancamentoDetalhesEtOrigem.setText(lancamento.origem)
            binding.actLancamentoDetalhesEtValor.setText(lancamento.valor.getFormattedDecimal())
            setaListaCategoriaObserver(lancamento)
            iniciaBuscaCategoria()
        }
    }

    private fun iniciaBuscaCategoria() {
        setaUiCarregando(true)
        viewModel.buscaCategorias {
            Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setaUiCarregando(ativo: Boolean) {
        if(ativo) {
            binding.actLancamentoDetalhesEtCategoria.setText(getString(R.string.lancamento_detalhes_act_bucando_lbl))
            binding.actLancamentoDetalhesPbCategoria.visibility = View.VISIBLE
        } else{
            binding.actLancamentoDetalhesPbCategoria.visibility = View.GONE
        }
    }

    private fun setaListaCategoriaObserver(lancamento: Lancamento) {
        viewModel.listaCategoriaLiveData.observe(
                this,
                Observer { listaCategorias ->
                    val categoriaDoLancamento = listaCategorias.find { it.id == lancamento.categoria }
                    binding.actLancamentoDetalhesEtCategoria.setText(categoriaDoLancamento?.nome)
                    setaUiCarregando(false)
                }
        )
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}