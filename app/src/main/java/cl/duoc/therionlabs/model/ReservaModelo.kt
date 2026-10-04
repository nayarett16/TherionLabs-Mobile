package cl.duoc.therionlabs.model
data class Sala(
    val codigo: String,
    val nombre: String,
    val tipo: String,
    val piso: Int,
    val capacidad: Int,
    val caracteristicas: List<String> = emptyList(),
    val disponibleAhora: Boolean=true
)
data class BloqueHorario(
    val id:String,
    val HoraInicio: String,
    val HoraFin: String,
    val disponible: Boolean,
    val asignatura: String?=null,
    val docente: String?= null
)

data class ReservaSemestral(
    val id: String,
    val salaCodigo: String,
    val asignatura: String,
    val docente: String,
    val fecha: String,
    val mes: String,
    val diaSemana: String,
    val numeroSemana: Int,
    val HoraInicio: String,
    val HoraFin: String
)

enum class EstadoReserva{
    PENDIENTE,
    APROBADA,
    RECHAZADA
}
data class SolicitudReserva(
    val id: String,
    val codigoSala: String,
    val nombreDocente: String,
    val asignatura: String,
    val fecha: String,
    val HoraInicio: String,
    val HoraFin: String,
    val estado: EstadoReserva= EstadoReserva.PENDIENTE,
    val motivo: String?=null
)