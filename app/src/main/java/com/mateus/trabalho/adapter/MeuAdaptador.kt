package com.mateus.trabalho.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mateus.trabalho.AtividadeEditarNota


import com.mateus.trabalho.FragmentoNota.OnListFragmentInteractionListener
import com.mateus.trabalho.R
import com.mateus.trabalho.models.Nota
import com.mateus.trabalho.utils.ArmazenadorLocal

import kotlinx.android.synthetic.main.fragment_item.view.*

class MeuAdaptador(
    private val mValues: List<Nota>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MeuAdaptador.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        // Quando clicka em uma nota
        mOnClickListener = View.OnClickListener { v ->
            // Pega a nota clickada
            val item = v.tag as Nota
            mListener?.onListFragmentInteraction(item)
            // Passa os dados para a próxima tela
            val intent = Intent(v.context, AtividadeEditarNota::class.java).apply {
                putExtra("NOTA" , item)
            }
            startActivity(v.context, intent, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        // holder.mIdView.text = item.id.toString()
        holder.mContentView.text = item.toString()

        holder.botaoDeletar.setOnClickListener {
            FirebaseFirestore.getInstance().collection(ArmazenadorLocal.user!!.uid).document(item.id).delete().addOnCompleteListener {
                // remove a nota no armazenamento do aplicativo
                ArmazenadorLocal.notas.remove(item)

                notifyItemRemoved(position)
            }

        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val botaoDeletar: ImageView = mView.delete_button
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
