package minesweeper

fun main() {
    val board = Board()
    println("How many mines do you want on the field?")
    board.setMines(readln().toInt())
    board.resultField.printField()
    while (!board.gameIsOver) {
        board.setUnset()
        board.checkIfWin()
    }
}
