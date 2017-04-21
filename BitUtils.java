package common.android.fiot.androidcommon;

/**
 * Created by caoxuanphong on 3/23/17.
 */

public class BitUtils {
    /**
     * Covert int to string of binary
     * @param i
     * @return
     */
    public static String toBinaryString(int i) {
        return Integer.toBinaryString(i);
    }

    public static int binaryStringToInt(String b) {
        return Integer.parseInt(b, 2);
    }

    public static String binaryStringToHexString(String b) {
        return Integer.toHexString(Integer.parseInt(b, 2));
    }

    public static String getBit(int i, int startPos, int length) {
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
