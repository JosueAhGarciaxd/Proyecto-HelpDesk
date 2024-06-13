package recicledViewHelper

import Modelo.ClaseConexion
import Modelo.tickest
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import josue.a.helpdeskjosue.R
import josue.a.helpdeskjosue.activity_detalleTickect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Adaptador(private var Datos: List<tickest>) : RecyclerView.Adapter<Adaptador.MyViewHolder>() {

    // ViewHolder interno
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val imgBorrar: View = view.findViewById(R.id.imgBorrar)
        val imgEditar: View = view.findViewById(R.id.imgEditar)
    }

    // Actualizar la lista
    fun actualizarLista(nuevaLista: List<tickest>) {
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    // Actualizar la lista después de actualizar los datos
    fun actualizarListaDespuesDeActualizarDatos(uuid: String, nuevoTitulo: String, nuevaDescripcion: String, nuevoAutor: String, nuevoEmail: String, nuevoEstado: String, nuevaFechafin: String) {
        val index = Datos.indexOfFirst { it.uuid == uuid }
        if (index != -1) {
            Datos[index].titulo = nuevoTitulo
            Datos[index].descripcion = nuevaDescripcion
            Datos[index].autor = nuevoAutor
            Datos[index].email = nuevoEmail
            Datos[index].estado = nuevoEstado
            Datos[index].fechaFinalizacion = nuevaFechafin
            notifyItemChanged(index)
        }
    }

    // Eliminar registro
    fun eliminarRegistro(uuid: String, position: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)
        Datos = listaDatos.toList()
        notifyItemRemoved(position)

        // Eliminar de la base de datos
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            val deleteTiket = objConexion?.prepareStatement("DELETE FROM tb_tiket WHERE uuid = ?")!!
            deleteTiket.setString(1, uuid)
            deleteTiket.executeUpdate()

            val commit = objConexion.prepareStatement("COMMIT")!!
            commit.executeUpdate()
        }
    }

    // Actualizar producto
    fun actualizarProducto(numero: Number, titulo: String, descripcion: String, autor: String, email: String, fechaCreacion: String, estado: String, fechaFinalizacion: String, uuid: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = ClaseConexion().cadenaConexion()
            val updateProducto = objConexion?.prepareStatement("UPDATE tb_Tiket SET titulo = ?, descripcion = ?, autor = ?, email = ?, estado = ?, fechaFinalizacion = ? WHERE uuid = ?")!!
            updateProducto.setString(1, titulo)
            updateProducto.setString(2, descripcion)
            updateProducto.setString(3, autor)
            updateProducto.setString(4, email)
            updateProducto.setString(5, estado)
            updateProducto.setString(6, fechaFinalizacion)
            updateProducto.setString(7, uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("COMMIT")
            commit.executeUpdate()

            withContext(Dispatchers.Main) {
                actualizarListaDespuesDeActualizarDatos(uuid, titulo, descripcion, autor, email, estado, fechaFinalizacion)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = Datos[position]
        holder.textView.text = item.titulo

        holder.imgBorrar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("¿Estás seguro?")
            builder.setMessage("¿Deseas en verdad eliminar el registro?")
            builder.setPositiveButton("Sí") { dialog, which ->
                eliminarRegistro(item.uuid, position)
            }
            builder.setNegativeButton("No") { dialog, which -> }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        holder.imgEditar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar nombre")
            val cuadritoNuevoTitulo = EditText(context)
            cuadritoNuevoTitulo.hint = item.titulo
            builder.setView(cuadritoNuevoTitulo)
            builder.setPositiveButton("Actualizar") { dialog, which ->
                actualizarProducto(
                    item.numero,
                    cuadritoNuevoTitulo.text.toString(),
                    item.descripcion,
                    item.autor,
                    item.email,
                    item.fechaCreacion,
                    item.estado,
                    item.fechaFinalizacion,
                    item.uuid
                )
            }
            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val pantallaDetalles = Intent(context, activity_detalleTickect::class.java)
            pantallaDetalles.putExtra("uuid", item.uuid)
            pantallaDetalles.putExtra("numero", item.numero)
            pantallaDetalles.putExtra("titulo", item.titulo)
            pantallaDetalles.putExtra("descripcion", item.descripcion)
            pantallaDetalles.putExtra("autor", item.autor)
            pantallaDetalles.putExtra("email", item.email)
            pantallaDetalles.putExtra("fechaCreacion", item.fechaCreacion)
            pantallaDetalles.putExtra("estado", item.estado)
            pantallaDetalles.putExtra("fechaFinalizacion", item.fechaFinalizacion)
            context.startActivity(pantallaDetalles)
        }
    }
}