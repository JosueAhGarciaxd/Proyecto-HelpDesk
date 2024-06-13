package recicledViewHelper

import Modelo.ClaseConexion
import Modelo.tickest
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adaptador(private var Datos: List<tickest>) {

    fun actualizarLista(nuevaLista:List<tickest>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }

    //funcion parar actualizar el reciler view cuando actualizo los datos

    fun actualizarListaDespuesDeActualizarDatos(uuid: String,nuevoTitulo:String,nuevaDescripcion:String,nuevoAutor:String,nuevoEmail:String,nuevoEstado:String,nuevaFechafin:String){
        val index=Datos.indexOfFirst { it.uuid==uuid }
        Datos[index].titulo=nuevoTitulo
        Datos[index].descripcion=nuevaDescripcion
        Datos[index].autor=nuevoAutor
        Datos[index].email=nuevoEmail
        Datos[index].estado=nuevoEstado
        Datos[index].fechaFinalizacion=nuevaFechafin

        notifyItemChanged(index)
    }

    fun eliminarRegistro(uuid: String,position: Int){

        //quitar el elementpo de la lista
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)

        //quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO) {

            //crear un objeto e la clase conexion
            val objConexion=ClaseConexion().cadenaConexion()

            val deleteTiket = objConexion?.prepareStatement("delete tb_tiket where uuid =?")!!
            deleteTiket.setString( 1,uuid)
            deleteTiket.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()

    }

    fun actualizarProducto(numero: String ,titulo:String,descripcion:String, autor:String, email:String,fechaCreacion:String,estado:String,fechaFinalizacion:String, uuid:String){
        //crear una corrutinan
        GlobalScope.launch(Dispatchers.IO){
            //creo un objeto de la clase conexion

            val objConexion = ClaseConexion().cadenaConexion()

            //variable que contenga prepared sttement
            val updateProducto = objConexion?.prepareStatement("update tb_Tiket set titulo = ? , descripcion = ?, autor = ?,email = ? , estado = ? where uuid = ?")!!

            updateProducto.setString(1,titulo)
            updateProducto.setString(2,descripcion)
            updateProducto.setString(3,autor)
            updateProducto.setString(4,email)
            updateProducto.setString(5,estado)
            updateProducto.setString(6,fechaFinalizacion)
            updateProducto.setString(7,uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarListaDespuesDeActualizarDatos(uuid,titulo,descripcion,autor,email,estado,fechaFinalizacion )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            return ViewHolder(vista)
        }

        override fun getItemCount() = Datos.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val tiket = Datos[position]
            holder.textView.text = tiket.titulo

            val item =Datos[position]


            holder.imgBorrar.setOnClickListener {
                //craeamos una alaerta

                //invocamos  el contexto
                val context = holder.itemView.context

                //CREO LA ALERTA

                val builder = AlertDialog.Builder(context)

                //le ponemos titulo a la alerta

                builder.setTitle("¿estas seguro?")

                //ponerle mendsaje a la alerta

                builder.setMessage("Deseas en verdad eliminar el registro")

                //agrgamos los botones

                builder.setPositiveButton("si"){dialog,wich ->
                    eliminarRegistro(item.titulo,position)
                }

                builder.setNegativeButton("no"){dialog,wich ->

                }

                //cramos la alerta
                val alertDialog=builder.create()

                //mostramos la alerta

                alertDialog.show()

            }

            holder.imgEditar.setOnClickListener {
                val context=holder.itemView.context

                //creo la alerta
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Editar nombre")

                //agregar un cuadro de texto para que el usuario pueda escribir un nuevo nombre

                val cuadritoNuevoTitulo = EditText(context)
                cuadritoNuevoTitulo.setHint(item.titulo)
                builder.setView(cuadritoNuevoTitulo)

                builder.setPositiveButton("Actualizar"){
                        dialog,which->actualizarProducto(cuadritoNuevoTitulo.text.toString(),item.uuid,item.titulo,item.descripcion,item.autor,item.email,item.estado,item.fechaFinalizacion,item.uuid)
                }

                builder.setNegativeButton("cancelar"){
                        dialog,which->dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            //darle clic a la card
            holder.itemView.setOnClickListener {
                //invoco el contexto
                val context = holder.itemView.context

                //cambiamos de pantalla
                //abro pantalla detalle productos
                val pantallaDetalles=Intent(context,ActivityDetalleTiket::class.java)
                //aqui antes de abrir la nueva pantalla le abro los parametros

                pantallaDetalles.putExtra("uuid",item.uuid)
                pantallaDetalles.putExtra("numero",item.numero)
                pantallaDetalles.putExtra("titulo",item.titulo)
                pantallaDetalles.putExtra("descripcion",item.descripcion)
                pantallaDetalles.putExtra("autor",item.autor)
                pantallaDetalles.putExtra("email",item.email)
                pantallaDetalles.putExtra("fechaCreacion",item.fechaCreacion)
                pantallaDetalles.putExtra("estado",item.estado)
                pantallaDetalles.putExtra("fechaFinalizacion",item.fechaFinalizacion)
                context.startActivity(pantallaDetalles)
            }

        }
    }
}