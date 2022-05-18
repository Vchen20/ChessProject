package com.example.chessproject

import org.junit.Test
import org.junit.Assert.*

class CanKnightMove {
    @Test
    fun canKnightMove()
    {
        ChessGame.clear()
        ChessGame.addPiece(Piece(3,0, Player.WHITE, Types.KNIGHT, -1))
        println(ChessGame)
        assertTrue(ChessGame.canKnightMove(Square(3,0), Square(5,1)))
        assertTrue(ChessGame.canKnightMove(Square(3,0), Square(4,2)))
        assertFalse(ChessGame.canKnightMove(Square(3,1), Square(4,2)))
    }
}