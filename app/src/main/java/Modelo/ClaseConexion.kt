package Modelo

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:192.168.56.1:xe"
            val usuario = "System"
            val contrasena= "desarrollo"

            val connection = DriverManager.getConnection(url, usuario, contrasena)

            return connection
        }
        catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }
}