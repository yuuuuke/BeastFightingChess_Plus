package com.zhpew.plus.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhpew.plus.CellBean
import com.zhpew.plus.Controller
import com.zhpew.plus.px

val paint = Paint()

@Composable
fun CellView(cell: CellBean,index:Int) {
    val isSelected = index == Controller.state.value.selectedIndex
    if(cell.level == -1){
        Box(
            modifier = Modifier
                .height(CellSize.dp)
                .width(CellSize.dp)
                .clickable {
                    Controller.clickEmpty(index)
                }
        )
        return
    }
    Box(
        modifier = Modifier
            .height(CellSize.dp)
            .width(CellSize.dp)
            .clickable {
                Controller.clickPiece(index)
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawIntoCanvas {
                paint.color = if (cell.isRed) Color.Red else Color.Blue
                paint.style = PaintingStyle.Stroke
                if(isSelected){
                    paint.strokeWidth = 25f
                    it.drawCircle(Offset(CellSize.px / 2, CellSize.px / 2), CellSize.px / 2 - 22, paint)
                }else{
                    paint.strokeWidth = 3f
                    it.drawCircle(Offset(CellSize.px / 2, CellSize.px / 2), CellSize.px / 2 - 10, paint)
                    it.drawCircle(Offset(CellSize.px / 2, CellSize.px / 2), CellSize.px / 2 - 35, paint)
                }
            }
        })
        Text(
            text = getLevel(cell),
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = if (cell.isRed) Color.Red else Color.Blue,
                fontSize = 15.sp
            ),
        )
    }
}


fun getLevel(cell: CellBean): String {
    return when (cell.level) {
        1 -> "鼠"
        2 -> "猫"
        3 -> "狗"
        4 -> "狼"
        5 -> "豹"
        6 -> "虎"
        7 -> "狮"
        8 -> "象"
        else -> "ERROR"
    }
}