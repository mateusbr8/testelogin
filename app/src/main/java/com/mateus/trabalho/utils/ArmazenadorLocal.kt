package com.mateus.trabalho.utils

import com.google.firebase.auth.FirebaseUser
import com.mateus.trabalho.models.Nota
import java.util.ArrayList

class ArmazenadorLocal {
    companion object {
        var notas: MutableList<Nota> = ArrayList()
        var user: FirebaseUser? = null
    }
}