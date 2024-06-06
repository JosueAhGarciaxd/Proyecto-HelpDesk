package Modelo

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@192.168.142.167:xe"
            val usuario = "josueahg2"
            val contrasena= "ITR2024"

            val connection = DriverManager.getConnection(url, usuario, contrasena)

            return connection
        }
        catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }
}