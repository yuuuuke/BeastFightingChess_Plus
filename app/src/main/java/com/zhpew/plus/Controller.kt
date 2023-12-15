package com.zhpew.plus

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

object Controller {

    var onFinished: ((Boolean) -> Unit)? = null

    private val RedTrap = intArrayOf(2, 4, 10)
    private val BlueTrap = intArrayOf(52, 58,60)
    private val RedHome = intArrayOf(3)
    private val BlueHome = intArrayOf(60)
    private val River = intArrayOf(22, 23, 25, 26, 29, 30, 32, 33, 36, 37, 39, 40)

    val state = mutableStateOf(UiState(initCell()))

    fun refresh() {
        state.value = UiState(initCell())
    }

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

        if (lastCell.level == 6 || lastCell.level == 7) {
            //豹子老虎可以跨河，特殊处理,上下左右
            val access = getAccessCell(lastIndex)
            if (index in access) {
                //可走
                moveOrEat(lastIndex, index)
            }
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
        if ((lastIndex == -1 && state.value.isRedTurn == targetCell.isRed) || state.value.isRedTurn == targetCell.isRed) {
            //选中或者切换选中
            state.value = state.value.copy(selectedIndex = index)
            return
        } else {
            //吃别人的棋子
            if (lastCell.level == 6 || lastCell.level == 7) {
                // 豹子老虎可以跨河吃，特殊处理
                val access = getAccessCell(lastIndex)
                if (index in access) {
                    //可走
                    if ((index in BlueTrap)
                        || (index in RedTrap)
                    ) {
                        // 敌方棋子在我方陷阱中，可以吃掉，无视等级，直接吃掉
                        moveOrEat(lastIndex, index)
                    } else {
                        if (lastCell.level >= targetCell.level) {
                            // 可吃
                            moveOrEat(lastIndex, index)
                        }
                    }
                }
            } else {
                if (lastIndex % 7 == index % 7 + 1 || lastIndex % 7 == index % 7 - 1  //左右走
                    || lastIndex / 7 == index / 7 + 1 || lastIndex / 7 == index / 7 - 1 //往上下走
                ) {
                    if ((index in BlueTrap)
                        || (index in RedTrap)
                    ) {
                        // 敌方棋子在我方陷阱中，可以吃掉，无视等级，直接吃掉
                        moveOrEat(lastIndex, index)
                    } else {
                        if (lastCell.level >= targetCell.level || (lastCell.level == 1 && targetCell.level == 8)) {
                            // 可吃
                            moveOrEat(lastIndex, index)
                        }
                    }
                }
            }
        }


    }

    // 根据index 获取 cell
    private fun getPiece(index: Int): CellBean {
        if (index == -1) {
            return CellBean()
        }
        return state.value.Pieces[index / 7][index % 7]
    }

    private fun getAccessCell(index: Int): IntArray {
        val result = intArrayOf(-1, -1, -1, -1)
        // 左边能不能到达
        if ((index - 1) % 7 != 6) {
            // 没有换行
            if (index - 1 in River) {
                // 有河流，判断能不能跨河
                var target = index - 1
                var haveMouse = false
                while (target in River) {
                    if (getPiece(target).level == 1) {
                        //河中间有老鼠
                        haveMouse = true
                        break
                    }
                    target--
                }
                if (!haveMouse) {
                    result[0] = target
                }
            } else {
                // 没有河流，可到达
                result[0] = index - 1
            }
        }

        //右边能不能到达
        if ((index + 1) % 7 != 0) {
            // 没有换行
            if (index + 1 in River) {
                // 有河流，判断能不能跨河
                var target = index + 1
                var haveMouse = false
                while (target in River) {
                    if (getPiece(target).level == 1) {
                        //河中间有老鼠
                        haveMouse = true
                        break
                    }
                    target++
                }
                if (!haveMouse) {
                    result[1] = target
                }
            } else {
                // 没有河流，可到达
                result[1] = index + 1
            }
        }

        //下边
        if (index + 7 <= 62) {
            // 没有越界
            if (index + 7 in River) {
                // 有河流，判断能不能跨河
                var target = index + 7
                var haveMouse = false
                while (target in River) {
                    if (getPiece(target).level == 1) {
                        //河中间有老鼠
                        haveMouse = true
                        break
                    }
                    target += 7
                }
                if (!haveMouse) {
                    result[2] = target
                }
            } else {
                // 没有河流，可到达
                result[2] = index + 7
            }
        }

        //上边
        if (index - 7 >= 0) {
            // 没有越界
            if (index - 7 in River) {
                // 有河流，判断能不能跨河
                var target = index - 7
                var haveMouse = false
                while (target in River) {
                    if (getPiece(target).level == 1) {
                        //河中间有老鼠
                        haveMouse = true
                        break
                    }
                    target -= 7
                }
                if (!haveMouse) {
                    result[3] = target
                }
            } else {
                // 没有河流，可到达
                result[3] = index - 7
            }
        }
        return result
    }

    private fun moveOrEat(lastIndex: Int, targetIndex: Int) {
        getPiece(targetIndex).level = getPiece(lastIndex).level
        getPiece(targetIndex).isRed = getPiece(lastIndex).isRed
        getPiece(lastIndex).level = -1
        state.value = state.value.copy(selectedIndex = -1, isRedTurn = !state.value.isRedTurn)
        checkFinish()
    }

    // 判断结束
    private fun checkFinish() {

        if (getPiece(RedHome[0]).level != -1) {
            // 结束，蓝方赢了
            onFinished?.invoke(false)
            return
        }
        if (getPiece(BlueHome[0]).level != -1) {
            // 结束，红方胜利
            onFinished?.invoke(true)
            return
        }

        var isRedWin = true
        var isBlueWin = true

        for (index in 0 until 63) {
            val piece = getPiece(index)
            if (piece.level != -1) {
                if (piece.isRed) {
                    //红的还没死完
                    isRedWin = false
                } else {
                    isBlueWin = false
                }
            }
            if (!isRedWin && !isBlueWin) {
                // 对局还没结束
                return
            }
        }
        if (isRedWin) {
            onFinished?.invoke(true)
        }else{
            onFinished?.invoke(false)
        }
    }

}

data class UiState(
    val Pieces: SnapshotStateList<SnapshotStateList<CellBean>> = mutableStateListOf(),
    val selectedIndex: Int = -1,
    val isRedTurn: Boolean = false,
)