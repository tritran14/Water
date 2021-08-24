package com.example.water.data

import com.google.gson.annotations.SerializedName

data class Feeds(
    @SerializedName("field2")
    var field2: Float,
    @SerializedName("field1")
    var field1: Float
)