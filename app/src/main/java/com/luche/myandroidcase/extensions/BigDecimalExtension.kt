package com.luche.myandroidcase.extensions

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

fun BigDecimal.getFormattedDecimal(): String {
    val formatoBr = DecimalFormat.getCurrencyInstance(Locale("pt", "br"))
    return formatoBr.format(this).replace("-R$","R$ -")
}