package com.aldi.retrox.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class Github {

    @SerializedName("avatar_url")
    @Expose
    open var avatarUrl: String? = null

    @SerializedName("name")
    @Expose
    open var name: String? = null


}