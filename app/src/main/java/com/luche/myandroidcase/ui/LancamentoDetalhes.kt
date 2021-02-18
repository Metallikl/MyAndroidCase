package com.luche.myandroidcase.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luche.myandroidcase.R
import com.luche.myandroidcase.databinding.ActivityLancamentoDetalhesBinding
import com.luche.myandroidcase.model.Lancamento

class LancamentoDetalhes : AppCompatActivity() {

    private lateinit var binding: ActivityLancamentoDetalhesBinding
    private val lancamento: Lancamento by lazy{
       intent?.extras?.getSerializable(MainActivity.LANCAMENTO_SAFE_PARAM) as Lancamento
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLancamentoDetalhesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        lancamento.let {
            Toast.makeText(
                this,
                "Lancamento: \n" + lancamento?.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}