package pt.ipt.dam.trabalho_final_dam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.trabalho_final_dam.R
import pt.ipt.dam.trabalho_final_dam.model.Foto

class NoteListAdapter(private val fotos: List<Foto>, private val context: Context) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foto = fotos[position]
        holder?.let {
            it.bindView(foto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.foto_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fotos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(foto: Foto) {
            val image: TextView = itemView.findViewById(R.id.foto_item_image)
            val description:TextView = itemView.findViewById(R.id.foto_item_description)
            image.text = foto.Foto
            description.text = foto.Descricao
        }
    }
}