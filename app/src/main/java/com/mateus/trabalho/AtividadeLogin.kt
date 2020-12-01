package com.mateus.trabalho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mateus.trabalho.utils.ArmazenadorLocal


class AtividadeLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mostra a tela principal
        auth = Firebase.auth
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user != null) {
            ArmazenadorLocal.user = user
            val intent = Intent(applicationContext, AtividadePrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(applicationContext, intent, null)
        }
    }

    fun aoCliclarLogin(view: View) {
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        if(editEmail.text.isBlank() || editSenha.text.isBlank()) {
            AtividadeBalaoAlerta( "O email e a senha não  pode ser nulo", 1).show(getSupportFragmentManager(), "DIALOG")
            return
        }
        auth.signInWithEmailAndPassword(editEmail.text.toString(), editSenha.text.toString()).addOnCompleteListener {
            val exception = it.exception
            if(it.isSuccessful) {
                ArmazenadorLocal.user = it.result?.user!!
                val intent = Intent(applicationContext, AtividadePrincipal::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(applicationContext, intent, null)
            } else if(exception != null && exception is FirebaseException) {
                AtividadeBalaoAlerta(exception.message!!, 1).show(getSupportFragmentManager(), "DIALOG")
            }
        }
    }

    fun aoClicarCadastrar(view: View) {
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        AtividadeBalaoAlerta("Deseja mesmo se cadastrar com o email e senha informados?", 0,editEmail.text.toString(), editSenha.text.toString()).show(getSupportFragmentManager(), "DIALOG")
    }
}
