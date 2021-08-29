package com.frogobox.pianotiles.game

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.frogobox.pianotiles.game.GameView.Companion.screenHeight
import com.frogobox.pianotiles.game.GameView.Companion.screenWidth


/**
 * Tile Class.
 * It goes from top to bottom
 * Purpose of the game is to press the tile
 */

class GameTile(
    private val context: Context,
    private var normalTileColor: Int,
    private var clickedTileColor: Int,
    private var loseTileColor: Int,
    row: Int
) {

    companion object {
        var speed = 30
    }

    private var startX: Int = 0
    var startY: Int = 0
    private var endX: Int = 0
    var endY: Int = 0

    var pressed: Boolean = false

    var outOfScreen = false
    private var outOfBounds = false
    var gameOver = false

    private var tileColor = normalTileColor

    init {
        startX = row * (screenWidth / 4)
        startY = -screenHeight / 4
        endX = screenWidth / 4 + startX
        endY = screenHeight / 4 + startY
    }

    /**
     * Draws the object on to the canvas.
     */
    fun draw(canvas: Canvas) {
        val rect = Rect(startX, startY, endX, endY)
        // canvas.drawRect(rect, tileColor)
        val bitmap = BitmapFactory.decodeResource(context.resources, tileColor)
        canvas.drawBitmap(bitmap, null, rect, null)
    }

    /**
     * update properties for the game object
     */
    fun update() {

        //stop the tile if it reaches the end
        if (startY >= screenHeight && !pressed) {
            tileColor = loseTileColor
            outOfBounds = true
            speed = -40
        }
        if (outOfBounds && endY <= screenHeight) {
            gameOver = true
        }
        if (startY >= screenHeight && pressed) {
            outOfScreen = true
        }
        startY += (speed)
        endY += (speed)

    }

    fun checkTouch(x: Float, y: Float): Boolean {
        if (x > startX - screenWidth / 30 && x < endX + screenWidth / 30 && y < endY && y > startY && !pressed) {
            tileColor = clickedTileColor
            GameView.score++
            pressed = true
            return pressed
        }
        return false
    }

}