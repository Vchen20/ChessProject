package com.example.chessproject

import android.graphics.drawable.Drawable
import com.example.chessproject.Types

data class Piece(
    val col: Int,
    val row: Int,
    val player: Player,
    val types: Types,
    val resID: Int
) {}
