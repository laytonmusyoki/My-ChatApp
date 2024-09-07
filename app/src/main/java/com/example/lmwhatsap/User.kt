package com.example.lmwhatsap

class User {
    var username:String?=null
    var email:String?=null
    var uid:String?=null

    constructor()

    constructor(email: String?, uid: String?, username: String?) {
        this.username = username
        this.email = email
        this.uid = uid

    }
}