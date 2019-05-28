package Listeners

import Models.Move

interface InputListener {
    fun onNewInput(move: Move)
}