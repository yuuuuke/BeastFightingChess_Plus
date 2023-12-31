package com.zhpew.plus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.zhpew.plus.Controller.state
import com.zhpew.plus.view.BoardView
import com.zhpew.plus.view.CellSize
import com.zhpew.plus.view.CellView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel

    var redPoint = 0
    var bluePoint = 0

    private fun initVM() {
        vm = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()

        Controller.onFinished = {
            if(it) redPoint++ else bluePoint ++
            Toast.makeText(this, (if (it) "红方胜利！" else "蓝方胜利！") + "比分 红 $redPoint : 蓝 $bluePoint", Toast.LENGTH_LONG).show()
            Controller.refresh()
        }

        setContent {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .width(CellSize.dp * 7)
                        .height(CellSize.dp * 9)
                ) {
                    BoardView()
                    // 横7竖9
                    Row(modifier = Modifier.fillMaxSize()) {
                        for (i in 0 until 7) {
                            Column(
                                Modifier
                                    .fillMaxHeight()
                                    .width(CellSize.dp)
                            ) {
                                for (j in 0 until 9) {
                                    CellView(state.value.Pieces[j][i], j * 7 + i)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}