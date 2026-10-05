package cl.duoc.therionlabs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import cl.duoc.therionlabs.model.Sala

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

enum class PantallaPrincipal { BUSCAR, CALENDARIO }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisponibilidadSalasScreen(
    onLogoutClick: () -> Unit = {},
    onReservarClick: (BloqueHorario) -> Unit = {}
) {
    var pantallaActual by remember { mutableStateOf(PantallaPrincipal.BUSCAR) }

    // Estados de Filtros
    var capacidadMinima by remember { mutableFloatStateOf(0f) }
    var filtroTipoSeleccionado by remember { mutableStateOf<String?>(null) }
    var filtroEquipamientoSeleccionado by remember { mutableStateOf<String?>(null) }

    // Lista de salas de prueba con sus características
    val listaSalas = remember {
        listOf(
            Sala("AUD-01", "Auditorio Central", "Auditorio", 1, 120, listOf("Proyector", "Climatización", "Videoconferencia"), true),
            Sala("LAB-201", "Laboratorio de Computación", "Laboratorio", 2, 20, listOf("Proyector", "Computadores", "Climatización"), true),
            Sala("LAB-202", "Laboratorio de Electrónica", "Laboratorio", 2, 16, listOf("Computadores"), true),
            Sala("MP-301", "Salón Multipropósito", "Multipropósito", 3, 60, listOf("Proyector", "Climatización", "Videoconferencia"), true),
            Sala("SC-101", "Sala de Clases 101", "Sala de clases", 1, 30, listOf("Proyector"), true)
        )
    }

    // Filtrado dinámico según Slider y Chips seleccionados
    val salasFiltradas = listaSalas.filter { sala ->
        val cumpleCapacidad = sala.capacidad >= capacidadMinima.toInt()
        val cumpleTipo = filtroTipoSeleccionado == null || sala.tipo.equals(filtroTipoSeleccionado, ignoreCase = true)
        val cumpleEquipamiento = filtroEquipamientoSeleccionado == null || sala.caracteristicas.contains(filtroEquipamientoSeleccionado)
        cumpleCapacidad && cumpleTipo && cumpleEquipamiento
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = pantallaActual == PantallaPrincipal.BUSCAR,
                    onClick = { pantallaActual = PantallaPrincipal.BUSCAR },
                    icon = { Text("🔍", fontSize = 16.sp) },
                    label = {
                        Text(
                            "BUSCAR SALA",
                            fontWeight = if (pantallaActual == PantallaPrincipal.BUSCAR) FontWeight.Bold else FontWeight.Normal,
                            color = if (pantallaActual == PantallaPrincipal.BUSCAR) DuocAzul else DuocTextoGris,
                            fontSize = 11.sp
                        )
                    }
                )
                NavigationBarItem(
                    selected = pantallaActual == PantallaPrincipal.CALENDARIO,
                    onClick = { pantallaActual = PantallaPrincipal.CALENDARIO },
                    icon = { Text("📅", fontSize = 16.sp) },
                    label = {
                        Text(
                            "CALENDARIO",
                            fontWeight = if (pantallaActual == PantallaPrincipal.CALENDARIO) FontWeight.Bold else FontWeight.Normal,
                            color = if (pantallaActual == PantallaPrincipal.CALENDARIO) DuocAzul else DuocTextoGris,
                            fontSize = 11.sp
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (pantallaActual) {
                PantallaPrincipal.BUSCAR -> {
                    Column(modifier = Modifier.fillMaxSize().background(DuocGrisFondo)) {
                        // Header Superior
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DuocAzul)
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                                Text(
                                    text = "ESPACIOS DISPONIBLES",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Consulta de recintos y equipamiento",
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

                        // Sección de Filtros Deslizables (Chips)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(vertical = 12.dp)
                        ) {
                            // Fila 1: Filtro Tipo de Espacio
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val tipos = listOf("Sala de clases", "Laboratorio", "Auditorio", "Multipropósito")
                                tipos.forEach { tipo ->
                                    val seleccionado = filtroTipoSeleccionado == tipo
                                    FilterChip(
                                        selected = seleccionado,
                                        onClick = {
                                            filtroTipoSeleccionado = if (seleccionado) null else tipo
                                        },
                                        label = { Text(tipo, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = DuocAzul,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Fila 2: Filtro Equipamiento/Características
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val equipamientos = listOf("Proyector", "Computadores", "Climatización", "Videoconferencia")
                                equipamientos.forEach { equip ->
                                    val seleccionado = filtroEquipamientoSeleccionado == equip
                                    FilterChip(
                                        selected = seleccionado,
                                        onClick = {
                                            filtroEquipamientoSeleccionado = if (seleccionado) null else equip
                                        },
                                        label = { Text(equip, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = DuocAzul,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(top = 8.dp), color = DuocGrisBorde)

                            // Slider de Capacidad
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (capacidadMinima == 0f) "Capacidad: cualquiera" else "Capacidad mínima: ${capacidadMinima.toInt()} personas",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    if (capacidadMinima > 0f || filtroTipoSeleccionado != null || filtroEquipamientoSeleccionado != null) {
                                        TextButton(onClick = {
                                            capacidadMinima = 0f
                                            filtroTipoSeleccionado = null
                                            filtroEquipamientoSeleccionado = null
                                        }) {
                                            Text("Limpiar", color = DuocAzul, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Slider(
                                    value = capacidadMinima,
                                    onValueChange = { capacidadMinima = it },
                                    valueRange = 0f..120f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = DuocAzul,
                                        activeTrackColor = DuocAzul,
                                        inactiveTrackColor = DuocGrisBorde
                                    )
                                )
                            }
                        }

                        // Lista de Recintos
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "${salasFiltradas.size} de ${listaSalas.size} espacios",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuocTextoGris
                                )
                            }

                            items(salasFiltradas) { sala ->
                                SalaTarjetaCard(sala = sala)
                            }
                        }
                    }
                }
                PantallaPrincipal.CALENDARIO -> {
                    CalendarioSalasScreen()
                }
            }
        }
    }
}

@Composable
fun SalaTarjetaCard(sala: Sala) {
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
                Column {
                    Text(
                        text = sala.codigo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuocAzul
                    )
                    Text(
                        text = sala.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DisponibleFondo
                ) {
                    Text(
                        text = "Libre ahora",
                        color = DisponibleVerde,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${sala.tipo} · Piso ${sala.piso} · 1 - ${sala.capacidad} personas",
                fontSize = 13.sp,
                color = DuocTextoGris
            )

            if (sala.caracteristicas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = sala.caracteristicas.joinToString(" · "),
                    fontSize = 12.sp,
                    color = DuocTextoGris,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}