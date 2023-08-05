package com.timoleon.gamedirectory.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

public class CustomUtil {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String stripAccents(String s) {
        if (StringUtils.hasLength(s)) {
            s = Normalizer.normalize(s, Normalizer.Form.NFD);
            s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        }
        return s;
    }

    public static double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

    public static String padLeftZeros(String str, int n) {
        if (str == null) {
            return null;
        }
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }

    public static void printResult(final Logger log, String methodName, long startTime, long endTime) {
        long resultInMillis = endTime - startTime;
        long resultInSeconds = TimeUnit.MILLISECONDS.toSeconds(resultInMillis);

        log.debug(methodName + ": " + Long.toString(resultInMillis) + "ms" + " | " + resultInSeconds + "s");
    }

    public static boolean verifyContentTypeFromMagicBytes(byte[] body, String contentType) {
        switch (contentType) {
            case "application/pdf":
                return (
                    Byte.toUnsignedInt(body[0]) == 0x25 &&
                    Byte.toUnsignedInt(body[1]) == 0x50 &&
                    Byte.toUnsignedInt(body[2]) == 0x44 &&
                    Byte.toUnsignedInt(body[3]) == 0x46 &&
                    Byte.toUnsignedInt(body[4]) == 0x2D
                );
            case "image/jpeg":
                return (
                    Byte.toUnsignedInt(body[0]) == 0xFF &&
                    Byte.toUnsignedInt(body[1]) == 0xD8 &&
                    Byte.toUnsignedInt(body[2]) == 0xFF &&
                    Byte.toUnsignedInt(body[3]) == 0xDB ||
                    Byte.toUnsignedInt(body[0]) == 0xFF &&
                    Byte.toUnsignedInt(body[1]) == 0xD8 &&
                    Byte.toUnsignedInt(body[2]) == 0xFF &&
                    Byte.toUnsignedInt(body[3]) == 0xE0 &&
                    Byte.toUnsignedInt(body[4]) == 0x00 &&
                    Byte.toUnsignedInt(body[5]) == 0x10 &&
                    Byte.toUnsignedInt(body[6]) == 0x4A &&
                    Byte.toUnsignedInt(body[7]) == 0x46 &&
                    Byte.toUnsignedInt(body[8]) == 0x49 &&
                    Byte.toUnsignedInt(body[9]) == 0x46 &&
                    Byte.toUnsignedInt(body[10]) == 0x00 &&
                    Byte.toUnsignedInt(body[11]) == 0x01 ||
                    Byte.toUnsignedInt(body[0]) == 0xFF &&
                    Byte.toUnsignedInt(body[1]) == 0xD8 &&
                    Byte.toUnsignedInt(body[2]) == 0xFF &&
                    Byte.toUnsignedInt(body[3]) == 0xEE ||
                    Byte.toUnsignedInt(body[0]) == 0xFF &&
                    Byte.toUnsignedInt(body[1]) == 0xD8 &&
                    Byte.toUnsignedInt(body[2]) == 0xFF &&
                    Byte.toUnsignedInt(body[3]) == 0xE1 &&
                    Byte.toUnsignedInt(body[6]) == 0x45 &&
                    Byte.toUnsignedInt(body[7]) == 0x78 &&
                    Byte.toUnsignedInt(body[8]) == 0x69 &&
                    Byte.toUnsignedInt(body[9]) == 0x66 &&
                    Byte.toUnsignedInt(body[10]) == 0x00 &&
                    Byte.toUnsignedInt(body[11]) == 0x00
                );
            case "image/png":
                return (
                    Byte.toUnsignedInt(body[0]) == 0x89 &&
                    Byte.toUnsignedInt(body[1]) == 0x50 &&
                    Byte.toUnsignedInt(body[2]) == 0x4E &&
                    Byte.toUnsignedInt(body[3]) == 0x47 &&
                    Byte.toUnsignedInt(body[4]) == 0x0D &&
                    Byte.toUnsignedInt(body[5]) == 0x0A &&
                    Byte.toUnsignedInt(body[6]) == 0x1A &&
                    Byte.toUnsignedInt(body[7]) == 0x0A
                );
            default:
                return false;
        }
    }

    /* Log exception with its stack trace */
    public static void logException(Exception ex, Logger logger) {
        logger.error("Error while using callable statement", ex);
    }
}
