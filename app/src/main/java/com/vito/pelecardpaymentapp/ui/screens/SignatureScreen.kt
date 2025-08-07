package com.vito.pelecardpaymentapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vito.pelecardpaymentapp.ui.navigation.Screen
import com.vito.pelecardpaymentapp.ui.viewmodel.PaymentViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    // stores previous strokes
    var paths by remember { mutableStateOf(listOf<Path>()) }

    // currently drawn stroke
    var currentPath by remember { mutableStateOf(Path()) }

    // size of the canvas
    var canvasSize by remember { mutableStateOf(IntSize(0, 0)) }

    Scaffold(
        topBar = { SignatureTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // drawing canvas
            SignatureDrawingBox(
                paths = paths,
                currentPath = currentPath,
                setPaths = { paths = it },
                setCurrentPath = { currentPath = it },
                setCanvasSize = { canvasSize = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // clear,cancel, submit buttons
            SignatureActions(
                viewModel = viewModel,
                navController = navController,
                paths = paths,
                currentPath = currentPath,
                canvasSize = canvasSize,
                setPaths = { paths = it },
                setCurrentPath = { currentPath = it }
            )
        }
    }
}

// top bar + title
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignatureTopBar() {
    TopAppBar(title = { Text("Draw Your Signature") })
}

// drawing area
@Composable
private fun SignatureDrawingBox(
    paths: List<Path>,
    currentPath: Path,
    setPaths: (List<Path>) -> Unit,
    setCurrentPath: (Path) -> Unit,
    setCanvasSize: (IntSize) -> Unit
) {
    var lastPoint by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(1.dp, Color.Gray)
            .background(Color.White)
            .onSizeChanged { setCanvasSize(it) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        currentPath.moveTo(offset.x, offset.y)
                        lastPoint = offset
                    },
                    onDrag = { change, _ ->
                        lastPoint?.let {
                            currentPath.quadraticTo(
                                it.x, it.y,
                                (it.x + change.position.x) / 2,
                                (it.y + change.position.y) / 2
                            )
                        }
                        lastPoint = change.position
                    },
                    onDragEnd = {
                        setPaths(paths + currentPath)
                        setCurrentPath(Path())
                        lastPoint = null
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            paths.forEach { drawPath(it, Color.Black, style = Stroke(4f)) }
            drawPath(currentPath, Color.Black, style = Stroke(4f))
        }
    }
}

// bottom row with clear, cancel and submit btns
@Composable
private fun SignatureActions(
    viewModel: PaymentViewModel,
    navController: NavController,
    paths: List<Path>,
    currentPath: Path,
    canvasSize: IntSize,
    setPaths: (List<Path>) -> Unit,
    setCurrentPath: (Path) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            onClick = {
                setPaths(emptyList())
                setCurrentPath(Path())
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Clear")
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            onClick = {
                setPaths(emptyList())
                setCurrentPath(Path())
                viewModel.clearSignature()
                navController.popBackStack()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Cancel")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                val bitmap = createBitmap(canvasSize.width, canvasSize.height)
                val canvas = android.graphics.Canvas(bitmap)

                val paint = android.graphics.Paint().apply {
                    color = Color.Black.toArgb()
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = 4f
                    isAntiAlias = true
                }

                (paths + currentPath).forEach {
                    canvas.drawPath(it.asAndroidPath(), paint)
                }

                viewModel.saveSignature(bitmap)
                viewModel.onSignatureSubmitted()
                navController.navigate(Screen.Receipt.route)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Submit")
        }
    }
}
