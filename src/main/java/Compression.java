import java.io.*;
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
                String fileName = scan.next();
                String outputFileName = fileName + ".comp";
                File inputFile = new File(fileName);
                File outputFile = new File(outputFileName);
                if (inputFile.exists() && !outputFile.exists()) {
                    try {
                        outputFile.createNewFile();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(new
                                FileInputStream("../resources/" + fileName));
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new
                                FileOutputStream("../resources/" + outputFileName));
                        byte[] buffer = new byte[100];
                        Huffman huffman = new Huffman();
                        while (bufferedInputStream.read(buffer) != -1) {
                            huffman.generateFrequency(buffer);
                        }
                        huffman.structureHuffmanTree();
                        while (bufferedInputStream.read(buffer) != -1) {
                            bufferedOutputStream.write(huffman.huffmanEncoding(buffer));
                        }
                        bufferedOutputStream.close();
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            //解压
            case 1 -> {
                String fileName = scan.next();
                File inputFile = new File(fileName);
                if (fileName.endsWith(".comp") && inputFile.exists()) {
                    String outputFileName = fileName.substring(0,fileName.length() - 5);
                    File outputFile = new File(outputFileName);
                    if (!outputFile.exists()) {
                        try {
                            outputFile.createNewFile();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(new
                                    FileInputStream("../resources/" + fileName));
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new
                                    FileOutputStream("../resources/" + outputFileName));
                            byte[] buffer = new byte[100];
                            Huffman huffman = new Huffman();
                            while (bufferedInputStream.read(buffer) != -1) {
                                huffman.generateFrequency(buffer);
                            }
                            huffman.structureHuffmanTree();
                            while (bufferedInputStream.read(buffer) != -1) {
                                bufferedOutputStream.write(huffman.huffmanDecoding(buffer));
                            }
                            bufferedOutputStream.close();
                            bufferedInputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                } else {
                    //抛出异常
                }

            }
        }
    }
}