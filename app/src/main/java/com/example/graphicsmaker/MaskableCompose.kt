package com.example.graphicsmaker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable


@Composable
fun MaskableComposable(
    modifier: Modifier = Modifier,
    drawable: Painter,
    mask: Painter,
    blendMode: BlendMode = BlendMode.DstIn
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val context = LocalContext.current

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale = (scale * zoom).coerceIn(0.5f, 3f)
                offset += pan
            }
        }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Draw the mask
        val maskBitmap =
            mask.asBitmap(IntSize(canvasWidth.toInt(), canvasHeight.toInt()), context = context)
        val drawableBitmap =
            drawable.asBitmap(IntSize(canvasWidth.toInt(), canvasHeight.toInt()), context = context)

        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.blendMode = blendMode
            }

            // Draw the mask and blend the drawable
            canvas.drawImageRect(
                image = drawableBitmap.asImageBitmap(),
                srcSize = IntSize(drawableBitmap.width, drawableBitmap.height),
                dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
                paint = paint
            )
            canvas.drawImageRect(
                image = maskBitmap.asImageBitmap(),
                srcSize = IntSize(maskBitmap.width, maskBitmap.height),
                dstSize = IntSize(canvasWidth.toInt(), canvasHeight.toInt()),
                paint = paint
            )
        }
    }
}

fun Color.toArgb(): Int {
    return android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

fun Painter.asBitmap(size: IntSize, context: Context): Bitmap {
    val drawable = this.asBitmap(size, context = context).toDrawable(context.resources)
    return drawable.toBitmap(size.width, size.height)
}

@Composable
fun MaskColumn(position: Int) {

    val resList = listOf(
        R.drawable.shape_1,
        R.drawable.shape_2,
        R.drawable.shape_3,
        R.drawable.shape_4,
        R.drawable.shape_5,
        R.drawable.shape_6,
        R.drawable.shape_7,
        R.drawable.shape_8,
        R.drawable.shape_9,
        R.drawable.shape_10,
        R.drawable.shape_11,
        R.drawable.shape_12,
        R.drawable.shape_13,
        R.drawable.shape_14,
        R.drawable.shape_15,
        R.drawable.shape_16,
        R.drawable.shape_17,
        R.drawable.shape_18,
        R.drawable.shape_19,
        R.drawable.shape_20,
        R.drawable.shape_21,
        R.drawable.shape_22,
        R.drawable.shape_23
    )

    val list = listOf<BlendMode>(
        BlendMode.DstIn,
        BlendMode.SrcIn,
        BlendMode.Src,
        BlendMode.SrcAtop,
        BlendMode.SrcOut,
        BlendMode.SrcOver,
        BlendMode.Dst,
        BlendMode.DstAtop,
        BlendMode.DstOut,
        BlendMode.DstOver,
        BlendMode.Color,
        BlendMode.ColorBurn,
        BlendMode.ColorDodge,
        BlendMode.Difference,
        BlendMode.Overlay,
        BlendMode.Multiply,
        BlendMode.Saturation,
        BlendMode.Lighten,
        BlendMode.Softlight,
        BlendMode.Darken,
        BlendMode.Hardlight,
        BlendMode.Exclusion,
        BlendMode.Hue,
        BlendMode.Luminosity,
        BlendMode.Modulate,
        BlendMode.Screen,
        BlendMode.Plus,
        BlendMode.Xor,
        BlendMode.Clear
    )

        Box(contentAlignment = Alignment.Center) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) { mode ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        MaskableComposable(
                            drawable = painterResource(id = R.drawable.background),
                            mask = painterResource(id = resList[position]),
                            blendMode = mode,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
}


@Preview
@Composable
fun MaskableComposablePreview() {
    Box(modifier = Modifier.width(100.dp).height(100.dp)) {
        MaskableComposable(
            drawable = painterResource(id = R.drawable.btxt12),
            mask = painterResource(id = R.drawable.shape_1),
            blendMode = BlendMode.DstIn, // Choose the desired blending mode
            modifier = Modifier.fillMaxSize()
        )
    }
}
