package pt.ipt.dam.trabalho_final_dam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.trabalho_final_dam.model.Foto
import pt.ipt.dam.trabalho_final_dam.base64.Base64
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FotoListAdapter(private val fotos: List<Foto>, private val context: Context) :
    RecyclerView.Adapter<FotoListAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foto = fotos[position]

        // Atualizar a descrição com a data atual
        foto.Descricao = getCurrentDate()

        holder?.let {
            it.bindView(foto)
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
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
            val base64 = Base64()
            val image: ImageView = itemView.findViewById(R.id.foto_item_image) // Corrigido para ImageView
            val description: TextView = itemView.findViewById(R.id.foto_item_description)

            // Configurando a descrição
            description.text = foto.Descricao

            // Configurando a imagem
            val base64Image = foto.Foto
            if (!base64Image.isNullOrBlank()) {
                try {
                    val bitmap = base64.convertBase64toBitmap(base64Image)
                    image.setImageBitmap(bitmap) // Agora isso funcionará corretamente
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }
    }

}
}