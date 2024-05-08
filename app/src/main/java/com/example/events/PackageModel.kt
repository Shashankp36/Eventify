package com.example.events

class PackageModel (
    val cmpID: String?, val packID: String?,
    val title:String?,
    val desc:String?,
    val price:String?,
    val pcount:String?){

    constructor() : this(null, null, null,null,null,null)
}
