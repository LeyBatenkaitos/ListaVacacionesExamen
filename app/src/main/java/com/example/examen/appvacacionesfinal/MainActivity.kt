package com.example.examen.appvacacionesfinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.examen.appvacacionesfinal.database.DatabaseConnection
import com.example.examen.appvacacionesfinal.entities.LugarVacaciones
import com.example.examen.appvacacionesfinal.viewmodels.AppVM
import com.example.examen.appvacacionesfinal.ws.CurrencyIndicator
import com.example.examen.appvacacionesfinal.ws.CurrencyService
import com.example.examen.appvacacionesfinal.ws.ServiceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    lateinit var cameraController:LifecycleCameraController

    val camaraVM: AppVM by viewModels()

    var permisosUbicacionOk:() -> Unit = {}

    val launcherPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[android.Manifest.permission.CAMERA]?:false) {
            camaraVM.onPermissesCameraOk()
        } else {
            Log.v("launcherPermissions::cameraPermissions", "Permisos denegados")
        }

        if ((it[android.Manifest.permission.ACCESS_COARSE_LOCATION]?:false) or
            (it[android.Manifest.permission.ACCESS_FINE_LOCATION]?:false)) {
            camaraVM.permisosUbicacionOk()
        } else {
            Log.v("launcherPermissions::locationPermissions", "Permisos denegados")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        cameraController = LifecycleCameraController(this)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        setContent {
            PantallaInicio()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaInicio() {

    val contexto = LocalContext.current

    var (lugares, setLugares) = remember {
        mutableStateOf(emptyList<LugarVacaciones>())
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val dao = DatabaseConnection.getInstance(contexto).LugarVacacionesDAO()
            setLugares(dao.getAll())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (lugares.isEmpty()) {
            Text(
                text = "No hay lugares para mostrar",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                items(lugares) { lugar ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp) // Espacio entre filas
                    ) {

                        AsyncImage(
                            model = lugar.imagenUri,
                            contentDescription = "Imagen de ${lugar.nombreLugar}",
                            modifier = Modifier
                                .size(100.dp) // Tamaño de la imagen
                                .padding(end = 16.dp),
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            error = painterResource(id = R.drawable.ic_launcher_foreground))

                        Column( // Usar Column para alinear los Text uno debajo del otro
                            modifier = Modifier
                                .weight(1f) // Permite que el Column ocupe el espacio disponible
                                .padding(end = 16.dp) // Espacio entre el texto y los íconos
                        ) {
                            Text(
                                text = lugar.nombreLugar,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(2.5f, TextUnitType.Em)
                            )

                            Text(
                                text = "Costo x noche: $ ${lugar.costoAlojamiento} - ${lugar.costoAlojamientoDolar}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(2.5f, TextUnitType.Em)
                            )

                            Text(
                                text = "Traslado: $ ${lugar.costoTraslado} - ${lugar.costoTrasladoDolar}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(2.5f, TextUnitType.Em)
                            )
                        }

                        // Iconos a la derecha
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            Row {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Ubicacion",
                                    Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(24.dp)
                                        .clickable {
                                            val intent = Intent(contexto, ViewLugarInformation::class.java)
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
                                        },
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar",
                                    Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(24.dp)
                                        .clickable {
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
                                        },
                                    tint = Color.Black
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Borrar",
                                    Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(24.dp)
                                        .clickable {
                                            deleteLugar(lugar, contexto)
                                            val intent = Intent(contexto, MainActivity::class.java)
                                            contexto.startActivity(intent)
                                        },
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(onClick = {
            val intent = Intent(contexto, CreateLugarVacaciones::class.java)
            contexto.startActivity(intent)
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .width(140.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar lugar",
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 12.dp))
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(text = "Agregar lugar", modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 24.dp))
        }

    }
}

fun deleteLugar(lugar: LugarVacaciones, contexto: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        Log.i("deleteLugar::", "Eliminar lugar en base de datos")

        val dao = DatabaseConnection.getInstance(contexto).LugarVacacionesDAO()
        dao.deleteLugarVacaciones(lugar)

        Log.i("deleteLugar", "Lugar eliminada correctamente!!!")
    }
}
