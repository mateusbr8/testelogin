package com.mateus.trabalho

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mateus.trabalho.utils.ArmazenadorLocal

class AtividadeBalaoAlerta(val texto: String, val tipoDialog: Int, val email: String = "", val senha: String = ""): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val fragment = it
            var builder = AlertDialog.Builder(it)
            builder.setMessage(texto)
                .setPositiveButton("Confirmar",
                    DialogInterface.OnClickListener { dialog, id ->
                        if(email.isNotBlank() && senha.isNotBlank()) {
                            Firebase.auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {
                                val exception = it.exception
                                if(it.isSuccessful) {
                                    ArmazenadorLocal.user = it.result?.user!!
                                    val intent = Intent(fragment, AtividadePrincipal::class.java)
                                    ContextCompat.startActivity(fragment, intent, null)
                                } else if(exception != null && exception is FirebaseException) {
                                    AtividadeBalaoAlerta(exception.message!!, 1).show(fragment.getSupportFragmentManager(), "DIALOG2")
                                }
                            }
                        } else {
                            AtividadeBalaoAlerta("O email e senha nao pode ser nulo.", 1).show(fragment.getSupportFragmentManager(), "DIALOG2")
                        }
                        dialog.dismiss()

                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })

            if(tipoDialog == 1) {
                builder = AlertDialog.Builder(it)
                builder.setMessage(texto)
                    .setPositiveButton("Entendido",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.dismiss()
                        })
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}