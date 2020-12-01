package com.mateus.trabalho

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mateus.trabalho.models.Nota
import com.mateus.trabalho.utils.ArmazenadorLocal
import java.util.*
import kotlin.collections.ArrayList


class AtividadePrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mostra a tela principal
        setContentView(R.layout.activity_main)


        // Mostra a tela de notas
        FirebaseFirestore.getInstance().collection(ArmazenadorLocal.user!!.uid).get().addOnCompleteListener {
            val documents = it.result!!.documents
            ArmazenadorLocal.notas = ArrayList()
            if(documents.size > 0) {
                for(document in documents) {

                    val texto: String = document.data?.get("texto") as String
                    ArmazenadorLocal.notas.add(Nota(document.reference.id, texto))
                }
            }
            val fragment: Fragment = FragmentoNota()

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, fragment.javaClass.simpleName).addToBackStack(null).commit();
        }



        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_exit_to_app)

        val wrappedDrawable =
            DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE)
        this.supportActionBar!!.setHomeAsUpIndicator(wrappedDrawable)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            Firebase.auth.signOut()
            ArmazenadorLocal.user = null
            val intent = Intent(applicationContext, AtividadeLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(applicationContext, intent, null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun aoClicarBotaoFlutuante(view: View) {
        val intent = Intent(view.context, AtividadeEditarNota::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
        ContextCompat.startActivity(view.context, intent, null)
    }
}
