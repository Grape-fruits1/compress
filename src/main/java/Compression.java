import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author 17291
 * @version 1.0
 * @className Compression
 * @description TODO 压缩文件（除压缩算法外）
 * @date 2022/6/17 22:52
 */
public class Compression {
    /**
     * 压缩软件入口方法
     * @param args
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        switch (scan.nextInt()) {
            //压缩
            case 0 -> {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("../resources/a.txt"));
                byte[] buffer = new byte[100];
                int len;
                Haffman haffman = new Haffman();
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    haffman.generateFrequency(buffer);
                }
                haffman.structureHaffmanTree();
            }
            //解压
            case 1 -> {

            }
        }
    }
}