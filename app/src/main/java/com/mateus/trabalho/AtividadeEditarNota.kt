package com.mateus.trabalho

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.mateus.trabalho.models.Nota
import com.mateus.trabalho.utils.ArmazenadorLocal
import java.util.*


class AtividadeEditarNota : AppCompatActivity() {
    var nota : Nota? = null
    lateinit var  editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_nota)

        // Pega as notas no banco de dados

        editText = findViewById<EditText>(R.id.edit_text)
        editText.text.clear()
        val extras = intent.extras
        // Se for em modo de editar essa variável terá valor
        if(extras != null) {
            // Recebe a nota enviada por parâmetro
            val item = extras["NOTA"] as Nota
            nota = item

            editText.text.append(item.texto)
        }

    }

    fun aoClicarBotaoFlutuante(view: View) {

        if(nota == null) {
            nota = Nota(FirebaseFirestore.getInstance().collection(ArmazenadorLocal.user!!.uid).document().id, editText.text.toString())
        }

        val map = mapOf<String, String>("texto" to editText.text.toString())

        FirebaseFirestore.getInstance().collection(ArmazenadorLocal.user!!.uid).document(nota!!.id).set(map).addOnCompleteListener {
            // Caso seja uma nova nota, isto é, não está armazenada. Adiciona na lista da tela inicial
            if(it.isComplete) {
                if (!ArmazenadorLocal.notas.contains(nota!!)) {
                    ArmazenadorLocal.notas.add(nota!!)
                }

                val intent = Intent(view.context, AtividadePrincipal::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                ContextCompat.startActivity(view.context, intent, null)
            }

        }
    }
}
