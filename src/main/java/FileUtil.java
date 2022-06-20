/**
 * @author 17291
 * @version 1.0
 * @className FileUtil
 * @description TODO 操作文件工具类
 * @date 2022/6/20 11:43
 */
public class FileUtil {
    /**
     * 4个byte转1个int
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {
        int i = (b[0] & 0xFF) << 24;
        i += (b[1] & 0xFF) << 16;
        i += (b[2] & 0xFF) << 8;
        i += (b[3] & 0xFF);
        return i;
    }

    /**
     * 1个int转4个byte
     * @param i
     * @return
     */
    public static byte[] intToByte(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (((i) >> 24) & 0xFF);
        b[1] = (byte) (((i) >> 16) & 0xFF);
        b[2] = (byte) (((i) >> 8) & 0xFF);
        b[3] = (byte) ((i) & 0xFF);
        return b;
    }

    /**
     * 4个int类型的无符号整形（0 - 255）转1个无符号整形（double）
     * @param b
     * @return
     */
    public static double unsignedByteToInt(int[] b) {
        double i = (b[0] & 0xFF) << 24;
        i += (b[1] & 0xFF) << 16;
        i += (b[2] & 0xFF) << 8;
        i += (b[3] & 0xFF);
        return i;
    }
}