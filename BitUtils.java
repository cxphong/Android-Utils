package common.android.fiot.androidcommon;

/**
 * Created by caoxuanphong on 3/23/17.
 */

public class BitUtils {

    /**
     * Covert Integer to string of binary
     *
     * @param i
     * @return
     */
    public static String toBinaryString(int i) {
        return Integer.toBinaryString(i);
    }

    /**
     * Convert Binary string into Integer
     *
     * @param b
     * @return
     */
    public static int binaryStringToInt(String b) {
        return Integer.parseInt(b, 2);
    }

    /**
     * Convert Binary string into Hex string
     *
     * @param b
     * @return
     */
    public static String binaryStringToHexString(String b) {
        return Integer.toHexString(Integer.parseInt(b, 2));
    }

    /**
     * Get value of Integer with bit
     *
     * @param i
     * @param startPos
     * @param length
     * @return
     */
    public static String getBit(int i, int startPos, int length) {
        if (startPos < 0 || length < 0) {
            return null;
        }

        String b = "";
        int j = 0;

        do {
            int k = (i >> startPos) & 1;
            b += k;
            j ++;
            startPos ++;
        } while (j < length);

        return new StringBuilder(b).reverse().toString();
    }
}
