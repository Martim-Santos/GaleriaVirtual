package pt.ipt.dam.trabalho_final_dam.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import pt.ipt.dam.trabalho_final_dam.R
import pt.ipt.dam.trabalho_final_dam.FotoListAdapter
import pt.ipt.dam.trabalho_final_dam.model.Foto
import pt.ipt.dam.trabalho_final_dam.model.Fotos
import pt.ipt.dam.trabalho_final_dam.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class fragmento2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        listFoto()

    }

    private fun listFoto() {
        processFotos(RetrofitInitializer().fotoService().listFoto())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragmento2, container, false)

        // Definindo o botão de recarregar para chamar a função listFoto
        val reloadButton: Button = rootView.findViewById(R.id.btn_reload)
        reloadButton.setOnClickListener {
            listFoto()  // Recarregar as fotos
        }

        return rootView
    }


    private fun processFotos(call: Call<Fotos>) {
        call.enqueue(object : Callback<Fotos?> {
            override fun onResponse(call: Call<Fotos?>?, response: Response<Fotos?>?) {
                // Se a resposta for bem-sucedida
                response?.body()?.let {
                    val fotos: Fotos = it
                    configureList(fotos)  // Passar o objeto Fotos para o método de configuração
                }
            }

            override fun onFailure(call: Call<Fotos?>, t: Throwable) {
                // Tratar falha na requisição
                t?.message?.let { Log.e("onFailure error", it) }
            }
        })
    }

    private fun configureList(fotos: Fotos) {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.foto_list_recyclerview)

        // Verificando se há fotos, senão passando uma lista vazia
        val listaFotos: List<Foto> = fotos.fotos ?: emptyList()

        // Definindo o adaptador do RecyclerView
        recyclerView.adapter = FotoListAdapter(listaFotos, requireContext())

        // Definindo o layout manager (usei o StaggeredGridLayoutManager, mas pode ser outro)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragmento2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmento2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}