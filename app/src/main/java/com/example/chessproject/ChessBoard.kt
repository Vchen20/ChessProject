package com.example.chessproject

import android.content.Context
import android.graphics.*
import android.nfc.Tag
import android.view.View
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import kotlin.math.min

class ChessBoard(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val originX = 20f
    private val originY = 200f
    private val cellSide: Float = 175f
    private val black = Color.argb(1f, .4f, .4f, .4f)
    private val white = Color.argb(1f, .8f, .8f, .8f)
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val paint = Paint()

    private val imageIDs = setOf(
        R.drawable.king_white_shorr,
        R.drawable.king_black_shorr,
        R.drawable.queen_white,
        R.drawable.queen_black,
        R.drawable.rook_white,
        R.drawable.rook_black,
        R.drawable.bishop_white,
        R.drawable.bishop_black,
        R.drawable.knight_white,
        R.drawable.knight_black,
        R.drawable.pawn_white,
        R.drawable.pawn_black
    )

    private var movingPiece: Piece? = null
    private var movingPieceBitmap: Bitmap?= null
    private var fromCol  = -1
    private var fromRow = -1
    private var movingX : Float = -1f
    private var movingY : Float = -1f

    var chessDelegate : ChessDelegate? = null


    init {
        loadBitMaps()
    }

    override fun onDraw(canvas: Canvas?)
    {
        canvas?: return
        drawChessBoard(canvas)
        drawPieces(canvas)
    }

    private fun loadBitMaps() {
        imageIDs.forEach { imageIDs ->
            bitmaps[imageIDs] = BitmapFactory.decodeResource(resources, imageIDs)
        }
    }

    private fun drawPieces(canvas: Canvas)
    {
        for (row in 0..7)
        {
            for (col in 0..7) {
                if (col != fromCol || row != fromRow) {
                    chessDelegate?.pieceAt(Square(col, row))
                        ?.let { drawPieceAt(canvas, col, row, it.resID) }
                }
                chessDelegate?.pieceAt(Square(col, row))?.let {
                    if(it != movingPiece) {
                        drawPieceAt(canvas, col, row, it.resID)
                    }
                }
            }
        }
        movingPieceBitmap?.let {
            canvas.drawBitmap(it, null, RectF(
                movingX - cellSide/2,
                movingY - cellSide/2,
                movingX + cellSide,
                movingY + cellSide), paint)
        }


    }

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resID: Int)
    {
        val bitmap = bitmaps[resID]!!
        canvas.drawBitmap(bitmap, null, RectF(
            originX + col * cellSide,
            originY + (7-row) * cellSide,
            originX + (col + 1) * cellSide,
            originY + ((7-row) + 1) * cellSide), paint)
    }

    private fun drawChessBoard(canvas: Canvas) {
        val paint = Paint()
        for(row in 0..7)
        {
            for(col in 0..7){
                drawSquareAt(canvas, col, row, (row + col) % 2 == 1)
            }
        }
    }

    private fun drawSquareAt(canvas: Canvas, col: Int, row: Int, isDark: Boolean)
    {
        paint.color = if (isDark) black else white
        canvas.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY + (row + 1) * cellSide, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?: return false
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()

                chessDelegate?.pieceAt(Square(fromCol, fromRow))?.let {
                    movingPiece = it
                    movingPieceBitmap = bitmaps[it.resID]
                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingX = event.x
                movingY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                Log.d(TAG, "from ($fromCol, $fromRow) to ($col, $row)")
                chessDelegate?.movePiece(Square(fromCol, fromRow), Square(col, row))
                movingPieceBitmap = null
                movingPiece = null
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller,smaller)
    }

}