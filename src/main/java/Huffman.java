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
            for (int i = 0; i < forest.size() - 1; i++) {
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
        for (byte b : buffer) {
            //读到文件末位
            if (b == -1) {
                int i;
                outputCodeByte.set(4, (byte) 0);
                for (i = 0; i < outputCode.size(); i++) {
                    if ((i / 8 - (i - 1) / 8) == 1) {
                        outputCodeByte.set(i / 8 + 4, (byte) 0);
                    }
                    if (outputCode.get(i) == 0) {
                        outputCodeByte.set(i / 8 + 4, (byte) (outputCodeByte.get(i / 8 + 4) & (~(0x1 << (7 - (i % 8))))));
                    } else {
                        outputCodeByte.set(i / 8 + 4, (byte) (outputCodeByte.get(i / 8 + 4) | (0x1 << (7 - (i % 8)))));
                    }
                }
                //前4个字节指示空余位数
                outputCodeByte.set(0, (byte) (((7 - (i % 8)) >> 24) & 0xFF));
                outputCodeByte.set(1, (byte) (((7 - (i % 8)) >> 16) & 0xFF));
                outputCodeByte.set(2, (byte) (((7 - (i % 8)) >> 8) & 0xFF));
                outputCodeByte.set(3, (byte) ((7 - (i % 8)) & 0xFF));
                //包装类List转基本类型原生数组
                Byte[] outputCodeArray = new Byte[outputCodeByte.size()];
                outputCodeArray = outputCodeByte.toArray(outputCodeArray);
                byte[] outputCodeArrays = new byte[outputCodeByte.size()];
                for (int j = 0; j < outputCodeArray.length; j++) {
                    outputCodeArrays[j] = outputCodeArray[j];
                }
                return outputCodeArrays;
            }
            //广度优先遍历找到字符并回溯产生哈夫曼编码
            while (nowNode != null) {
                Stack<Byte> code = new Stack<>();
                if (nowNode.data == b) {
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