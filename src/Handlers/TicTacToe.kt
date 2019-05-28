package Handlers

import Listeners.StateListener
import Models.Move

class TicTacToe {


    private fun isMovesLeft(board: Array<CharArray>): Boolean {
        return board.any { chars -> chars.any { char -> char == '_' } }
    }

    private fun evaluate(board: Array<CharArray>, depth: Int): Int {

        // Check Rows
        for (row in 0..2) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == player)
                    return 10 - depth
                else if (board[row][0] == opponent)
                    return -10 + depth
            }
        }

        // Check Cols
        for (col in 0..2) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == player)
                    return 10 - depth
                else if (board[0][col] == opponent)
                    return -10 + depth
            }
        }

        // Check Diagonals

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == player)
                return 10 - depth
            else if (board[0][0] == opponent)
                return -10 + depth
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == player)
                return 10 - depth
            else if (board[0][2] == opponent)
                return -10 + depth
        }

        // is Draw
        return 0
    }

    private fun minimax(board: Array<CharArray>, depth: Int, alpha: Int, beta: Int, isMax: Boolean): Int {

        var _alpha = alpha
        var _beta = beta

        val score = evaluate(board, depth)

        if (score > 0 || score < 0)
            return score

        if (!isMovesLeft(board))
            return 0

        if (isMax) {
            var best = Integer.MIN_VALUE

            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == '_') {

                        board[i][j] = player

                        best = Math.max(best, minimax(board, depth + 1, _alpha, _beta, false))
                        _alpha = Math.max(_alpha, best)

                        board[i][j] = '_'

                        if (_beta <= _alpha)
                            break
                    }
                }
            }
            return best
        } else {
            var best = Integer.MAX_VALUE

            for (i in 0..2) {
                for (j in 0..2) {

                    if (board[i][j] == '_') {
                        board[i][j] = opponent

                        best = Math.min(best, minimax(board, depth + 1, _alpha, _beta, true))
                        _beta = Math.min(_beta, best)

                        board[i][j] = '_'

                        if (_beta <= _alpha)
                            break
                    }
                }
            }
            return best
        }
    }

    private fun findBestMove(board: Array<CharArray>): Move {

        var bestVal = Integer.MIN_VALUE
        val bestMove = Move()

        bestMove.row = -1
        bestMove.col = -1

        for (i in 0..2) {
            for (j in 0..2) {

                if (board[i][j] == '_') {

                    board[i][j] = player

                    val moveVal = minimax(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false)

                    board[i][j] = '_'

                    if (moveVal > bestVal) {
                        bestMove.row = i
                        bestMove.col = j
                        bestVal = moveVal
                    }
                }
            }
        }
        return bestMove
    }

    companion object {

        private val player = 'x'
        private val opponent = 'o'
        private var win = false

        private lateinit var board: Array<CharArray>
        private lateinit var game: TicTacToe
        private lateinit var listener: StateListener


        private fun isEnd(board: Array<CharArray>): Pair<Boolean, Char> {

            for (row in 0..2) {
                if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                    if (board[row][1] == player)
                        return true to player
                    else if (board[row][1] == opponent)
                        return true to opponent
                }
            }

            for (col in 0..2) {
                if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                    if (board[1][col] == player)
                        return true to player
                    else if (board[1][col] == opponent)
                        return true to opponent
                }
            }

            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                if (board[1][1] == player)
                    return true to player
                else if (board[1][1] == opponent)
                    return true to opponent
            }

            if (board[0][2] == board[1][1] && board[1][1] == board[2][0])
                if (board[1][1] == player)
                    return true to player
                else if (board[1][1] == opponent)
                    return true to opponent

            return false to '@'

        }

        private fun isDraw(board: Array<CharArray>): Boolean {
            return !board.any { chars -> chars.any { char -> char == '_' } }
        }

        private fun print(board: Array<CharArray>) {
            for (row in 0 until 3) {
                for (col in 0 until 3)
                    print(board[row][col] + "    ")
                println()
            }

            println("\n*** *** ***")
        }

        private fun generateBoard(size: Int): Array<CharArray> {
            val board: MutableList<CharArray> = ArrayList()

            for (i in 0 until size) {
                val array = CharArray(size)
                for (j in 0 until size)
                    array[j] = '_'
                board.add(i, array)
            }


            return board.toTypedArray()
        }

        private fun canUseThePlace(board: Array<CharArray>, input: Pair<Int, Int>): Boolean {
            if (board[input.first][input.second] != '_')
                return false
            return true
        }

        private fun getInputFromPlayer(): Pair<Int, Int> {

            val c: Int
            val r: Int

            while (true) {
                print("Enter the Row : ")
                val row = readLine()!!.toInt()
                if (row !in 1..3)
                    println("Enter Row in 1..3 range!!")
                else {
                    r = row
                    break
                }
            }

            while (true) {
                print("Enter the Column : ")
                val col = readLine()!!.toInt()
                if (col !in 1..3)
                    println("Enter Column in 1..3 range!!")
                else {
                    c = col
                    break
                }
            }


            return r - 1 to c - 1
        }


        fun run(_listener: StateListener) {
            board = generateBoard(3)
            game = TicTacToe()
            listener = _listener
            win = false
        }

        fun move(playerMove: Move) {

            if (!win) {
                //val input = getInputFromPlayer()
                try {
                    if (canUseThePlace(board, playerMove.row to playerMove.col)) {
                        board[playerMove.row][playerMove.col] = 'x'
                        listener.onStateChange(playerMove, 'X')


                        val move: Move = game.findBestMove(board)
                        board[move.row][move.col] = 'o'
                        listener.onStateChange(move, 'O')
                        println("opponent Selected!!")


                        val end = isEnd(board)
                        if (end.first) {
                            print(board)
                            println("${end.second} Win!!")
                            listener.onDone(end.second.toUpperCase())
                            win = true
                        }
                        print(board)
                    } else
                        println("Enter a Another Place!!")
                } catch (e: Exception) {
                    if (isDraw(board))
                        listener.onDone('@')
                }
            } else
                println("Game Finished!! , Restart Game")
        }
    }
}