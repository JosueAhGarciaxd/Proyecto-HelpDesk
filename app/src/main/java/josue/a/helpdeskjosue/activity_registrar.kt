package josue.a.helpdeskjosue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_registrar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //1- Mandar a llamar a todos los elemento de la vista

        val txtCorreoR =findViewById<EditText>(R.id.txtCorreoRegistrar)
        val txtNombreR = findViewById<EditText>(R.id.txtNombreRegistrar)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasenaRegistrar)
        val btnRegistrar: Button = findViewById(R.id.btnInicioR)


        btnRegistrar.setOnClickListener {
            val correo = txtCorreoR.text.toString()
            val contrasena = txtContrasena.text.toString()
            val nombre = txtNombreR.text.toString()

            if (correo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Campos vacíos", Toast.LENGTH_LONG).show()
            } else {
                // Validar Correo Electrónico
                if (!correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())) {
                    Toast.makeText(this, "Ingrese correo válido", Toast.LENGTH_LONG).show()
                } else if (contrasena.length < 6 || contrasena.matches("[0-9]+".toRegex())) {
                    Toast.makeText(this, "La clave debe contener más de 6 dígitos y no solo números", Toast.LENGTH_LONG).show()
                } else {
                    // Todos los campos son válidos, registra al usuario y redirige
                    val intent = Intent(this, activity_Inicio::class.java)
                    startActivity(intent)
                }
            }
        }

    }
}