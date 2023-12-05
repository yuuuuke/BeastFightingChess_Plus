package com.zhpew.plus.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.zhpew.plus.MyApplication
import com.zhpew.plus.R
import com.zhpew.plus.px

val CellSize: Int by lazy {
    val display = MyApplication.instance.resources.displayMetrics
    if (display.widthPixels > display.heightPixels) {
        (display.heightPixels / 9 / display.density + 0.5).toInt()
    } else {
        (display.widthPixels / 7 / display.density + 0.5).toInt()
    }
}

@Composable
fun BoardView() {
    Canvas(
        modifier = Modifier
            .width((CellSize * 7).dp)
            .height((CellSize * 9).dp)
    ) {
        drawIntoCanvas {
            val paint = Paint()

            // 河流
            paint.color = Color(0xFF03A9F4)
            paint.style = PaintingStyle.Fill
            val leftRiver = Rect(CellSize.px, CellSize.px * 3, CellSize.px * 3, CellSize.px * 6)
            it.drawRect(leftRiver, paint)
            val rightRiver =
                Rect(CellSize.px * 4, CellSize.px * 3, CellSize.px * 6, CellSize.px * 6)
            it.drawRect(rightRiver, paint)

            paint.color = Color.Blue
            paint.style = PaintingStyle.Stroke
            val rect = Rect(0f, 0f, CellSize.px * 7, CellSize.px * 9)
            it.drawRect(rect, paint)

            // 画竖线
            for (row in 1..6) {
                it.drawLine(
                    Offset(CellSize.px * row, 0f),
                    Offset(CellSize.px * row, CellSize.px * 9),
                    paint
                )
            }
            // 横线
            for (column in 1..8) {
                it.drawLine(
                    Offset(0f, CellSize.px * column),
                    Offset(CellSize.px * 7, CellSize.px * column),
                    paint
                )
            }

            val trapImg = ImageBitmap.imageResource(
                res = MyApplication.instance.resources,
                id = R.drawable.trap
            )


            // 画陷阱和兽巢
            it.drawImage(
                trapImg,
                topLeftOffset = Offset(
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 2,
                    (CellSize.px - trapImg.height) / 2f
                ),
                paint
            )
            it.drawImage(
                trapImg,
                topLeftOffset = Offset(
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 3,
                    (CellSize.px - trapImg.height) / 2f + CellSize.px
                ),
                paint
            )
            it.drawImage(
                trapImg,
                topLeftOffset = Offset(
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 4,
                    (CellSize.px - trapImg.height) / 2f
                ),
                paint
            )


            it.drawImage(
                trapImg,
                topLeftOffset = Offset(
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 2,
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 8
                ),
                paint
            )
            it.drawImage(
                trapImg,
                topLeftOffset = Offset(
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 3,
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 7
                ),
                paint
            )
            it.drawImage(
                trapImg,
                topLeftOffset = Offset(
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 4,
                    (CellSize.px - trapImg.height) / 2f + CellSize.px * 8
                ),
                paint
            )

            val homeImg = ImageBitmap.imageResource(
                res = MyApplication.instance.resources,
                id = R.drawable.home
            )

            it.drawImage(
                homeImg,
                topLeftOffset = Offset(
                    (CellSize.px - homeImg.height) / 2f + CellSize.px * 3,
                    (CellSize.px - homeImg.height) / 2f
                ),
                paint
            )

            it.drawImage(
                homeImg,
                topLeftOffset = Offset(
                    (CellSize.px - homeImg.height) / 2f + CellSize.px * 3,
                    (CellSize.px - homeImg.height) / 2f +  CellSize.px * 8
                ),
                paint
            )
        }
    }
}