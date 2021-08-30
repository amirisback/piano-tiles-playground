package com.frogobox.pianotiles.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.frogobox.pianotiles.R
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    companion object {
        var score = 0
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        var music = true
        var vibration = true
    }

    private val TAG = GameView::class.java.simpleName
    
    private val thread: GameThread

    private var tiles = LinkedList<GameTile>()
    private var tempTiles = CopyOnWriteArrayList<GameTile>()

    private val normalTileColor = R.drawable.node_normal
    private val clickedTileColor = R.drawable.node_clicked
    private val loseTileColor = R.drawable.node_lose

    private val bitmapLoseTile = BitmapFactory.decodeResource(resources, loseTileColor)

    private var row = -1
    private var lastRow = -1

    private var gameOver = false
    private var gameOverOver = false // true after game over sound is played
    private var tappedWrongTile = -1
    private var startY = -1
    private var endY = -1

    private var touchedX = 0f
    private var touchedY = 0f

    private var started = false

    init {
        // add callback
        setZOrderOnTop(true)

        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)


        // instantiate the game thread
        thread = GameThread(holder, this)
        score = 0
        row = (0..3).random()

        //game objects
        tiles.add(GameTile(context, normalTileColor, clickedTileColor, loseTileColor, row))
        lastRow = row

    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        Log.d(TAG, "surface created")
        thread.setRunning(true)
        if (!started) {
            // start the game thread
            Log.d(TAG, "started")
            thread.start()
            started = true
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        Log.d(TAG, "surface changed")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")
        thread.setRunning(false)
    }

    /** Everything that has to be drawn on Canvas */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        // stop the game
        if (gameOver && !gameOverOver) {
            GameTile.speed = 0
            thread.setRunning(false)
            gameOverOver = true
            (context as GameActivity).goToGameOver()
        }

        // remove the tiles that are out of screen from tiles list
        if (tiles.first.outOfScreen) {
            tiles.poll()
        }
        // draw new tiles when last one is on the screen
        if (tiles.last.startY >= 0) {
            do {
                row = (0..3).random()
            } while (row == lastRow)

            tiles.add(GameTile(context, normalTileColor, clickedTileColor, loseTileColor, row))

            lastRow = row
        }
        // update and draw all tiles
        for (tile in tiles) {
            tile.update()
            tile.draw(canvas)
            if (tile.gameOver) {
                gameOver = true
            }
        }
        // draw red tile if pressed the wrong tile
        when (tappedWrongTile) {
            0 -> {
                val rect = Rect(0, startY, screenWidth / 4, endY)
                canvas.drawBitmap(bitmapLoseTile, null, rect, null)
            }
            1 -> {
                val rect = Rect(screenWidth / 4, startY, screenWidth / 2, endY)
                canvas.drawBitmap(bitmapLoseTile, null, rect, null)
            }
            2 -> {
                val rect = Rect(screenWidth / 2, startY, screenWidth * 3 / 4, endY)
                canvas.drawBitmap(bitmapLoseTile, null, rect, null)
            }
            3 -> {
                val rect = Rect(screenWidth * 3 / 4, startY, screenWidth, endY)
                canvas.drawBitmap(bitmapLoseTile, null, rect, null)
            }
        }
        createScore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        event.actionMasked.let { action ->
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                event.actionIndex.let { index ->
                    if (GameTile.speed > 0) {
                        touchedX = event.getX(index)
                        touchedY = event.getY(index)
                        tempTiles = CopyOnWriteArrayList(tiles)
                        for (tile in tempTiles) {
                            if (tile.checkTouch(touchedX, touchedY)) {
                                break
                            } else if (!tile.pressed && touchedY < tile.endY && touchedY > tile.startY) {
                                // pressed wrong place
                                tappedWrongTile = when {
                                    (touchedX < screenWidth / 4) -> 0
                                    (touchedX < screenWidth / 2) -> 1
                                    (touchedX < 3 * screenWidth / 4) -> 2
                                    else -> 3
                                }
                                startY = tile.startY
                                endY = tile.endY
                                gameOver = true
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    fun createScore() {
        (context as GameActivity).setScoreText(score.toString())
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.background),
            null,
            Rect(0, 0, screenWidth, screenHeight),
            null
        )
    }

}