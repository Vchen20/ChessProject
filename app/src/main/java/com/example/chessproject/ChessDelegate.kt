package com.example.chessproject

interface ChessDelegate {
    fun pieceAt(square: Square) : Piece?
    fun movePiece(from: Square, to: Square)
}