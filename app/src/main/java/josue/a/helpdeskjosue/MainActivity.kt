package josue.a.helpdeskjosue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //no logre validar el de inciar sesion  :(
        //Tal vez despues lo arregle
        val btnInicio: Button = findViewById(R.id.btnIniciar)
        btnInicio.setOnClickListener {
            val intent = Intent(this, activity_Inicio::class.java)
            startActivity(intent)
        }

        val btn: Button = findViewById(R.id.btnRegistrar)
        btn.setOnClickListener {
            val intent = Intent(this, activity_registrar::class.java)
            startActivity(intent)
        }



    }

}