package Handlers

import Listeners.StateListener
import Models.Move

class TicTacToe {


    private fun getChildren(): MutableList<Pair<Int, Int>> {
        val children: MutableList<Pair<Int, Int>> = ArrayList()
        board.forEachIndexed { index, chars ->
            chars.forEachIndexed { indexC, c ->
                if (c == '_') children.add(index to indexC)
            }
        }
        return children
    }

    private fun isLeaf(depth: Int): Pair<Boolean, Int> {
        val end = isEnd(board)
        return if (end.first) {
            if (end.second == player) true to getHeuristicValue(depth, true)
            else true to getHeuristicValue(depth, false)
        } else {
            if (isDraw(board))
                true to 0
            else false to Int.MIN_VALUE
        }
    }

    private fun getHeuristicValue(depth: Int, isPlayer: Boolean): Int {
        return if (isPlayer) 10 - depth
        else -(10 - depth)
    }

    private fun minimax(board: Array<CharArray>, depth: Int, alpha: Int, beta: Int, isMax: Boolean): Int {

        var _alpha = alpha
        var _beta = beta
        var best: Int

        val leaf = isLeaf(depth)
        if (leaf.first)
            return leaf.second


        if (isMax) {
            best = Integer.MIN_VALUE

            for (child in getChildren()) {

                board[child.first][child.second] = player

                best = Math.max(best, minimax(board, depth + 1, _alpha, _beta, false))
                _alpha = Math.max(_alpha, best)

                board[child.first][child.second] = '_'

                if (_beta <= _alpha) break
            }

        } else {

            best = Integer.MAX_VALUE

            for (child in getChildren()) {

                board[child.first][child.second] = opponent

                best = Math.min(best, minimax(board, depth + 1, _alpha, _beta, true))
                _beta = Math.min(_beta, best)

                board[child.first][child.second] = '_'

                if (_beta <= _alpha) break
            }
        }
        return best
    }

    private fun findBestMove(board: Array<CharArray>): Move {

        var bestVal = Integer.MIN_VALUE
        val bestMove = Move()

        bestMove.row = -1
        bestMove.col = -1

        for (i in 0..BoardSize) {
            for (j in 0..BoardSize) {

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

        const val player = 'X'
        const val opponent = 'O'

        private var win = false
        private var BoardSize = 2
        lateinit var buffer: MutableList<Char>


        private lateinit var board: Array<CharArray>
        private lateinit var game: TicTacToe
        private lateinit var listener: StateListener


        private fun isEnd(board: Array<CharArray>): Pair<Boolean, Char> {

            // Check Rows
            for (row in 0..BoardSize) {
                if (board[row].all { it == player })
                    return true to player
                else if (board[row].all { it == opponent })
                    return true to opponent
            }


            // Check Cols
            for (col in 0..BoardSize) {
                if (board.all { it[col] == player })
                    return true to player
                else if (board.all { it[col] == opponent })
                    return true to opponent
            }


            // Check Diagonals
            buffer.clear()
            for (i in 0..BoardSize)
                buffer.add(i, board[i][i])

            if (buffer.all { it == player })
                return true to player
            else if (buffer.all { it == opponent })
                return true to opponent

            buffer.clear()
            for (i in 0..BoardSize)
                buffer.add(i, board[BoardSize - i][i])

            if (buffer.all { it == player })
                return true to player
            else if (buffer.all { it == opponent })
                return true to opponent

            return false to '@'

        }

        private fun isDraw(board: Array<CharArray>): Boolean {
            return !board.any { chars -> chars.any { char -> char == '_' } }
        }

        private fun print(board: Array<CharArray>) {
            board.forEach { chars ->
                println(chars)
            }
        }

        private fun generateBoard(): Array<CharArray> {
            val board: MutableList<CharArray> = ArrayList()

            for (i in 0..BoardSize) {
                val array = CharArray(BoardSize + 1)
                for (j in 0..BoardSize)
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


        fun run(_BoardSize: Int, _listener: StateListener) {
            BoardSize = _BoardSize
            board = generateBoard()
            game = TicTacToe()
            listener = _listener
            buffer = ArrayList()
            win = false
        }

        fun move(playerMove: Move) {

            if (!win) {
                try {
                    if (canUseThePlace(board, playerMove.row to playerMove.col)) {
                        board[playerMove.row][playerMove.col] = player
                        listener.onStateChange(playerMove, player)


                        val move: Move = game.findBestMove(board)

                        board[move.row][move.col] = opponent
                        listener.onStateChange(move, opponent)


                        val end = isEnd(board)
                        if (end.first) {
                            // print(board)
                            println("${end.second} Win!!")
                            listener.onDone(end.second.toUpperCase())
                            win = true
                        }
                        //print(board)
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