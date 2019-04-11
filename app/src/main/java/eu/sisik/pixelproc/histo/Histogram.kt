package eu.sisik.pixelproc.histo

import android.graphics.Bitmap
import android.graphics.Color
import java.lang.IllegalStateException
import kotlin.math.roundToInt
import android.renderscript.*
import android.util.Log
import java_eu_sisik_pixelproc_histo.ScriptC_histo
import android.renderscript.Allocation
import android.renderscript.Element.U8_4
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript



/**
 * Copyright (c) 2019 by Roman Sisik. All rights reserved.
 */

class Histogram(var bitmap: Bitmap? = null, val rs: RenderScript? = null) {


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

    fun generateRs(normalized: Boolean = true): Array<IntArray> {
        checkParams()

        val (rBins, gBins, bBins, lBins) = createBins()
        val (redAlloc, greenAlloc, blueAlloc, lumaAlloc) = createBinAllocations(rBins.size)

        val script = ScriptC_histo(rs)
        bindAllocations(script, redAlloc, greenAlloc, blueAlloc, lumaAlloc)

        val bitmapAllocation = Allocation.createFromBitmap(rs, bitmap)
        script.forEach_histo(bitmapAllocation)

        if (normalized)
            normalize(script, redAlloc, blueAlloc, greenAlloc, lumaAlloc)

        redAlloc.copyTo(rBins)
        greenAlloc.copyTo(gBins)
        blueAlloc.copyTo(bBins)
        lumaAlloc.copyTo(lBins)

        return arrayOf(rBins, gBins, bBins, lBins)
    }

    private fun createBinAllocations(size: Int): Array<Allocation> {
        val redAlloc = Allocation.createSized(rs, Element.I32(rs), size)
        val greenAlloc = Allocation.createSized(rs, Element.I32(rs), size)
        val blueAlloc = Allocation.createSized(rs, Element.I32(rs), size)
        val lumaAlloc = Allocation.createSized(rs, Element.I32(rs), size)

        return arrayOf(redAlloc, greenAlloc, blueAlloc, lumaAlloc)
    }

    private fun bindAllocations(script: ScriptC_histo, r: Allocation, g: Allocation, b: Allocation, l: Allocation) {
        script.bind_reds(r)
        script.bind_greens(g)
        script.bind_blues(b)
        script.bind_lumas(l)
    }

    private fun normalize(script: ScriptC_histo,
                          redAlloc: Allocation, greenAlloc: Allocation, blueAlloc: Allocation, lumaAlloc: Allocation) {
        normalize(script, redAlloc)
        normalize(script, greenAlloc)
        normalize(script, blueAlloc)
        normalize(script, lumaAlloc)
    }

    private fun normalize(script: ScriptC_histo, allocation: Allocation) {
        var minMax = script.reduce_minMax(allocation).get()
        script._minVal = minMax.x
        script._maxVal = minMax.y
        script.forEach_norm(allocation, allocation)
    }

    companion object {
        @JvmStatic private external fun generate(bitmap: Bitmap, redBins: IntArray, greenBins: IntArray,
                                                 blueBins: IntArray, lumaBins: IntArray, normalize: Boolean = true)

        init {
            System.loadLibrary("pixel-proc")
        }
    }

}