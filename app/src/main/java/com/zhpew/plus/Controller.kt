package com.zhpew.plus

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

object Controller {

    val RedTrap = intArrayOf(2, 4, 10)
    val BlueTrap = intArrayOf(53, 59, 61)
    val RedHome = intArrayOf(3)
    val BlueHome = intArrayOf(60)
    val River = intArrayOf(22, 23, 25, 26, 29, 30, 32, 33, 36, 37, 39, 40)

    val state = mutableStateOf(UiState(initCell()))

    private fun initCell(): SnapshotStateList<SnapshotStateList<CellBean>> {
        val result = SnapshotStateList<SnapshotStateList<CellBean>>()
        for (i in 0 until 9) {
            val list = SnapshotStateList<CellBean>()
            for (j in 0 until 7) {
                val cellBean = CellBean()
                list.add(cellBean)
            }
            result.add(list)
        }
        result[0][0].isRed = true
        result[0][0].level = 7
        result[0][6].isRed = true
        result[0][6].level = 6
        result[1][1].isRed = true
        result[1][1].level = 3
        result[1][5].isRed = true
        result[1][5].level = 2
        result[2][0].isRed = true
        result[2][0].level = 1
        result[2][2].isRed = true
        result[2][2].level = 5
        result[2][4].isRed = true
        result[2][4].level = 4
        result[2][6].isRed = true
        result[2][6].level = 8
        result[6][0].isRed = false
        result[6][0].level = 8
        result[6][2].isRed = false
        result[6][2].level = 4
        result[6][4].isRed = false
        result[6][4].level = 5
        result[6][6].isRed = false
        result[6][6].level = 1
        result[7][1].isRed = false
        result[7][1].level = 2
        result[7][5].isRed = false
        result[7][5].level = 3
        result[8][0].isRed = false
        result[8][0].level = 6
        result[8][6].isRed = false
        result[8][6].level = 7
        return result
    }

    //点击空地
    fun clickEmpty(index: Int) {
        val lastIndex = state.value.selectedIndex
        val lastCell = getPiece(state.value.selectedIndex)

        if (lastIndex == -1) {
            //无效点击
            return
        }

        if ((lastCell.isRed && index in RedHome) ||
            (!lastCell.isRed && index in BlueHome)
        ) {
            // 自家不能进自己屋
            return
        }

        if (lastCell.level == 6 || lastCell.level == 5) {
            //豹子老虎可以跨河，特殊处理

        } else {
            if (lastIndex % 7 == index % 7 + 1 || lastIndex % 7 == index % 7 - 1  //左右走
                || lastIndex / 7 == index / 7 + 1 || lastIndex / 7 == index / 7 - 1 //往上下走
            ) {
                if (index in River) {
                    // 是老鼠,可以在河里跑,其它动物均为无效操作
                    if (lastCell.level == 1) {
                        moveOrEat(lastIndex, index)
                    }
                } else {
                    // 不是河，直接走
                    moveOrEat(lastIndex, index)
                }
            }
        }
    }

    //点击棋子
    fun clickPiece(index: Int) {
        val lastIndex = state.value.selectedIndex
        val lastCell = getPiece(state.value.selectedIndex)
        val targetCell = getPiece(index)
        if (lastIndex == -1 || state.value.isRedTurn == targetCell.isRed) {
            //选中或者切换选中
            state.value = state.value.copy(selectedIndex = index)
            return
        } else {
            //吃别人的棋子
            if (lastCell.level == 6 || lastCell.level == 5) {
                // 豹子老虎可以跨河吃，特殊处理
            } else {
                if (lastIndex % 7 == index % 7 + 1 || lastIndex % 7 == index % 7 - 1  //左右走
                    || lastIndex / 7 == index / 7 + 1 || lastIndex / 7 == index / 7 - 1 //往上下走
                ) {
                    if ((index in BlueTrap && state.value.isRedTurn == targetCell.isRed)
                        || (index in RedTrap && state.value.isRedTurn != targetCell.isRed)
                    ) {
                        // 敌方棋子在我方陷阱中，可以吃掉，无视等级，直接吃掉
                        moveOrEat(lastIndex,index)
                    }else{
//                        if(lastCell.level>)
                    }
                }
            }
        }


    }

    // 根据index 获取 cell
    fun getPiece(index: Int): CellBean {
        if (index == -1) {
            return CellBean()
        }
        return state.value.Pieces[index / 7][index % 7]
    }

    private fun moveOrEat(lastIndex: Int, targetIndex: Int) {
        getPiece(targetIndex).level = getPiece(lastIndex).level
        getPiece(targetIndex).isRed = getPiece(lastIndex).isRed
        getPiece(lastIndex).level = -1
        state.value = state.value.copy(selectedIndex = -1)
    }

}

data class UiState(
    val Pieces: SnapshotStateList<SnapshotStateList<CellBean>> = mutableStateListOf(),
    val selectedIndex: Int = -1,
    val isRedTurn: Boolean = false,
)