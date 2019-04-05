// Copyright (c) 2019 Roman Sisik
#include <jni.h>
#include <android/bitmap.h>
#include <tuple>
#include <cmath>
#include <algorithm>
#include "log.h"

namespace sisik {
    int getLuma(int r, int g, int b) {
        return std::lroundf(0.299f*r + 0.587f*g + 0.114f*b);
    }

    std::tuple<int, int, int, int> getRGBL(uint32_t pixel) {
        int red = (int) (pixel & 0x0000FF);
        int green = (int)((pixel & 0x00FF00) >> 8);
        int blue = (int) ((pixel & 0xFF0000) >> 16);
        int luma = getLuma(red, green, blue);

        return std::make_tuple(red, green, blue, luma);
    }

    int normalize(int v, int min, int max, int newMin, int newMax) {
        return std::lrintf((v - min) * float(newMax - newMin)/(max - min) + newMin);
    }

    void normalize(int* vals, std::size_t size, int newMin, int newMax) {
        int min = *std::min_element(vals, vals + size);
        int max = *std::max_element(vals, vals + size);

        for (size_t i = 0; i < size; i++) {
            vals[i] = normalize(vals[i], min, max, newMin, newMax);
        }
    }
}

extern "C" {

JNIEXPORT void JNICALL
Java_eu_sisik_pixelproc_histo_Histogram_generate(JNIEnv* env, jclass, jobject bitmap,
        jintArray redBins, jintArray greenBins, jintArray blueBins, jintArray lumaBins, jboolean normalize) {

    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);

    // Access the pixels
    void* pixels;
    AndroidBitmap_lockPixels(env, bitmap, &pixels);

    int* rBins = (int*)env->GetPrimitiveArrayCritical(redBins, 0);
    int* gBins = (int*)env->GetPrimitiveArrayCritical(greenBins, 0);
    int* bBins = (int*)env->GetPrimitiveArrayCritical(blueBins, 0);
    int* lBins = (int*)env->GetPrimitiveArrayCritical(lumaBins, 0);

    for (int y = 0; y < info.height; y++) {

        uint32_t * line = (uint32_t *)pixels;

        for (int x = 0; x < info.width; x++) {

            auto [red, green, blue, luma] = sisik::getRGBL(line[x]);

            rBins[red]++;
            gBins[green]++;
            bBins[blue]++;
            lBins[luma]++;
        }

        pixels = (uint8_t *)pixels + info.stride;
    }

    if (normalize) {
        sisik::normalize(rBins, env->GetArrayLength(redBins), 0, 255);
        sisik::normalize(bBins, env->GetArrayLength(blueBins), 0, 255);
        sisik::normalize(gBins, env->GetArrayLength(greenBins), 0, 255);
        sisik::normalize(lBins, env->GetArrayLength(lumaBins), 0, 255);
    }

    env->ReleasePrimitiveArrayCritical(redBins, rBins, 0);
    env->ReleasePrimitiveArrayCritical(greenBins, gBins, 0);
    env->ReleasePrimitiveArrayCritical(blueBins, bBins, 0);
    env->ReleasePrimitiveArrayCritical(lumaBins, lBins, 0);

    AndroidBitmap_unlockPixels(env, bitmap);
}
} // extern "C"


