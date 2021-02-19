package com.luche.myandroidcase.ui

import android.content.DialogInterface
import android.content.Intent
import java.math.BigDecimal
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.luche.myandroidcase.R
import com.luche.myandroidcase.adapter.LancamentoAdapter
import com.luche.myandroidcase.databinding.ActivityMainBinding
import com.luche.myandroidcase.extensions.getFormattedDecimal
import com.luche.myandroidcase.model.Lancamento
import com.luche.myandroidcase.repository.LancamentoRepository
import com.luche.myandroidcase.retrofit.webclient.LancamentoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var listaLancamentos = mutableListOf<Lancamento>()
    private val lancamentoAdapter: LancamentoAdapter by lazy{
        LancamentoAdapter(
            this,
            listaLancamentos,
             { position, lancamento ->
                trataCliqueLancamento(position,lancamento)
            },
            { lancamentosDoMes ->
                calculaBalanco(lancamentosDoMes)
            }
        )
    }

    private val viewModel : MainActivityViewModel by lazy{
        ViewModelProvider(this,
            MainActivityViewModel.MainActivityViewModelFactory(
                LancamentoRepository(LancamentoClient())
            )
        ).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainActToolbar.root)
        setaLiveDataObserble()
        setaBuscaUI(true)
        buscaLancamentos()
        iniRecycle()
        iniSpinner()
    }

    private fun setaLiveDataObserble() {
        viewModel.listaLancamentosLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                it?.let {
                    lancamentoAdapter.atualizaLista(it)
                    aplicaFiltroMesAtual()
                }
                setaBuscaUI(false)
                setaBtnBusca(false)
            }
        )
    }

    private fun iniSpinner() {
        val listaMes = resources.getStringArray(R.array.mounths)
        val mAdapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listaMes)
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mainActSpMes.adapter = mAdapter
        binding.mainActSpMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.atualizaIdxSpinner(position)
                lancamentoAdapter?.filter.filter("${position + 1}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                lancamentoAdapter?.filter.filter("")
            }
        }
    }

    private fun setaBuscaUI(buscaAtiva: Boolean) {
        if(buscaAtiva){
            binding.mainActPbLoading.visibility =  View.VISIBLE
        }else{
            binding.mainActPbLoading.visibility =  View.GONE
            setaMensagemNenhumDadoEncontrado(false)
        }
    }

    //TODO VIEWMODEL
    private fun buscaLancamentos() {
        viewModel.buscaLancamentos {
            setaBuscaUI(false)
            setaBtnBusca(true)
            Toast.makeText(
                this@MainActivity,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
        //
        //setaBuscaUI(false)
    }

    private fun setaBtnBusca(exibe: Boolean) {
        binding.mainActBtnBusca.visibility =
            if(exibe){
                binding.mainActBtnBusca.setOnClickListener(View.OnClickListener {
                    setaBuscaUI(true)
                    buscaLancamentos()
                })
                //
                View.VISIBLE
            }else{
                View.GONE
            }
    }

    private fun aplicaFiltroMesAtual() {
        val posicao = viewModel.spinnerIdx
        //
        binding.mainActSpMes.setSelection(posicao)
    }

    private fun iniRecycle() {
        /*val divisor = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.mainActRvLancamentos.addItemDecoration(divisor)*/
        binding.mainActRvLancamentos.adapter = lancamentoAdapter
        binding.mainActRvLancamentos.layoutManager = LinearLayoutManager(this)
    }

    private fun trataCliqueLancamento(position: Int, lancamento: Lancamento) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog
            .setTitle(getString(R.string.main_act_dialog_confirma_detalhe_ttl))
            .setMessage(getString(R.string.main_act_dialog_confirma_detalhe_msg))
            .setNegativeButton(getString(R.string.generic_btn_nao),null)
            .setPositiveButton(getString(R.string.generic_btn_sim))
            { dialogInterface: DialogInterface, i: Int ->
                chamaActLancamentoDetalhes(lancamento)
            }
        //
        alertDialog.create().show()

    }

    private fun calculaBalanco(lancamentosDoMes: List<Lancamento>) {
        var somaBalanco = viewModel.calculaBalanco(lancamentosDoMes)
                if(somaBalanco > BigDecimal.ZERO) {
                    setaMensagemNenhumDadoEncontrado(false)
                }else{
                    setaMensagemNenhumDadoEncontrado(true)
                }
                //
        binding.mainActToolbar.toolbarTvBalancoVal.text = somaBalanco.getFormattedDecimal()
    }

    private fun setaMensagemNenhumDadoEncontrado(exibeMsg: Boolean) {
        binding.mainActTvSemResultados.visibility =
                if(exibeMsg){
                    View.VISIBLE
                }else{
                    View.GONE
                }
    }

    private fun chamaActLancamentoDetalhes(lancamento: Lancamento) {
        val intent = Intent(this,LancamentoDetalhes::class.java)
        val bundle = Bundle()
        bundle.putSerializable(LANCAMENTO_SAFE_PARAM,lancamento)
        intent.putExtras(bundle)
        startActivity(intent)
        //Não matarei a tela incial, pois se não geraria uma nova chamada a API ou seria necessario
        //persistir ou trafegar a lista via bundle.
    }


    companion object {
        const val LANCAMENTO_SAFE_PARAM = "BUNDLE_PARAM_LANCAMENTO"
    }
}