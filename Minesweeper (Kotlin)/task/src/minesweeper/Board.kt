package minesweeper

import kotlin.random.Random

class Board {
    val field = Array(9) { Array(9) { '.' } }
    val resultField = Array(9) { Array(9) { '.' } }
    var gameIsOver = false
    var minesQuantity = 0

    fun setMines(minesQuantity: Int) {
        var i = minesQuantity
        this.minesQuantity = minesQuantity
        while (i > 0) {
            val x = Random.nextInt(0, field.size)
            val y = Random.nextInt(0, field[0].size)
            if (field[x][y] == '.') {
                field[x][y] = 'X'
                i--
            }
        }
        setMinesNumbersField()
    }

    private fun setMinesNumbersField() {
        for (i in field.indices) {
            for (j in field[0].indices) {
                field[i][j] = countMinesAround(Cell(i, j))
            }
        }
    }

    fun checkIfWin() {
        if (gameIsOver) return

        gameIsOver = true
        val asterics = resultField.joinToString ("") { it.joinToString("") }.count { it == '*' }
        for (i in field.indices) {
            for (j in field[0].indices) {
                if (field[i][j] == 'X') {
                   if (resultField[i][j] != '*') {
                       gameIsOver = false
                   }
                }
            }
        }
        if (asterics != minesQuantity) {
            gameIsOver = false
        }

        if (gameIsOver) {
            println("Congratulations! You found all the mines!")
        }
    }

    fun setUnset() {
        println("Set/unset mines marks or claim a cell as free:")
        val line = readln().split(" ")
        when(line[2]) {
            "free" -> setFree(line[1].toInt() - 1, line[0].toInt() - 1)
            "mine" -> setMine(line[1].toInt() - 1, line[0].toInt() - 1)
        }
    }

    private fun setMine(x: Int, y: Int) {
        when {
            resultField[x][y] == '.' -> {
                resultField[x][y] = '*'
                resultField.printField()
            }
            resultField[x][y] == '*' -> {
                resultField[x][y] = '.'
                resultField.printField()
            }
            else -> {
                println("There is a number here!")
            }
        }
    }

    private fun setFree(x: Int, y: Int) {
        if (field[x][y].toString().matches("\\d".toRegex())) {
            resultField[x][y] = field[x][y]
        } else if (field[x][y] == 'X') {
            gameIsOver = true
        } else {
            openEmptyCells(x, y)
        }
        resultField.printField()
        if (gameIsOver) {
            println("You stepped on a mine and failed!")
        }
    }

    private fun openEmptyCells(x: Int, y: Int) {
        if (field[x][y] == '.') {
            resultField[x][y] = '/'
            field[x][y] = '/'
            val list = getListAround(Cell(x, y)).filter { field[it.x][it.y] == '.' || field[it.x][it.y].toString().matches("\\d".toRegex()) }
            list.forEach { openEmptyCells(it.x, it.y) }
        } else if (field[x][y].toString().matches("\\d".toRegex())) {
            resultField[x][y] = field[x][y]
        }
    }

    private fun getListAround(cell: Cell): List<Cell> {
        val x = cell.x
        val y = cell.y
        val listCellAround = mutableListOf<Cell>()
        listCellAround.add(Cell(x - 1, y - 1))
        listCellAround.add(Cell(x - 1, y))
        listCellAround.add(Cell(x - 1, y + 1))
        listCellAround.add(Cell(x, y - 1))
        listCellAround.add(Cell(x, y + 1))
        listCellAround.add(Cell(x + 1, y - 1))
        listCellAround.add(Cell(x + 1, y))
        listCellAround.add(Cell(x + 1, y + 1))

        return listCellAround.filter { it.x in 0..8 && it.y in 0..8 }
    }

    private fun countMinesAround(cell: Cell): Char {
        if (field[cell.x][cell.y] == 'X') return 'X'

        val listCellAround = getListAround(cell)
        val count = listCellAround.count { field[it.x][it.y] == 'X'}

        return if (count == 0) '.' else (count + 48).toChar()
    }
}

fun Array<Array<Char>>.printField() {
    println(" │123456789│\n" +
            "—│—————————│")
    this.forEachIndexed { index, c -> println("${index + 1}|${c.joinToString("")}|") }
    println("—│—————————│")
}

class Cell(val x: Int, val y: Int)