package eu.sisik.pixelproc

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.RenderScript
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import eu.sisik.pixelproc.histo.Histogram
import java_eu_sisik_pixelproc_histo.ScriptC_histo
import junit.framework.TestCase.*
import org.junit.*
import org.junit.runner.RunWith

/**
 * Copyright (c) 2019 by Roman Sisik. All rights reserved.
 */
// TODO: check possible floating point rounding issues

@RunWith(AndroidJUnit4::class)
class HistogramTest {

    lateinit var bitmap: Bitmap

    @Before
    fun setUp() {
        val stream =  InstrumentationRegistry.getContext().assets.open(BITMAP_PATH)
        bitmap = BitmapFactory.decodeStream(stream)
    }

    @Test
    fun generateKotlin() {
        val histogram = Histogram(bitmap)
        val (r, g, b, l) = histogram.generateKotlin(false)

        Assert.assertArrayEquals(RED, r)
        Assert.assertArrayEquals(GREEN, g)
        Assert.assertArrayEquals(BLUE, b)
        Assert.assertArrayEquals(LUMA, l)
    }

    @Test
    fun generateKotlinNormalized() {
        val histogram = Histogram(bitmap)
        val (r, g, b, l) = histogram.generateKotlin(true)

        Assert.assertArrayEquals(RED_NORM, r)
        Assert.assertArrayEquals(GREEN_NORM, g)
        Assert.assertArrayEquals(BLUE_NORM, b)
        Assert.assertArrayEquals(LUMA_NORM, l)
    }

    @Test
    fun generateCpp() {
        val histogram = Histogram(bitmap)
        val (r, g, b, l) = histogram.generateCpp(false)

        Assert.assertArrayEquals(RED, r)
        Assert.assertArrayEquals(GREEN, g)
        Assert.assertArrayEquals(BLUE, b)
        Assert.assertArrayEquals(LUMA, l)
    }

    @Test
    fun generateCppNormalized() {
        val histogram = Histogram(bitmap)
        val (r, g, b, l) = histogram.generateCpp(true)

        Assert.assertArrayEquals(RED_NORM, r)
        Assert.assertArrayEquals(GREEN_NORM, g)
        Assert.assertArrayEquals(BLUE_NORM, b)
        Assert.assertArrayEquals(LUMA_NORM, l)
    }

    @Test
    fun generateRs() {
        val rs = RenderScript.create(InstrumentationRegistry.getTargetContext())
        val histogram = Histogram(bitmap, rs)
        val (r, g, b, l) = histogram.generateRs(false)

        Assert.assertArrayEquals(RED, r)
        Assert.assertArrayEquals(GREEN, g)
        Assert.assertArrayEquals(BLUE, b)
        Assert.assertArrayEquals(LUMA, l)
    }

    @Test
    fun generateRsNormalized() {
        val rs = RenderScript.create(InstrumentationRegistry.getTargetContext())
        val histogram = Histogram(bitmap, rs)
        val (r, g, b, l) = histogram.generateRs(true)

        Assert.assertArrayEquals(RED_NORM, r)
        Assert.assertArrayEquals(GREEN_NORM, g)
        Assert.assertArrayEquals(BLUE_NORM, b)
        Assert.assertArrayEquals(LUMA_NORM, l)
    }


    companion object {

        private val BITMAP_PATH = "high_tatras.jpg"

        private val RED = intArrayOf(
            1576, 675, 555, 696, 807, 696, 877, 809, 782, 817, 818, 858, 791, 860,
            776, 837, 791, 821, 855, 868, 833, 840, 909, 926, 934, 855, 867, 914,
            878, 921, 882, 917, 899, 951, 966, 968, 994, 977, 952, 960, 956, 1019,
            984, 1036, 1004, 1037, 1117, 1039, 1122, 1107, 1143, 1163, 1139, 1160,
            1217, 1265, 1260, 1276, 1317, 1369, 1396, 1382, 1425, 1436, 1524, 1548,
            1525, 1602, 1641, 1768, 1694, 1758, 1846, 1882, 1987, 2056, 2091, 2228,
            2229, 2335, 2412, 2405, 2507, 2567, 2702, 2845, 2857, 2889, 2966, 3068,
            3112, 3158, 3135, 3188, 3160, 3252, 3319, 3300, 3446, 3481, 3537, 3686,
            3721, 3884, 4052, 4135, 4449, 4477, 4894, 4904, 5138, 5271, 5373, 5463,
            5572, 5559, 5756, 6037, 5938, 5866, 5876, 5706, 5898, 5748, 5856, 5673,
            5657, 5775, 5804, 5908, 5965, 6103, 6158, 6249, 6436, 6619, 6826, 6792,
            6944, 6802, 6858, 6733, 6672, 6317, 6027, 5812, 5475, 5133, 4822, 4650,
            4373, 4054, 4018, 4051, 3779, 3744, 3779, 3451, 3303, 3176, 2891, 2748,
            2734, 2453, 2389, 2365, 2224, 2160, 2149, 2121, 2001, 1973, 1935, 1905,
            1842, 1794, 1833, 1732, 1679, 1741, 1692, 1726, 1664, 1608, 1593, 1582,
            1620, 1539, 1415, 1385, 1418, 1387, 1386, 1316, 1340, 1248, 1252, 1192,
            1184, 1199, 1205, 1094, 1181, 1118, 1122, 1084, 1055, 1020, 1062, 1030,
            1059, 1037, 1053, 1002, 973, 1081, 1010, 939, 911, 950, 980,955, 1028,
            1143, 1174, 1312, 1220, 1498, 1774, 1961, 2254, 2697, 2012, 1932, 1902,
            2982, 2500, 2528, 2324, 1115, 2795, 2540, 3323, 4176, 2039, 1472, 1476,
            1503, 76021, 1273, 887, 738, 652, 493, 434, 2384
        )

        private val GREEN = intArrayOf(
            17656, 3931, 3570, 3134, 2687, 2156, 1871, 1622, 1451, 1260, 1145, 1082,
            962, 954, 910, 944, 847, 891, 814, 844, 808, 804, 804, 832, 759, 840, 794,
            780, 783, 798, 784, 840, 853, 815, 833, 852, 864, 817, 846, 834, 911, 866,
            878, 895, 931, 1014, 965, 970, 957, 993, 967, 966, 1045, 1032, 1132, 1047,
            1065, 1060, 1091, 1039, 1100, 1075, 1158, 1169, 1134, 1130, 1082, 1113,
            1177, 1169, 1209, 1143, 1166,1187, 1214, 1259, 1295, 1333, 1252, 1199,
            1366, 1365, 1424, 1316, 1408, 1505, 1452, 1524, 1525, 1612, 1580, 1666,
            1764, 1823, 1826, 1912, 1844, 1925, 1930, 1994, 2048, 2223, 2280, 2326,
            2372, 2339, 2557, 2593, 2536, 2508, 2620, 2549, 2560,2694, 2568, 2725,
            2758, 2845, 2835, 2801, 2799, 2992, 3256, 3278, 3474, 3513, 3857, 3916,
            3931, 3891, 3957, 4039, 4146, 4222, 4443, 4608, 4843, 5215, 5521, 6075,
            6577, 7038, 7564, 7771, 7558, 7579, 7417, 7211, 7060, 6666, 6528, 6103,
            5955,5768, 5732, 5670, 5390, 5544, 5347, 5197, 5102, 4923, 5028, 4851,
            4792, 4758, 4786, 4681, 4774, 4665, 4722, 4644, 4566, 4671, 4592, 4368,
            4174, 4099, 3950, 3944, 3894, 3730, 3543, 3508, 3480, 3293, 3181, 3230,
            3018, 2821, 2733, 2503, 2499,2206, 2066, 1946, 1838, 1657, 1618, 1539,
            1476, 1457, 1314, 1286, 1231, 1205, 1095, 1134, 1124, 1042, 1153, 942,
            985, 954, 914, 933, 849, 777, 732, 759, 840, 731, 635, 742, 789, 726,
            680, 628, 648, 731, 709, 860, 903, 926, 1142, 1467, 1694, 1807, 2349,
            1378, 1328, 2772, 2113, 2337, 3026, 4734, 2576, 2394, 5165, 76601,1581,
            867, 626, 463, 335, 726
        )

        private val BLUE = intArrayOf(
            94559, 16564, 16583, 15774, 14399, 12961, 11335, 9874, 8466, 7396, 6222, 5529,
            4867, 4345, 3863, 3510, 3228, 3069, 2932, 2745, 2488, 2393, 2382, 2183, 2215,
            2208, 2097, 2065, 1957, 1856, 1869, 1794, 1790, 1742, 1759, 1745, 1689, 1791,
            1725, 1764, 1672, 1663, 1758, 1686, 1716, 1623, 1559, 1684, 1633, 1625, 1480,
            1536, 1516, 1542, 1497, 1481, 1430, 1498, 1499, 1580, 1439, 1515, 1450, 1427,
            1502, 1416, 1440, 1379, 1433, 1430, 1460, 1424, 1395, 1325, 1343, 1331, 1293,
            1323, 1343, 1364, 1305, 1316, 1310, 1314, 1261, 1369, 1174, 1214, 1193, 1196,
            1128, 1229, 1161, 1203, 1169, 1181, 1205, 1208, 1189, 1191, 1168, 1224, 1169,
            1183, 1238, 1150, 1220, 1164, 1159, 1234, 1147, 1131, 1140, 1164, 1145, 1182,
            1231, 1142, 1084, 1118, 1161, 1169, 1170, 1199, 1180, 1254, 1200, 1253, 1184,
            1191, 1271, 1207, 1222, 1212, 1304, 1331, 1420, 1511, 1421, 1529, 1585, 1538,
            1589, 1611, 1507, 1588, 1582, 1659, 1705, 1648, 1735, 1735, 1835, 2023, 2042,
            2195, 2121, 2246, 2466, 2543, 2526, 2526, 2577, 2559, 2437, 2200, 2175, 2121,
            2106, 2213, 2086, 2128, 2082, 2143, 2232, 2197, 2244, 2285, 2300, 2384, 2348,
            2341, 2204, 2146, 2051, 2038, 1938, 1866, 1832, 1708, 1562, 1586, 1635, 1504,
            1466, 1471, 1449, 1468, 1502, 1455, 1425, 1375, 1336, 1278, 1275, 1244, 1196,
            1041, 971, 907, 846, 814, 773,780, 740, 794, 735, 735, 812, 727, 702, 673, 672,
            690, 696, 733, 706, 603, 545,469, 451, 383, 404, 416, 399, 368, 325, 372, 402,
            372, 350, 399, 436, 499, 521,578, 691, 747, 910, 1290, 1895, 81677, 7581, 11594,
            9653, 11601
        )

        private val LUMA = intArrayOf(
            457, 898, 681, 527, 636, 514, 573, 675, 681, 781, 858, 936, 1002, 1062, 1121,
            1215, 1287, 1300, 1348, 1541, 1387, 1506, 1499, 1489, 1538, 1547, 1472, 1587,
            1518, 1461, 1573, 1492, 1472, 1559, 1529, 1528, 1644, 1545, 1447, 1702, 1492,
            1608, 1697, 1548, 1581, 1591, 1625, 1586, 1648, 1503, 1606, 1520, 1552, 1638,
            1538, 1582, 1620, 1633, 1614, 1654, 1711, 1656, 1649, 1618, 1678, 1735, 1723,
            1597, 1815, 1717, 1827, 1776, 1767, 1904, 1899, 1959, 2044, 1993, 2099, 1998,
            2191, 2159, 2232, 2267, 2299, 2343, 2431, 2385, 2550, 2542, 2517, 2691, 2822,
            2743, 2746, 2807, 2945, 2877, 3010, 3028, 2926, 3014, 3000, 2973, 3047, 3118,
            3159, 3217, 3218, 3181, 3341, 3434, 3515, 3508, 3604, 3702, 3776, 3687, 3881,
            3913, 4036, 4303, 4235, 4547, 4555, 4620, 4709, 4784, 4988, 5111, 5238, 5389,
            5849, 6215, 6910, 7217, 7438, 7370, 7281, 7255, 6926, 6842, 6711, 6571, 6448,
            6632, 6345, 6290, 6267, 6014, 6003, 5849, 5658, 5607, 5468, 5570, 5401, 5433,
            5000, 4713, 4793, 4533, 4445, 4301, 4258, 4172, 3976, 3973, 3813, 3750, 3559,
            3463, 3444, 3204, 3047, 2763, 2763, 2668, 2529, 2387, 2223, 2213, 2048, 1816,
            1754, 1602, 1474, 1416, 1248, 1166, 1136, 1036, 938, 871, 830, 752, 796, 710,
            664, 655, 596, 655, 597, 551, 586, 592, 607, 570, 539, 557, 561, 545, 541, 504,
            525, 530, 476, 485, 516, 483, 498, 490, 512, 495, 531, 529, 556, 507, 535, 534,
            619, 706, 748, 835, 1105, 1587, 1558, 1870, 2283, 1543, 1618, 3337, 2212, 2396,
            4193, 3430, 2409, 4910, 1987, 76306, 1190, 780, 489, 363, 281, 59)

        private val RED_NORM = intArrayOf(
            4, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1,
            2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6,7,
            7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 11, 11, 12,
            12, 12, 14, 14, 15, 15, 16, 16, 17, 17, 17, 17, 18, 19, 19, 18, 18, 18, 18, 18,
            18, 18, 18, 18, 18, 18, 19, 19, 19, 20, 20, 21, 22, 21, 22, 21, 22, 21, 21, 20,
            19, 18, 17, 16, 15, 14, 13, 12, 12, 12, 11, 11, 11, 10, 10, 9, 8, 8, 8, 7, 7,
            7, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3,
            3,3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4, 5, 5, 6, 8, 5, 5, 5, 9, 7, 7, 6, 2, 8, 7, 10,
            13, 5, 4, 4, 4, 255, 3, 2, 1, 1, 0, 0, 7
        )

        private val GREEN_NORM = intArrayOf(
            58, 12, 11, 9, 8, 6, 5, 4, 4, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
            2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 3, 2, 2, 2, 3, 2, 3, 2, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3,3, 3, 3, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 7,
            7, 7, 7, 7, 8, 7, 7, 8, 7, 7, 8, 7, 8, 8, 8, 8, 8, 8, 9, 10, 10, 10, 11, 12,
            12, 12, 12, 12, 12, 13, 13, 14, 14, 15, 16, 17, 19, 21, 22, 24, 25, 24, 24,
            24, 23, 22, 21, 21, 19, 19, 18, 18, 18, 17, 17, 17, 16, 16, 15, 16, 15, 15,
            15, 15, 15, 15, 14, 15, 14, 14, 14, 14, 13, 13, 13, 12, 12, 12, 11, 11, 11,
            11, 10, 10, 10, 9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3,
            3, 2, 3, 2, 2, 2, 2, 2, 2,1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2,
            3, 4, 5, 5, 7, 3, 3, 8, 6, 7, 9, 15, 7, 7, 16, 255, 4, 2, 1, 0, 0, 1
        )

        private val BLUE_NORM = intArrayOf(
            255, 44, 44, 42, 38, 34, 30, 26, 22, 19, 16, 14, 12, 11, 10, 9, 8, 7, 7, 7,
            6, 6, 6, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            4, 3, 4,4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 3, 2, 3,2, 2, 3, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4,
            4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 2, 2, 2,2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 3, 4, 220,
            20, 30, 25, 31
        )

        private val LUMA_NORM = intArrayOf(
            1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 4, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 6, 6, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6,
            7, 6,7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 10, 9, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 11, 11, 10, 11, 11, 12, 12, 12, 12, 12, 12, 13, 13, 13, 14, 14,
            15, 15, 15, 16, 16, 16, 17, 17, 18, 19, 21, 23, 24, 25, 24, 24, 24, 23, 23, 22,
            22, 21, 22, 21, 21, 21, 20, 20, 19, 19, 19, 18, 18, 18, 18, 17, 16, 16, 15, 15,
            14, 14, 14, 13, 13, 13, 12, 12, 11, 11, 11, 10, 9, 9, 9, 8, 8, 7, 7, 7, 6, 6, 5,
            5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            1,2, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 3, 3, 5, 5, 6, 7, 5,
            5, 11, 7, 8, 14, 11, 8, 16, 6, 255, 4, 2, 1, 1, 1, 0
        )
    }
}