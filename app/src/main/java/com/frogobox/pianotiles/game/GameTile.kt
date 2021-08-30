package com.frogobox.pianotiles.game

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
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
    private val normalTileColor: Int,
    private val clickedTileColor: Int,
    private val loseTileColor: Int,
    row: Int
) {

    companion object {
        var speed = 30
    }

    private var startX: Int = row * (screenWidth / 4)
    var startY: Int = -screenHeight / 4

    private var endX: Int = screenWidth / 4 + startX
    var endY: Int = screenHeight / 4 + startY

    var pressed: Boolean = false

    var outOfScreen = false
    private var outOfBounds = false

    var gameOver = false

    private var tileColor = normalTileColor

    /**
     * Draws the object on to the canvas.
     */
    fun draw(canvas: Canvas) {
        val rect = Rect(startX, startY, endX, endY)
        val bitmap = BitmapFactory.decodeResource(context.resources, tileColor)
        canvas.drawBitmap(bitmap, null, rect, null)
    }

    /**
     * update properties for the game object
     */
    fun update() {
        // Stop the tile if it reaches the end
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