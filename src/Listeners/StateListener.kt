package Listeners

import Models.Move

interface StateListener {
    fun onStateChange(move: Move, player: Char)
    fun onDone(winner: Char)
}