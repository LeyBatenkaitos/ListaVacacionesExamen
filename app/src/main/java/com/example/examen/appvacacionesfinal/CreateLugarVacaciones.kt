package com.example.examen.appvacacionesfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.examen.appvacacionesfinal.database.DatabaseConnection
import com.example.examen.appvacacionesfinal.entities.LugarVacaciones
import com.example.examen.appvacacionesfinal.ws.ServiceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateLugarVacaciones : ComponentActivity() {
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
            CreateLugarVacacionesUI(lugarVacacionesContext)
        }
    }
}

@Composable
fun CreateLugarVacacionesUI(lugarVacacionesContext: LugarVacaciones) {
    val contexto = LocalContext.current
    // TODO: cambiar luego por parametro idioma ingles
    val lugar = "Lugar"
    val imagenRef = "Imagen Ref."
    val latRef = "Latitud"
    val longRef = "Longitud"
    val orden = "Orden"
    val costoAlojamiento = "Costo Alojamiento"
    val costoTraslado = "Costo Traslado"
    val comentarios = "Comentarios"
    val buttonGuardar = "Guardar"

    var lugarForm by remember {
        mutableStateOf(lugarVacacionesContext.nombreLugar)
    }

    var imagenUriForm by remember {
        mutableStateOf(lugarVacacionesContext.imagenUri)
    }

    var latitudForm by remember {
        mutableStateOf(lugarVacacionesContext.latitud)
    }

    var longitudForm by remember {
        mutableStateOf(lugarVacacionesContext.longitud)
    }

    var ordenForm by remember {
        mutableStateOf(lugarVacacionesContext.orden)
    }

    var costoAlojamientoForm by remember {
        mutableStateOf(lugarVacacionesContext.costoAlojamiento)
    }

    var costoTrasladoForm by remember {
        mutableStateOf(lugarVacacionesContext.costoTraslado)
    }

    var comentariosForm by remember {
        mutableStateOf(lugarVacacionesContext.comentarios)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = lugarForm,
            onValueChange = { lugarForm = it },
            label = { Text(text = lugar) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = imagenUriForm,
            onValueChange = { imagenUriForm = it },
            label = { Text(text = imagenRef) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = latitudForm,
            onValueChange = { latitudForm = it },
            label = { Text(text = latRef) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = longitudForm,
            onValueChange = { longitudForm = it },
            label = { Text(text = longRef) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = ordenForm,
            onValueChange = { ordenForm = it },
            label = { Text(text = orden) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = costoAlojamientoForm,
            onValueChange = { costoAlojamientoForm = it },
            label = { Text(text = costoAlojamiento) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = costoTrasladoForm,
            onValueChange = { costoTrasladoForm = it },
            label = { Text(text = costoTraslado) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = comentariosForm,
            onValueChange = { comentariosForm = it },
            label = { Text(text = comentarios) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (lugarVacacionesContext.nombreLugar.isNotBlank() && lugarVacacionesContext.id != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val service = ServiceFactory.getCurrencyService()

                    Log.i("EditarLugarVacaciones::saveLugar", "Editando lugar en base de datos")
                    Log.i("EditarLugarVacaciones::saveLugar", "Id ${lugarVacacionesContext.id}")
                    val dao = DatabaseConnection.getInstance(contexto).LugarVacacionesDAO()
                    val valorCambio = service.getCurrency().dolarCurrency.valorCambio
                    dao.updateLugarVacaciones(LugarVacaciones(
                        id = lugarVacacionesContext.id,
                        nombreLugar = lugarForm,
                        imagenUri = imagenUriForm,
                        latitud = latitudForm,
                        longitud = longitudForm,
                        orden = ordenForm,
                        costoAlojamiento = costoAlojamientoForm,
                        costoTraslado = costoTrasladoForm,
                        comentarios = comentariosForm,
                        costoAlojamientoDolar = costoAlojamientoForm,
                        costoTrasladoDolar = costoTrasladoForm
                    ))

                    Log.i("EditarLugarVacaciones::saveLugar", "Lugar ingresada correctamente!!!")
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.i("CreateLugarVacaciones::saveLugar", "Creando lugar en base de datos")
                    val service = ServiceFactory.getCurrencyService()
                    Log.i("OBTAIN PRICE" , "VALOR: ${service.getCurrency().dolarCurrency.valorCambio}")
                    val valorCambio = service.getCurrency().dolarCurrency.valorCambio
                    val dao = DatabaseConnection.getInstance(contexto).LugarVacacionesDAO()
                    dao.insertLugarVacaciones(LugarVacaciones(
                        nombreLugar = lugarForm,
                        imagenUri = imagenUriForm,
                        latitud = latitudForm,
                        longitud = longitudForm,
                        orden = ordenForm,
                        costoAlojamiento = costoAlojamientoForm,
                        costoTraslado = costoTrasladoForm,
                        comentarios = comentariosForm,
                        costoAlojamientoDolar = costoAlojamientoForm,
                        costoTrasladoDolar = costoTraslado
                    ))

                    Log.i("CreateLugarVacaciones::saveLugar", "Lugar ingresada correctamente!!!")
                }
            }
            val intent = Intent(contexto, MainActivity::class.java)
            contexto.startActivity(intent)

        }) {
            Text(text = buttonGuardar)
        }
    }
}
