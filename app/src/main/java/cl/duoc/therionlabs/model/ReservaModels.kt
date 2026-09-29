package cl.duoc.therionlabs.model
data class Sala(
    val codigo: String,
    val nombre: String,
    val piso: Int,
    val capacidad: Int
)
data class BloqueHorario(
    val id:String,
    val HoraInicio: String,
    val HoraFin: String,
    val disponible: Boolean,
    val asignatura: String?=null,
    val docente: String?= null
)