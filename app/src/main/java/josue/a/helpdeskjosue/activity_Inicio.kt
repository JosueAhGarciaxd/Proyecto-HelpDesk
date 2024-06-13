package josue.a.helpdeskjosue

import Modelo.ClaseConexion
import Modelo.tickest
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNumeroTickest = findViewById<TextView>(R.id.txtNumeroTikects)
        val txtTitulo = findViewById<TextView>(R.id.txtTitulo)
        val txtDescripccion = findViewById<TextView>(R.id.txtDescripccion)
        val txtAutor = findViewById<TextView>(R.id.txtAutor)
        val txtCorreo = findViewById<TextView>(R.id.txtCorreo)
        val txtFechaCreacion = findViewById<TextView>(R.id.txtFechaC)
        val txtEstado = findViewById<TextView>(R.id.txtEstado)
        val txtFechaFinalizacion = findViewById<TextView>(R.id.txtFechaF)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarInicio)
        val rcvRegistro=findViewById<RecyclerView>(R.id.rcvregistro)

        fun obtenerDatos(): List<tickest> {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from Tikets")!!

            val tiketsList = mutableListOf<tickest>()
            while (resultSet.next()) {
                val uuid = resultSet.getString("uuid")
                val numero = resultSet.getInt("numero")
                val titulo = resultSet.getString("titulo")
                val descripcion = resultSet.getString("descripcion")
                val autor = resultSet.getString("autor")
                val email = resultSet.getString("email")
                val fechaCreacion = resultSet.getString("fechaCreacion")
                val estado = resultSet.getString("estado")
                val fechaFinalizacion = resultSet.getString("fechaFinalizacion")
                val tiket = tickest(uuid, numero, titulo, descripcion, autor, email, fechaCreacion, estado, fechaFinalizacion)
                tiketsList.add(tiket)
            }
            return tiketsList
        }



    }
}