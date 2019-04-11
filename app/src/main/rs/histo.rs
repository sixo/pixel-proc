#pragma version(1)
#pragma rs java_package_name(java_eu_sisik_pixelproc_histo)
#pragma rs_fp_relaxed

#define INT_MIN -2147483648
#define INT_MAX 2147483647

int32_t* reds;
int32_t* greens;
int32_t* blues;
int32_t* lumas;

int32_t minVal;
int32_t maxVal;
int32_t newMinVal = 0;
int32_t newMaxVal = 255;

void RS_KERNEL histo(uchar4 in) {
    rsAtomicInc(&reds[in.r]);
    rsAtomicInc(&greens[in.g]);
    rsAtomicInc(&blues[in.b]);

    uchar l = round(0.299f*in.r + 0.587f*in.g + 0.114f*in.b);
    rsAtomicInc(&lumas[l]);
}

int32_t RS_KERNEL norm(int32_t in) {
    return round((in - minVal) * (float)(newMaxVal - newMinVal)/(maxVal - minVal) + newMinVal);
}


#pragma rs reduce(minMax) \
  initializer(initMinMax) \
  accumulator(accMinMax) \
  combiner(combMinMax)

static void initMinMax(int2 *accum) {
    accum->x = INT_MAX;
    accum->y = INT_MIN;
}

static void accMinMax(int2 *accum, int32_t val) {
    accum->x = min(val, accum->x);
    accum->y = max(val, accum->y);
}

static void combMinMax(int2 *accum, const int2* val) {
    accum->x = min(val->x, accum->x);
    accum->y = max(val->y, accum->y);
}