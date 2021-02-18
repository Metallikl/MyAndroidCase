package com.luche.myandroidcase.model

import java.math.BigDecimal

data class Lancamento (
    val id : Int,
    val valor: BigDecimal,
    val origem: String,
    val categoria: Int,
    val mes_lancamento: Int
)