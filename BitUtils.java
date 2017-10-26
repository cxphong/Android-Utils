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
    public static Integer getBit(int i, int startPos, int length) {
        if (startPos < 0 || length < 0 || length > Integer.SIZE) {
            return null;
        }

        String b = "";
        int j = 0;

        do {
            int k = (i >> startPos) & 1;
            b += k;
            j++;
            startPos++;
        } while (j < length);

        String bits = new StringBuilder(b).reverse().toString();
        return binaryStringToInt(bits);
    }

    /**
     * Get bit of an integer
     * @param i an integer
     * @param position
     * @param value 0/1
     * @return
     */
    public static Integer setBit(int i, int position, int value) {
        if (value != 0 && value != 1) {
            return null;
        }

        if (position < 0 || position > Integer.SIZE) {
            return null;
        }

        String bits = toBinaryString(i);
        char[] b = bits.toCharArray();
        b[bits.length() - 1 - position] = (value + "").toCharArray()[0];

        return binaryStringToInt(new String(b));
    }
}
