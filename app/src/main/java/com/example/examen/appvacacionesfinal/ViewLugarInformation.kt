package com.example.examen.appvacacionesfinal

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.examen.appvacacionesfinal.entities.LugarVacaciones
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class ViewLugarInformation : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val lugarVacacionesContext = LugarVacaciones(
            id = intent.getIntExtra("id", -1),
            nombreLugar = intent.getStringExtra("nombreLugar") ?: "",
            imagenUri = intent.getStringExtra("imagenUri") ?: "",
            latitud = intent.getStringExtra("latitud") ?: "",
            longitud = intent.getStringExtra("longitud") ?: "",
            orden = intent.getStringExtra("orden") ?: "",
            costoAlojamiento = intent.getStringExtra("costoAlojamiento") ?: "",
            costoTraslado = intent.getStringExtra("costoTraslado") ?: "",
            comentarios = intent.getStringExtra("comentarios") ?: "",
            costoTrasladoDolar = intent.getStringExtra("costoTrasladoDolar") ?: "",
            costoAlojamientoDolar = intent.getStringExtra("costoAlojamientoDolar") ?: ""
        )

        setContent {
            DetallesLugar(lugarVacacionesContext)
        }
    }
}

@Composable
fun DetallesLugar(lugar: LugarVacaciones) {
    val contexto = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Nombre del lugar
        Text(
            text = lugar.nombreLugar,
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(5f, TextUnitType.Em),
            modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
        )

        // Imagen del lugar
        AsyncImage(
            model = lugar.imagenUri,
            contentDescription = "Imagen de ${lugar.nombreLugar}",
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        // Costos y Traslados
        Row (modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Costo x Noche", fontWeight = FontWeight.Bold)
                Text("$${lugar.costoAlojamiento}")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Traslados", fontWeight = FontWeight.Bold)
                Text("$${lugar.costoTraslado}")
            }
        }

        // Comentarios
        Text(
            text = "Comentarios:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(text = lugar.comentarios ?: "No hay comentarios disponibles.")

        // Botones de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton (onClick = {
                // Lógica para abrir cámara
            }) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Tomar foto")
            }
            IconButton(onClick = {
                val intent = Intent(contexto, CreateLugarVacaciones::class.java)
                intent.putExtra("id", lugar.id)
                intent.putExtra("nombreLugar", lugar.nombreLugar)
                intent.putExtra("imagenUri", lugar.imagenUri)
                intent.putExtra("latitud", lugar.latitud)
                intent.putExtra("longitud", lugar.longitud)
                intent.putExtra("orden", lugar.orden)
                intent.putExtra("costoAlojamiento", lugar.costoAlojamiento)
                intent.putExtra("costoTraslado", lugar.costoTraslado)
                intent.putExtra("comentarios", lugar.comentarios)
                intent.putExtra("costoTrasladoDolar", lugar.costoTrasladoDolar)
                intent.putExtra("costoAlojamientoDolar", lugar.costoAlojamientoDolar)
                contexto.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = {
                // Eliminar lugar y volver a la actividad principal
                deleteLugar(lugar, contexto)
                val intent = Intent(contexto, MainActivity::class.java)
                contexto.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        // Mapa utilizando OSMDroid
        MapaLugar(latitud = lugar.latitud.toDouble(), longitud = lugar.longitud.toDouble())
    }

}

@Composable
fun MapaLugar(latitud: Double, longitud: Double) {
    AndroidView(factory = { context ->
        // Configuración del mapa OSMDroid
        val mapView = MapView(context)
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        val mapController = mapView.controller
        mapController.setZoom(15.0)

        // Establecer el punto en la latitud y longitud
        val startPoint = GeoPoint(latitud, longitud)
        mapController.setCenter(startPoint)

        // Crear marcador para mostrar la ubicación
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.title = "Ubicación"
        mapView.overlays.add(marker)

        mapView
    }, modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(top = 20.dp)
    )
}