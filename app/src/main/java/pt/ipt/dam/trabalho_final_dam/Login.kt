package pt.ipt.dam.trabalho_final_dam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dam.trabalho_final_dam.retrofit.RetrofitInitializer

import pt.ipt.dam.trabalho_final_dam.model.Logins
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val emailField: EditText = findViewById(R.id.etEmail)
        val passwordField: EditText = findViewById(R.id.etPassword)
        val loginButton: Button = findViewById(R.id.btnLogin)
        val registerText: TextView = findViewById(R.id.tvRegister)

        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(this, "Por favor preencha todos os espaços.", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to RegisterActivity when the "Register" TextView is clicked
        registerText.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)

        }
    }

    // Faz
    private fun login(email: String, password: String) {
        val call = RetrofitInitializer().utilizadorService().listUser()
        call.enqueue(object : Callback<Logins?> {
            override fun onResponse(call: Call<Logins?>, response: Response<Logins?>) {
                if (response.isSuccessful) {
                    val users = response.body()?.logins
                    val user = users?.find { it.Email == email && it.Password == password }
                    if (user != null) {
                        Toast.makeText(this@Login, "Autenticação com sucesso!", Toast.LENGTH_SHORT).show()

                        // Salvar o e-mail do usuário no SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", email) // Salvar o e-mail
                        editor.apply()

                        // Navegar para a MainActivity
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Login, "email ou password invalida.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Logins?>, t: Throwable) {
                Toast.makeText(this@Login, "Erro de Network: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
