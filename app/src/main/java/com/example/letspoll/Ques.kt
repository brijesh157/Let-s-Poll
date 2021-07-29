package com.example.letspoll

import java.util.*
import kotlin.collections.ArrayList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class Ques (
    var question : String ="",
    var item1: ArrayList<String> = arrayListOf<String>(),
    var current : String=""
)
