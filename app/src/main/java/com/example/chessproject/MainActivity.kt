package com.example.chessproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

const val TAG = "MainActivity"
var chessModel = ChessGame
lateinit var chessBoard: ChessBoard
class MainActivity : AppCompatActivity(), ChessDelegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "$chessModel")
        chessBoard = findViewById<ChessBoard>(R.id.chess_board)
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            chessModel.reset()
            chessBoard.invalidate()
        }
        chessBoard.chessDelegate = this
    }

    override fun pieceAt(square: Square): Piece? = ChessGame.pieceAt(square)

    override fun movePiece(from: Square, to: Square) {
        chessModel.movePiece(from, to)
        chessBoard.invalidate()


    }
}