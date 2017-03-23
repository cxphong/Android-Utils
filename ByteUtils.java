package common.android.fiot.androidcommon;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by caoxuanphong on    4/28/16.
 */
public class ByteUtils {
    private static final String TAG = "ByteUtils";

    /**
     * Create byte array from list of integer
     *
     * Ex: ByteUtils.createByteArray(1,2,3,4,5,100, 500);
     * Result: [0x1, 0x2, 0x3, 0x4, 0x5, 0x64, 0xf4]
     *
     * @param numbers
     * @return
     */
    public static byte[] createByteArray(int ...numbers) {
        byte [] array = new byte[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            array[i] = (byte) numbers[i];
        }

        return array;
    }

    public static byte[] stringToByteArray(String string) {
        return string.getBytes();
    }

    public static byte[] integerToByteArray(int number) {
        return ByteBuffer.allocate(4).putInt(number).array();
    }

    public static byte[] longToByteArray(long number) {
        return ByteBuffer.allocate(8).putLong(number).array();
    }

    public static byte[] add2ByteArray(byte[] a, byte[] b) {
        if (a == null && b == null) return null;
        if (a == null && b != null) return b;
        if (a != null && b == null) return a;

        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    public static byte[] addByte(byte[] a, byte b) {
        byte[] c;
        if (a == null) {
            return new byte[] {b};
        } else {
            c = new byte[a.length + 1];
        }


        if (a != null) {
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(new byte[]{b}, 0, c, a.length, 1);
        } else {
            c[0] = b;
        }

        return c;
    }

    public static byte[] append(byte[] src, byte[] bytes, int startPos) {
        int j = 0;
        for (int i = startPos; i < startPos + bytes.length; i++) {
            src[i] = bytes[j++];
        }

        return src;
    }

    public static byte[] subByteArray(byte[] src, int startPos, int num) {
        int endPos = 0;

        if (startPos + num > src.length) {
            endPos = src.length;
        } else {
            endPos = startPos + num;
        }

        return Arrays.copyOfRange(src, startPos, endPos );
    }

    // the first byte the most significant
    public static long toLongFirstMostSignificant(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) + (bytes[i] & 0xff);
        }

        return value;
    }

    // the first byte the least significant
    public static long toLongFirstLeastSignificant(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value += ((long) bytes[i] & 0xffL) << (8 * i);
        }

        return value;
    }

    public static String toHexString(byte[] a) {
        if (a == null) return null;

        String[] s = new String[a.length];
        for (int  i = 0; i < a.length; i++) {
            s[i] = "0x" + Integer.toHexString((a[i] & 0xff));
        }

        return Arrays.toString(s);
    }

    public static String toIntegerString(byte[] a) {
        if (a == null) return null;

        return Arrays.toString(a);
    }
    
    public static boolean compare2Array(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) return false;
        
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) return false;
        }
        
        return true;
    }

    public static int getHi(byte b) {
        return (((b&0xff) & 0xf0) >> 4);
    }

    public static int getLo(byte b) {
        return (((b&0xff) & 0x0F));
    }

    public static int merge2Bytes(byte bHi, byte bLo) {
        return ((bHi&0xff) << 8) + (bLo&0xff);
    }

    public static String toString(byte[] b) {
        if (b == null) {
            return null;
        }

        return new String(b);
    }

    /**
     * Check @child is contain in @src
     * @param src
     * @param child
     * @return
     */
    public static boolean isContain(byte[] src, byte[] child) {
        if (src == null && child == null) return true;
        if (src == null || child == null) return false;

        if (src == child) return true;

        if (child.length > src.length) {
          return false;
        } else if (child.length == src.length) {
            for (int i = 0; i < src.length; i++) {
                if (src[i] != child[i]) {
                    return false;
                }
            }
            return true;
        } else {
            for (int i = 0; i < src.length; i++) {
                if (i > child.length - 1) return false;

                if (src[i] == child[i]) {
                    byte[] sub = subByteArray(src, i, child.length);
                    if (isContain(sub, child)) return true;
                }
            }
        }

        return false;
    }
}
