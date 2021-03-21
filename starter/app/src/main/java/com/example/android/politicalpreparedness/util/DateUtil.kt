package com.example.android.politicalpreparedness.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun electionFormat(date: Date): String {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}
