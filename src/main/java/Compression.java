import java.io.*;
import java.util.Arrays;
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
                String outputFileName = fileName + ".jaycomp";
                File inputFile = new File("D:\\workspace_jdea\\compress\\src\\main\\resources\\" + fileName);
                File outputFile = new File("D:\\workspace_jdea\\compress\\src\\main\\resources\\" + outputFileName);
                if (inputFile.exists() && !outputFile.exists()) {
                    try {
                        outputFile.createNewFile();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(inputFile));
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                        byte[] buffer = new byte[100];
                        Arrays.fill(buffer, (byte) -1);
                        Huffman huffman = new Huffman();
                        int len;
                        while ((len = bufferedInputStream.read(buffer)) != -1) {
                            //文件为空时
                            if (len == 0) {
                                break;
                            }
                            huffman.generateFrequency(buffer);
                        }
                        huffman.structureHuffmanTree();
                        /**
                         * 记得重置指针
                         */
                        while (bufferedInputStream.read(buffer) != -1) {
                            //文件为空时
                            if (len == 0) {
                                break;
                            }
                        }
                        byte[] code = huffman.huffmanEncoding(buffer);
                        bufferedOutputStream.write(code);
                        bufferedOutputStream.flush();
                        /**
                         * 调试用
                         */
                        for (byte b : code) {
                            System.out.println(b);
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
                File inputFile = new File("D:\\workspace_jdea\\compress\\src\\main\\resources\\" + fileName);
                if (fileName.endsWith(".jaycomp") && inputFile.exists()) {
                    String outputFileName = fileName.substring(0, fileName.length() - 8);
                    File outputFile = new File("D:\\workspace_jdea\\compress\\src\\main\\resources\\" + outputFileName);
                    if (!outputFile.exists()) {
                        try {
                            outputFile.createNewFile();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(inputFile));
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                            byte[] buffer = new byte[100];
                            Arrays.fill(buffer, (byte) -1);
                            Huffman huffman = new Huffman();
                            //获取频率表大小
                            int[] mapNumByte = new int[4];
                            mapNumByte[0] = bufferedInputStream.read();
                            mapNumByte[1] = bufferedInputStream.read();
                            mapNumByte[2] = bufferedInputStream.read();
                            mapNumByte[3] = bufferedInputStream.read();
                            long mapNum = FileUtil.unsignedByteToInt(mapNumByte) / 5;
                            //读取频率表
                            for (int i = 0; i < mapNum; i++) {
                                byte key = (byte) bufferedInputStream.read();
                                byte[] values = new byte[4];
                                values[0] = (byte) bufferedInputStream.read();
                                values[1] = (byte) bufferedInputStream.read();
                                values[2] = (byte) bufferedInputStream.read();
                                values[3] = (byte) bufferedInputStream.read();
                                int value = FileUtil.byteToInt(values);
                                huffman.frequency.put(key, value);
                            }
                            //生成哈夫曼树
                            huffman.structureHuffmanTree();
                            int len;
//                            while ((len = bufferedInputStream.read(buffer)) != -1) {
//                                //文件为空时
//                                if (len == 0) {
//                                    break;
//                                }
//                            }
                            while ((len = bufferedInputStream.read(buffer)) != -1) {
                                //文件为空时
                                if (len == 0) {
                                    break;
                                }
                                bufferedOutputStream.write(huffman.huffmanDecoding(buffer));
                            }
                            bufferedOutputStream.flush();
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