package com.example.water.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Info(
    @SerializedName("channel")
    var channel: Channel,
    @SerializedName("feeds")
    var feeds: List<Feeds>
)