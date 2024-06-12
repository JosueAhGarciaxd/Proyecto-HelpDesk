package Modelo

data class tickest(
    val uuid:String,
    val numero:Number,
    var titulo:String,
    var descripcion:String,
    var autor : String,
    var email: String,
    val fechaCreacion : String,
    var estado :String,
    var fechaFinalizacion:String
)
