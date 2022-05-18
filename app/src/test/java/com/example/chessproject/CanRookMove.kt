package com.example.chessproject

import org.junit.Test
import org.junit.Assert.*

class CanRookMove {
    @Test
    fun canRookMove()
    {
        ChessGame.clear()
        ChessGame.addPiece(Piece(3,0, Player.WHITE, Types.ROOK, -1))
        println(ChessGame)
        assertTrue(ChessGame.canRookMove(Square(3,0), Square(7,0)))
        //assertTrue(ChessGame.canRookMove(Square(3,0), Square(3,5)))
        assertFalse(ChessGame.canRookMove(Square(3,1), Square(4,2)))
    }

    @Test
    fun canRookMove_Blocked() {
        ChessGame.clear()
        ChessGame.addPiece(Piece(3,0, Player.WHITE, Types.ROOK, -1))
        ChessGame.addPiece(Piece(4,0, Player.WHITE, Types.KNIGHT, -1))
        println(ChessGame)
        assertFalse(ChessGame.canMove(Square(3,0), Square(6,0)))

    }
}