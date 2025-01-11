package pt.ipt.dam.trabalho_final_dam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dam.trabalho_final_dam.model.Utilizador
import pt.ipt.dam.trabalho_final_dam.retrofit.RetrofitInitializer

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val nameField: EditText = findViewById(R.id.etName)
        val emailField: EditText = findViewById(R.id.etEmail)
        val passwordField: EditText = findViewById(R.id.etPassword)
        val registerButton: Button = findViewById(R.id.btnRegister)
        val loginText: TextView = findViewById(R.id.tvLogin)

        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()


            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                //register(Utilizador(1email, password, name))
                val loginData = hashMapOf<String,Any>(
                    "email" to email,
                    "password" to password,
                    "nome" to name
                )

                val requestBody = hashMapOf<String, Any>(
                    "login" to loginData
                )
                register(requestBody)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginText.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
            finish() // Close Register
        }

    }

    private fun register(userMap: HashMap<String, Any>) {
        val service = RetrofitInitializer().utilizadorService()

        service.addUser(userMap).enqueue(object : Callback<Utilizador> {
            override fun onResponse(call: Call<Utilizador>, response: Response<Utilizador>) {

                if (response.isSuccessful) {
                    Toast.makeText(this@Register, "Registration successful!", Toast.LENGTH_SHORT).show()
                    // Navigate back to LoginActivity
                    val intent = Intent(this@Register, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Register, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Utilizador>, t: Throwable) {
                Toast.makeText(this@Register, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }
}
