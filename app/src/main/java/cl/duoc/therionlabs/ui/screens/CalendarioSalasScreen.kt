package cl.duoc.therionlabs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.therionlabs.model.ReservaSemestral

enum class VistaCalendario { MES, SEMANA, DIA }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioSalasScreen(
    onVolverClick: () -> Unit = {}
) {
    var vistaSeleccionada by remember { mutableStateOf(VistaCalendario.SEMANA) }
    var mesFiltro by remember { mutableStateOf("Octubre") }

    // Datos de prueba semestrales
    val reservasEjemplo = listOf(
        ReservaSemestral("1", "LC25", "Desarrollo Mobile", "Prof. R. Morales", "2026-10-12", "Octubre", "Lunes", 7, "08:30", "10:00"),
        ReservaSemestral("2", "LC25", "Arquitectura de Software", "Dra. A. Fuentes", "2026-10-13", "Octubre", "Martes", 7, "10:15", "11:45"),
        ReservaSemestral("3", "LC25", "Bases de Datos Avanzadas", "Ing. C. Silva", "2026-10-14", "Octubre", "Miércoles", 7, "12:00", "13:30"),
        ReservaSemestral("4", "LC25", "Taller de Integración", "Prof. R. Morales", "2026-10-15", "Octubre", "Jueves", 7, "15:00", "16:30")
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuocAzul)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = "CALENDARIO SEMESTRAL",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Programación de Clases y Reservas",
                        color = DuocAmarillo,
                        fontSize = 13.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DuocGrisFondo)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Selector de Vista: MES / SEMANA / DÍA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                VistaCalendario.values().forEach { vista ->
                    val seleccionado = vistaSeleccionada == vista
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (seleccionado) DuocAzul else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { vistaSeleccionada = vista }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = vista.name,
                            color = if (seleccionado) DuocAmarillo else DuocTextoGris,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de Filtro Actual según la vista seleccionada
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (vistaSeleccionada) {
                            VistaCalendario.MES -> "📅 Vista: $mesFiltro 2026"
                            VistaCalendario.SEMANA -> "🗓️ Semana 7 del Semestre"
                            VistaCalendario.DIA -> "📌 Programación Diaria"
                        },
                        fontWeight = FontWeight.Bold,
                        color = DuocAzul,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Sala: LC25",
                        color = DuocTextoGris,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de Reservas Semestrales
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reservasEjemplo) { reserva ->
                    ReservaSemestralCard(reserva = reserva)
                }
            }
        }
    }
}

@Composable
fun ReservaSemestralCard(reserva: ReservaSemestral) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna de día y fecha
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(DuocGrisFondo, shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = reserva.diaSemana.take(3).uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = DuocAzul,
                    fontSize = 12.sp
                )
                Text(
                    text = reserva.HoraInicio,
                    fontSize = 11.sp,
                    color = DuocTextoGris
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Detalles de la materia y profesor
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reserva.asignatura,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Docente: ${reserva.docente}",
                    fontSize = 12.sp,
                    color = DuocTextoGris
                )
                Text(
                    text = "Horario: ${reserva.HoraInicio} - ${reserva.HoraFin}",
                    fontSize = 11.sp,
                    color = DuocAzul,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}