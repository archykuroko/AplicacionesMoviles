package ste.archykuroko.aguapp.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import kotlinx.coroutines.launch
import ste.archykuroko.aguapp.data.WaterDataStore
import ste.archykuroko.aguapp.data.dataStore

@Composable
fun AguaAppScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    val metaVasos = 8
    var vasos by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        WaterDataStore.getVasos(context).collect { vasos = it }
    }

    val progresoAnimado by animateFloatAsState(
        targetValue = (vasos.coerceAtMost(metaVasos)).toFloat() / metaVasos
    )

    val colorProgreso =
        if (vasos >= metaVasos) Color(0xFF4CAF50)
        else Color(0xFF42A5F5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        // Progreso MUCHO más delgado
        CircularProgressIndicator(
            progress = progresoAnimado,
            modifier = Modifier.size(180.dp),
            strokeWidth = 8.dp,
            indicatorColor = colorProgreso,
            trackColor = Color.DarkGray
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {

            Text(
                text = "$vasos / $metaVasos vasos",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Chip(
                onClick = {
                    scope.launch {
                        WaterDataStore.saveVasos(context, vasos + 1)
                    }
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                label = {
                    Text(
                        text = "Tomé agua 💧",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                },
                colors = ChipDefaults.primaryChipColors(
                    backgroundColor = colorProgreso
                ),
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
    }
}
