package com.example.cinematown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cinematown.ui.theme.CinemaTownTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.util.Properties
import androidx.compose.ui.platform.LocalContext
import java.io.File

//val properties = readConfidentialProperties("CinemaTown/app/confidential.properties")

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.API_URL,
    supabaseKey = BuildConfig.API_KEY,
) {
    install(Postgrest)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaTownTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountriesList()
                }
            }
        }
    }
}

@Composable
fun CountriesList() {
    var countries by remember { mutableStateOf<List<Country>>(listOf()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            countries = supabase.from("countries")
                .select().decodeList<Country>()
        }
    }
    LazyColumn {
        items(
            countries,
            key = { country -> country.id },
        ) { country ->
            Text(
                country.name,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}