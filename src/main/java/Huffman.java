import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author 17291
 * @version 1.0
 * @className Haffman
 * @description TODO 哈夫曼算法
 * @date 2022/6/17 22:53
 */
public class Huffman {
    HuffmanTree mainTree = null;
    HashMap<Byte, Integer> frequency = new HashMap<>();
    class HuffmanTree {
        //叶子节点符号
        Byte data = null;
        //节点权重
        int freq;
        //左子树
        HuffmanTree leftSubtree = null;
        //右子树
        HuffmanTree rightSubtree = null;
        //双亲节点
        HuffmanTree parentNode = null;
        /**
         * 叶子结点构造方法
         * @param data
         * @param freq
         */
        HuffmanTree(Byte data, int freq) {
            this.data = data;
            this.freq = freq;
        }

        /**
         * 合并两棵树
         * @param leftSubtree
         * @param rightSubtree
         */
        HuffmanTree(HuffmanTree leftSubtree, HuffmanTree rightSubtree) {
            this.leftSubtree = leftSubtree;
            this.rightSubtree = rightSubtree;
            freq = leftSubtree.freq + rightSubtree.freq;
            leftSubtree.parentNode = this;
            rightSubtree.parentNode = this;
        }
    }

    /**
     * 生成符号频率表
     * @param buffer
     */
    public void generateFrequency(byte[] buffer) {
        for (int i = 0; i < buffer.length && buffer[i] != -1; i++) {
            if (frequency.containsKey(buffer[i])) {
                frequency.put(buffer[i], frequency.get(buffer[i]) + 1);
            } else {
                frequency.put(buffer[i], 1);
            }
        }
    }
    /**
     * 构造哈夫曼树
     */
    public void structureHuffmanTree() {
        //按频率大小排列数据
        TreeMap<Integer, Object> comparedNode = new TreeMap<>();
        for (Map.Entry<Byte, Integer> entry : frequency.entrySet()) {
            //如果频率相同
            if (comparedNode.containsKey(entry.getValue())) {
                //如果此频率之前只有一个数据
                if (comparedNode.get(entry.getValue()) instanceof Byte) {
                    ArrayList<Byte> dataList = new ArrayList<>();
                    dataList.add((Byte) comparedNode.get(entry.getValue()));
                    dataList.add(entry.getKey());
                    comparedNode.put(entry.getValue(), dataList);
                } else {
                    ArrayList<Byte> arrayList = (ArrayList<Byte>) comparedNode.get(entry.getValue());
                    arrayList.add(entry.getKey());
                }
            } else {
                comparedNode.put(entry.getValue(), entry.getKey());
            }
        }
        //贪心算法构造哈夫曼树
        ArrayList<HuffmanTree> forest = new ArrayList<>();
        for (Map.Entry<Integer, Object> entry : comparedNode.entrySet()) {
            if (entry.getValue() instanceof Byte) {
                forest.add(new HuffmanTree((Byte) entry.getValue(), entry.getKey()));
            } else {
                for (Byte b : (ArrayList<Byte>) entry.getValue()) {
                    forest.add(new HuffmanTree(b, entry.getKey()));
                }
            }
        }
        if (forest.size() == 0) {
            return;
        }
        mainTree = forest.get(0);
        forest.remove(0);
        if (forest.size() > 0) {
            while (forest.size() > 1) {
                if (mainTree.freq < forest.get(1).freq) {
                    mainTree = new HuffmanTree(mainTree, forest.get(0));
                    forest.remove(0);
                } else {
                    forest.set(0, new HuffmanTree(forest.get(0), forest.get(1)));
                    forest.remove(1);
                }
            }
            mainTree = new HuffmanTree(mainTree, forest.get(0));
        }
    }

    /**
     * 进行哈夫曼编码，广度优先遍历哈夫曼树
     */
    public byte[] huffmanEncoding(byte[] buffer) {
        Queue<HuffmanTree> treeQueue = new LinkedList<>();
        HuffmanTree nowNode = mainTree.rightSubtree;
        treeQueue.add(nowNode);
        treeQueue.add(mainTree.leftSubtree);
        List<Byte> outputCode = new ArrayList<>();
        //最终输出的不定长码文
        List<Byte> outputCodeByte = new ArrayList<>();
        outputCode.add((byte) 1);

        /**
         * 记得判断满数组时情况
         */

        for (byte b : buffer) {
            //读到文件末位
            if (b == -1) {
                //存入频率表及控制信息
                int mapSize = frequency.size() * 5;
                outputCodeByte.add((byte) (((mapSize) >> 24) & 0xFF));
                outputCodeByte.add((byte) (((mapSize) >> 16) & 0xFF));
                outputCodeByte.add((byte) (((mapSize) >> 8) & 0xFF));
                outputCodeByte.add((byte) ((mapSize) & 0xFF));
                for (Map.Entry<Byte, Integer> entry : frequency.entrySet()) {
                    outputCodeByte.add(entry.getKey());
                    outputCodeByte.add((byte) (((entry.getValue()) >> 24) & 0xFF));
                    outputCodeByte.add((byte) (((entry.getValue()) >> 16) & 0xFF));
                    outputCodeByte.add((byte) (((entry.getValue()) >> 8) & 0xFF));
                    outputCodeByte.add((byte) ((entry.getValue()) & 0xFF));
                }
                outputCodeByte.add((byte) 0);
                outputCodeByte.add((byte) 0);
                outputCodeByte.add((byte) 0);
                outputCodeByte.add((byte) 0);
                outputCodeByte.add((byte) 0);
                int charNum = 0;
                for (; charNum < outputCode.size(); charNum++) {
                    if ((charNum / 8 - (charNum - 1) / 8) == 1) {
                        outputCodeByte.add((byte) 0);
                    }
                    if (outputCode.get(charNum) == 0) {
                        outputCodeByte.set(charNum / 8 + 8 + mapSize, (byte) (outputCodeByte.get(charNum / 8 + 4) & (~(0x1 << (7 - (charNum % 8))))));
                    } else {
                        outputCodeByte.set(charNum / 8 + 8 + mapSize, (byte) (outputCodeByte.get(charNum / 8 + 4) | (0x1 << (7 - (charNum % 8)))));
                    }
                }
                //前4个字节指示空余位数
                outputCodeByte.set(mapSize + 4, (byte) (((7 - (charNum % 8)) >> 24) & 0xFF));
                outputCodeByte.set(mapSize + 5, (byte) (((7 - (charNum % 8)) >> 16) & 0xFF));
                outputCodeByte.set(mapSize + 6, (byte) (((7 - (charNum % 8)) >> 8) & 0xFF));
                outputCodeByte.set(mapSize + 7, (byte) ((7 - (charNum % 8)) & 0xFF));
                //包装类List转基本类型原生数组
                Byte[] outputCodeArray = new Byte[outputCodeByte.size()];
                outputCodeArray = outputCodeByte.toArray(outputCodeArray);
                byte[] outputCodeArrays = new byte[outputCodeByte.size()];
                for (int i = 0; i < outputCodeArray.length; i++) {
                    outputCodeArrays[i] = outputCodeArray[i];
                }
                return outputCodeArrays;
            }
            Stack<Byte> code = new Stack<>();
            //广度优先遍历找到字符并回溯产生哈夫曼编码
            while (!treeQueue.isEmpty()) {
                if (nowNode.data != null && nowNode.data == b) {
                    HuffmanTree backtrackingNode = nowNode;
                    while (backtrackingNode.parentNode != null) {
                        if (backtrackingNode.parentNode.leftSubtree == backtrackingNode) {
                            code.add((byte) 0);
                        } else {
                            code.add((byte) 1);
                        }
                        backtrackingNode = backtrackingNode.parentNode;
                    }
                    while (!code.empty()) {
                        outputCode.add(code.pop());
                    }
                }
                if (nowNode.rightSubtree != null && nowNode.leftSubtree != null) {
                    treeQueue.offer(nowNode.rightSubtree);
                    treeQueue.offer(nowNode.leftSubtree);
                }
                treeQueue.poll();
                nowNode = treeQueue.peek();
            }
        }
        return new byte[0];
    }
    public byte[] huffmanDecoding(byte[] buffer) {

        return new byte[0];
    }
}