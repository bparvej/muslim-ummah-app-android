package com.example.muslimummah

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muslimummah.data.AppPreferences

class SettingsActivity : ComponentActivity() {

    private lateinit var preferences: AppPreferences

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = AppPreferences(this)

        setContent {
            val primaryEmerald = Color(0xFF075B45)

            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = primaryEmerald,
                    onPrimary = Color.White,
                    surface = Color(0xFFF7F5EE)
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF7F5EE)
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("অ্যাপ সেটিংস / Settings", fontWeight = FontWeight.Bold) },
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = primaryEmerald,
                                    titleContentColor = Color.White,
                                    navigationIconContentColor = Color.White
                                )
                            )
                        }
                    ) { padding ->
                        SettingsContent(
                            padding = padding,
                            preferences = preferences,
                            onSave = {
                                Toast.makeText(this, "সেটিংস সংরক্ষিত হয়েছে!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsContent(
    padding: PaddingValues,
    preferences: AppPreferences,
    onSave: () -> Unit
) {
    var latText by remember { mutableStateOf(preferences.baseLat.toString()) }
    var lngText by remember { mutableStateOf(preferences.baseLng.toString()) }
    var locationNameText by remember { mutableStateOf(preferences.baseLocationName) }
    var radiusText by remember { mutableStateOf(preferences.searchRadiusKm.toString()) }
    var selectedMethod by remember { mutableIntStateOf(preferences.calcMethod) }
    var selectedTone by remember { mutableStateOf(preferences.selectedAzanTone) }

    val primaryColor = Color(0xFF075B45)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "লোকেশন ও বেস কোঅর্ডিনেট",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        OutlinedTextField(
            value = locationNameText,
            onValueChange = { locationNameText = it },
            label = { Text("স্থানের নাম / Location Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = latText,
                onValueChange = { latText = it },
                label = { Text("অক্ষাংশ (Lat)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = lngText,
                onValueChange = { lngText = it },
                label = { Text("দ্রাঘিমাংশ (Lng)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "মসজিদ সার্চ ব্যাসার্ধ (Search Radius)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        OutlinedTextField(
            value = radiusText,
            onValueChange = { radiusText = it },
            label = { Text("ব্যাসার্ধ (কিমি / KM)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "নামাজের ক্যালকুলেশন মেথড",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        val methods = listOf(
            1 to "University of Islamic Sciences, Karachi (বাংলাদেশ ফাউন্ডেশন)",
            2 to "Islamic Society of North America (ISNA)",
            3 to "Muslim World League (MWL)",
            4 to "Umm Al-Qura University, Makkah",
            5 to "Egyptian General Authority of Survey"
        )

        methods.forEach { (id, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = (selectedMethod == id),
                    onClick = { selectedMethod = id },
                    colors = RadioButtonDefaults.colors(selectedColor = primaryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, fontSize = 14.sp)
            }
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "আজানের সুর (Azan Tone)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        val tones = listOf(
            "baitul-mukarrom" to "বায়তুল মোকাররম জাতীয় মসজিদ",
            "makkah" to "মসজিদুল হারাম, মক্কার আজান",
            "madinah" to "মসজিদে নববী, মদিনার আজান",
            "al-aqsa" to "আল-আকসা / বায়তুল মুকাদ্দাস"
        )

        tones.forEach { (key, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = (selectedTone == key),
                    onClick = { selectedTone = key },
                    colors = RadioButtonDefaults.colors(selectedColor = primaryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                latText.toDoubleOrNull()?.let { preferences.baseLat = it }
                lngText.toDoubleOrNull()?.let { preferences.baseLng = it }
                radiusText.toDoubleOrNull()?.let { preferences.searchRadiusKm = it }
                if (locationNameText.isNotBlank()) preferences.baseLocationName = locationNameText
                preferences.calcMethod = selectedMethod
                preferences.selectedAzanTone = selectedTone
                onSave()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(
                text = "সংরক্ষণ করুন (Save Changes)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
