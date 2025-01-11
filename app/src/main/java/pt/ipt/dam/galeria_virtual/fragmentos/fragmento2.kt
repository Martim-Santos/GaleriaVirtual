package pt.ipt.dam.galeria_virtual.fragmentos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import pt.ipt.dam.galeria_virtual.R
import pt.ipt.dam.galeria_virtual.FotoListAdapter
import pt.ipt.dam.galeria_virtual.model.Foto
import pt.ipt.dam.galeria_virtual.retrofit.RetrofitInitializer
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragmento2, container, false)
        return rootView
    }

    private fun processFotos(call: Call<List<Foto>>) {
        call.enqueue(object : Callback<List<Foto>?> {
            override fun onResponse(call: Call<List<Foto>?>?,
                                    response: Response<List<Foto>?>?) {
                response?.body()?.let {
                    val fotos: List<Foto> = it
                    configureList(fotos)
                }
            }

            override fun onFailure(call: Call<List<Foto>?>?, t: Throwable?) {
                t?.message?.let { Log.e("onFailure error", it) }
            }
        })
    }

    private fun configureList(fotos: List<Foto>) {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.foto_list_recyclerview)
        recyclerView.adapter = FotoListAdapter(fotos, requireContext())
        val layoutManager = StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL)
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