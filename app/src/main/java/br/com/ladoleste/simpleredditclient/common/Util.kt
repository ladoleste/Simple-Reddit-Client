package br.com.ladoleste.simpleredditclient.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import br.com.ladoleste.simpleredditclient.R
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 *Created by Anderson on 19/02/2018.
 */
object Util {

    fun readFileFromAssets(cx: Context, fileName: String, indice: Int = 0): String {
        val builder = StringBuilder()
        try {
            val stream = cx.assets.open(fileName)
            val bReader = BufferedReader(InputStreamReader(stream, "UTF-8") as Reader?)
            var line: String?
            while (true) {
                line = bReader.readLine()
                if (line == null) {
                    break
                }
                builder.append(line)
            }
        } catch (e: IOException) {
            Timber.e(e)
        }

        return builder.toString().substring(indice)
    }

    fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
        var drawable: Drawable = AppCompatResources.getDrawable(CustomApplication.instance, drawableId)!!
        drawable = DrawableCompat.wrap(drawable).mutate()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun getErrorMessage(e: Throwable?) = when (e) {
        is SocketTimeoutException -> R.string.no_connection
        is UnknownHostException -> R.string.no_connection
        else -> R.string.generic_error
    }
}
