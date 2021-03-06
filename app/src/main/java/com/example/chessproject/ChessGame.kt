package com.example.chessproject

import android.widget.Toast
import kotlin.math.abs

object ChessGame {

    var chessArray = mutableSetOf<Piece>()

    init {
        reset()
    }

    fun clear() {
        chessArray.clear()
    }

    fun addPiece(piece: Piece) {
        chessArray.add(piece)
    }

    fun movePiece(from: Square, to: Square) {
        movePiece(from.col, from.row, to.col, to.row)
    }

    fun canMove(from: Square, to: Square):Boolean {
        if(from.col == to.col && from.row == to.row)
            return false
        val movingPiece = pieceAt(from)?: return false
        when(movingPiece.types) {
            //Types.KING -> return canKingMove(from, to)
            Types.QUEEN -> return canQueenMove(from, to)
            Types.ROOK -> return canRookMove(from, to)
            Types.BISHOP -> return canBishopMove(from, to)
            Types.KNIGHT -> return canKnightMove(from, to)
            Types.PAWN -> return canPawnMove(from, to)
        }
        return true
    }

    private fun movePiece(fromCol : Int, fromRow: Int, toCol: Int, toRow: Int)
    {
        if(fromRow == toRow && fromCol == toCol) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if(it.player == movingPiece.player)
            {
                return
            }
            chessArray.remove(it)
        }
        if(canMove(Square(fromCol,fromRow), Square(toCol, toRow)))
        {
            chessArray.remove(movingPiece)
            chessArray.add(Piece(toCol, toRow, movingPiece.player, movingPiece.types, movingPiece.resID))
        }
    }

        fun reset() {
            chessArray.removeAll(chessArray)

            chessArray.add(Piece(3, 0, Player.WHITE, Types.QUEEN, R.drawable.queen_white))
            chessArray.add(Piece(3, 7, Player.BLACK, Types.QUEEN, R.drawable.queen_black))
            chessArray.add(Piece(4, 0, Player.WHITE, Types.KING, R.drawable.king_white_shorr))
            chessArray.add(Piece(4, 7, Player.BLACK, Types.KING, R.drawable.king_black_shorr))

            for(i in 0..1)
            {
                chessArray.add(Piece(0 + i * 7, 0, Player.WHITE, Types.ROOK, R.drawable.rook_white))
                chessArray.add(Piece(0 + i * 7, 7, Player.BLACK, Types.ROOK, R.drawable.rook_black))

                chessArray.add(Piece(1 + i * 5, 0, Player.WHITE, Types.KNIGHT, R.drawable.knight_white))
                chessArray.add(Piece(1 + i * 5, 7, Player.BLACK, Types.KNIGHT, R.drawable.knight_black))

                chessArray.add(Piece(2 + i * 3, 0, Player.WHITE, Types.BISHOP, R.drawable.bishop_white))
                chessArray.add(Piece(2 + i * 3, 7, Player.BLACK, Types.BISHOP, R.drawable.bishop_black))

            }

            for (i in 0..7)
            {
                chessArray.add(Piece(i, 1, Player.WHITE, Types.PAWN, R.drawable.pawn_white))
                chessArray.add(Piece(i, 6, Player.BLACK, Types.PAWN, R.drawable.pawn_black))
            }
        }


    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"

        return desc
    }

    private fun boardRow(row: Int) : String {
        var desc = ""
        for (col in 0 until 8) {
            desc += " "
            desc += pieceAt(col, row)?.let {
                val white = it.player == Player.WHITE
                when (it.types) {
                    Types.KING -> if (white) "k" else "K"
                    Types.QUEEN -> if (white) "q" else "Q"
                    Types.BISHOP -> if (white) "b" else "B"
                    Types.ROOK -> if (white) "r" else "R"
                    Types.KNIGHT -> if (white) "n" else "N"
                    Types.PAWN -> if (white) "p" else "P"
                }

            } ?: "."
        }
        return desc
    }

    fun pgn(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "${row + 1}"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  a b c d e f g h"

        return desc
    }
    fun pieceAt(square: Square): Piece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int) : Piece? {
        for (piece in chessArray)
        {
            if (row == piece.row && col == piece.col)
            {
                return piece
            }
        }
        return null
    }

    //Chess Rules
    fun canKnightMove(from: Square, to: Square): Boolean {
        if((abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1))
        {
            return true
        }
        else if ((abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2))
        {
            return true
        }
        return false
    }

    fun canRookMove(from: Square, to: Square): Boolean {
        if(from.col == to.col && isClearVertically(from, to)
            || from.row == to.row && isClearHorizontally(from, to))
        {
            return true
        }
        return false
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    private fun canQueenMove(from: Square, to: Square) : Boolean {
        if(canBishopMove(from, to) || canRookMove(from, to)){
            return true
        }
        return false
    }

    private fun canPawnMove(from: Square, to: Square) : Boolean {
        var space = abs(from.row - to.row)
        if(canPawnCapture(from, to)) return true
        if (pieceAt(from)?.player == Player.WHITE) {
            if(from.col == to.col)
            {
                if(from.row == 1)
                {
                    return to.row == 2 || to.row == 3
                }
                else if(to.row > from.row && space == 1)
                {
                    return true
                }
            }
        }
        if (pieceAt(from)?.player == Player.BLACK) {
            if(from.col == to.col)
            {
                if(from.row == 6)
                {
                    return to.row == 5 || to.row == 4
                }
                else if(space == 1 && to.row < from.row){
                    return true
                }
            }
        }
        return false
    }

    //Blocking Pieces
    private fun isClearHorizontally(from: Square, to: Square): Boolean{
        if(from.row != to.row) return false
        var space = abs(from.col - to.col) - 1
        if(space == 0) return true
        for (i in 1..space)
        {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Square(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearVertically(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Square(from.col, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextCol, nextRow) != null) {
                return false
            }
        }
        return true
    }

    private fun canPawnCapture(from: Square, to: Square) : Boolean {
        if(pieceAt(from)?.player == Player.WHITE)
        {
            if(from.col == to.col - 1 && from.row == to.row - 1 && pieceAt(to.col, to.row) != null) return true
        }
        if(pieceAt(from)?.player == Player.BLACK)
        {
            if(from.col == to.col + 1 && from.row == to.row + 1 && pieceAt(to.col, to.row) != null) return true
        }
        return false
    }
}
