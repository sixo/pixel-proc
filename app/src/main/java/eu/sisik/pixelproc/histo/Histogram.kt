package eu.sisik.pixelproc.histo

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import java.lang.IllegalStateException
import kotlin.math.roundToInt

/**
 * Copyright (c) 2019 by Roman Sisik. All rights reserved.
 */

class Histogram(var bitmap: Bitmap? = null) {

    fun generateKotlin(normalized: Boolean = true): Array<IntArray> {
        checkParams()

        val (rBins, gBins, bBins, lBins) = createBins()


        for (pixel in getPixels()) {
            val (r, g, b, l) = getPixelValues(pixel)

            rBins[r]++
            gBins[g]++
            bBins[b]++
            lBins[l]++
        }

        val result = arrayOf(rBins, gBins, bBins, lBins)

        if (normalized)
            return normalize(result, 0, 255)

        return result
    }

    private fun checkParams() {
        if (bitmap == null)
            throw IllegalStateException("No Bitmap specified!")

        if (bitmap?.config != Bitmap.Config.ARGB_8888)
            throw IllegalStateException("Only ARGB_8888 bitmap format supported!")
    }

    private fun getPixels(): IntArray {
        val pixels = IntArray(bitmap!!.width * bitmap!!.height)
        bitmap!!.getPixels(pixels, 0, bitmap!!.width, 0, 0, bitmap!!.width, bitmap!!.height)

        return pixels
    }

    private fun createBins(): Array<IntArray> {
        return arrayOf(
            IntArray(256) {0},
            IntArray(256) {0},
            IntArray(256) {0},
            IntArray(256) {0})
    }

    private fun getPixelValues(pixel: Int): IntArray {
        val r = Color.red(pixel)
        val g = Color.green(pixel)
        val b = Color.blue(pixel)
        val l = getLuma(r, g, b)

        return intArrayOf(r, g, b, l)
    }

    private inline fun getLuma(r: Int, g: Int, b: Int): Int {
        return (0.299f*r + 0.587f*g + 0.114f*b).roundToInt()
    }

    private fun normalize(vals: Array<IntArray>, newMin: Int, newMax: Int): Array<IntArray> {
        val normalized = Array<IntArray>(vals.size) {IntArray(0)}
        for (i in 0 until vals.size)
            normalized[i] = normalize(vals[i], newMin, newMax)

        return normalized
    }

    private fun normalize(vals: IntArray, newMin: Int, newMax: Int): IntArray {
        val normalized = IntArray(vals.size) {0}
        val min = vals.min()
        val max = vals.max()

        for (i in 0 until vals.size)
            normalized[i] = normalize(vals[i], min!!, max!!, newMin, newMax)

        return normalized
    }

    private inline fun normalize(v: Int, min: Int, max: Int, newMin: Int, newMax: Int): Int {
        return ((v - min) * (newMax - newMin).toFloat()/(max - min) + newMin).roundToInt()
    }

    fun generateCpp(normalized: Boolean = true): Array<IntArray> {
        checkParams()

        val (rBins, gBins, bBins, lBins) = createBins()
        generate(bitmap!!, rBins, gBins, bBins, lBins, normalized)

        return arrayOf(rBins, gBins, bBins, lBins)
    }

    companion object {
        @JvmStatic private external fun generate(bitmap: Bitmap, redBins: IntArray, greenBins: IntArray,
                                                 blueBins: IntArray, lumaBins: IntArray, normalize: Boolean = true)

        init {
            System.loadLibrary("pixel-proc")
        }
    }

}