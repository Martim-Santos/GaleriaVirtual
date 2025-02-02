package pt.ipt.dam.trabalho_final_dam.fragmentos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import pt.ipt.dam.trabalho_final_dam.Login
import pt.ipt.dam.trabalho_final_dam.R




class fragmento3 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the logout button
        val logoutButton = view.findViewById<Button>(R.id.logout)

        // Set click listener for logout
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Clear SharedPreferences
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("email")  // Remove saved email
            apply()
        }

        // Redirect to LoginActivity
        val intent = Intent(requireActivity(), Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  // Clear back stack
        }
        startActivity(intent)

        // Finish the current Activity
        requireActivity().finish()
    }


}