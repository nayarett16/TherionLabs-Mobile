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
import cl.duoc.therionlabs.model.BloqueHorario

// Colores Institucionales Duoc UC
val DuocAzul = Color(0xFF002E56)
val DuocAmarillo = Color(0xFFFDBB30)
val DuocGrisFondo = Color(0xFFF5F6F8)
val DuocGrisBorde = Color(0xFFE0E0E0)
val DuocTextoGris = Color(0xFF666666)

// Estados de Disponibilidad
val DisponibleVerde = Color(0xFF2E7D32)
val DisponibleFondo = Color(0xFFE8F5E9)
val ReservadoRojo = Color(0xFFC62828)
val ReservadoFondo = Color(0xFFFFEBEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisponibilidadSalasScreen(
    onLogoutClick: () -> Unit = {},
    onReservarClick: (BloqueHorario) -> Unit = {}
) {
    var codigoSala by remember { mutableStateOf("LC25") }

    val bloques = listOf(
        BloqueHorario("1", "08:30", "10:00", disponible = true),
        BloqueHorario("2", "10:15", "11:45", disponible = false, asignatura = "Capacitación Interna", docente = "Prof. R. Morales"),
        BloqueHorario("3", "12:00", "13:00", disponible = false, asignatura = "Taller Redes", docente = "Dra. A. Fuentes"),
        BloqueHorario("4", "13:20", "14:50", disponible = true),
        BloqueHorario("5", "15:00", "16:30", disponible = true)
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuocAzul)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = "DISPONIBILIDAD DE SALAS",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Consulta de Recintos",
                        color = DuocAmarillo,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("SALIR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DuocAmarillo)
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text("🔍", fontSize = 16.sp) },
                    label = { Text("BUSCAR SALA", fontWeight = FontWeight.Bold, color = DuocAzul, fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Text("📋", fontSize = 16.sp) },
                    label = { Text("MIS RESERVAS", color = DuocTextoGris, fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Text("⚠️", fontSize = 16.sp) },
                    label = { Text("INCIDENCIAS", color = DuocTextoGris, fontSize = 11.sp) }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DuocGrisFondo)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "CÓDIGO DE SALA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuocAzul
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = codigoSala,
                    onValueChange = { codigoSala = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = DuocAzul,
                        unfocusedBorderColor = DuocGrisBorde
                    )
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Laboratorio $codigoSala",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuocAzul
                        )
                        Text(
                            text = "Piso 2 · Capacidad: 30 personas",
                            fontSize = 13.sp,
                            color = DuocTextoGris
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "BLOQUES HORARIOS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuocAzul
                )
            }

            items(bloques) { bloque ->
                BloqueHorarioCard(bloque = bloque, onReservarClick = onReservarClick)
            }
        }
    }
}

@Composable
fun BloqueHorarioCard(
    bloque: BloqueHorario,
    onReservarClick: (BloqueHorario) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕒", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${bloque.HoraInicio} - ${bloque.HoraFin}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (bloque.disponible) DisponibleFondo else ReservadoFondo
                ) {
                    Text(
                        text = if (bloque.disponible) "● Disponible" else "● Reservada",
                        color = if (bloque.disponible) DisponibleVerde else ReservadoRojo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (bloque.disponible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Espacio disponible",
                        fontSize = 13.sp,
                        color = DuocTextoGris
                    )
                    Button(
                        onClick = { onReservarClick(bloque) },
                        colors = ButtonDefaults.buttonColors(containerColor = DuocAzul),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("Reservar →", color = DuocAmarillo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text(
                    text = "Asignatura: ${bloque.asignatura ?: "N/A"}",
                    fontSize = 13.sp,
                    color = DuocTextoGris
                )
                Text(
                    text = "Relator: ${bloque.docente ?: "N/A"}",
                    fontSize = 13.sp,
                    color = DuocTextoGris
                )
            }
        }
    }
}