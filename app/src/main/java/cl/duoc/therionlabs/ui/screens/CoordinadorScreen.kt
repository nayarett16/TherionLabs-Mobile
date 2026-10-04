package cl.duoc.therionlabs.ui.screens

import androidx.compose.foundation.background
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
import cl.duoc.therionlabs.model.EstadoReserva
import cl.duoc.therionlabs.model.SolicitudReserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinadorScreen() {
    // Lista mutable de solicitudes para simular la aprobación/rechazo en tiempo real
    var solicitudes by remember {
        mutableStateOf(
            listOf(
                SolicitudReserva("1", "LC25", "Prof. R. Morales", "Desarrollo Mobile", "2026-10-15", "08:30", "10:00", EstadoReserva.PENDIENTE),
                SolicitudReserva("2", "AUD01", "Dra. A. Fuentes", "Charla Ciberseguridad", "2026-10-16", "11:00", "13:00", EstadoReserva.PENDIENTE),
                SolicitudReserva("3", "LC22", "Ing. C. Silva", "Taller Linux", "2026-10-17", "14:00", "16:00", EstadoReserva.APROBADA)
            )
        )
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuocAzul)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Column {
                    Text(
                        text = "PANEL DE COORDINACIÓN",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Aprobación y Gestión de Solicitudes",
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
            Text(
                text = "SOLICITUDES DE RESERVA",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DuocAzul
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(solicitudes) { solicitud ->
                    SolicitudCard(
                        solicitud = solicitud,
                        onAprobar = { id ->
                            solicitudes = solicitudes.map {
                                if (it.id == id) it.copy(estado = EstadoReserva.APROBADA) else it
                            }
                        },
                        onRechazar = { id ->
                            solicitudes = solicitudes.map {
                                if (it.id == id) it.copy(estado = EstadoReserva.RECHAZADA) else it
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SolicitudCard(
    solicitud: SolicitudReserva,
    onAprobar: (String) -> Unit,
    onRechazar: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sala ${solicitud.codigoSala}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DuocAzul
                )

                // Tag de Estado
                val (colorFondo, colorTexto, texto) = when (solicitud.estado) {
                    EstadoReserva.PENDIENTE -> Triple(Color(0xFFFFF8E1), Color(0xFFF57F17), "● Pendiente")
                    EstadoReserva.APROBADA -> Triple(DisponibleFondo, DisponibleVerde, "● Aprobada")
                    EstadoReserva.RECHAZADA -> Triple(ReservadoFondo, ReservadoRojo, "● Rechazada")
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colorFondo
                ) {
                    Text(
                        text = texto,
                        color = colorTexto,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Docente: ${solicitud.nombreDocente}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "Asignatura: ${solicitud.asignatura}",
                fontSize = 13.sp,
                color = DuocTextoGris
            )
            Text(
                text = "Fecha y Hora: ${solicitud.fecha} (${solicitud.HoraInicio} - ${solicitud.HoraFin})",
                fontSize = 12.sp,
                color = DuocTextoGris
            )

            // Botones de acción si la solicitud está PENDIENTE
            if (solicitud.estado == EstadoReserva.PENDIENTE) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onAprobar(solicitud.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = DisponibleVerde),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Aprobar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onRechazar(solicitud.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ReservadoRojo),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Rechazar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}