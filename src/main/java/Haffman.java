import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 17291
 * @version 1.0
 * @className Haffman
 * @description TODO 哈夫曼算法
 * @date 2022/6/17 22:53
 */
public class Haffman {
    HaffmanTree mainTree = null;
    HashMap<Byte, Integer> frequency = new HashMap<>();
    class HaffmanTree {
        //叶子节点符号
        Byte data = null;
        //节点权重
        int freq;
        //左子树
        HaffmanTree leftSubtree = null;
        //右子树
        HaffmanTree rightSubtree = null;

        /**
         * 叶子结点构造方法
         * @param data
         * @param freq
         */
        HaffmanTree(Byte data, int freq) {
            this.data = data;
            this.freq = freq;
        }

        /**
         * 合并两棵树
         * @param leftSubtree
         * @param rightSubtree
         */
        HaffmanTree(HaffmanTree leftSubtree, HaffmanTree rightSubtree) {
            this.leftSubtree = leftSubtree;
            this.rightSubtree = rightSubtree;
            freq = leftSubtree.freq + rightSubtree.freq;
        }
    }

    /**
     * 生成符号频率表
     * @param buffer
     */
    public void generateFrequency(byte[] buffer) {
        for (byte b : buffer) {
            if (frequency.containsKey(b)) {
                frequency.put(b, frequency.get(b) + 1);
            } else {
                frequency.put(b, 1);
            }
        }
    }
    /**
     * 构造哈夫曼树
     */
    public void structureHaffmanTree() {
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
        ArrayList<HaffmanTree> forest = new ArrayList<>();
        for (Map.Entry<Integer, Object> entry : comparedNode.entrySet()) {
            if (entry.getValue() instanceof Byte) {
                forest.add(new HaffmanTree((Byte) entry.getValue(), entry.getKey()));
            } else {
                for (Byte b : (ArrayList<Byte>) entry.getValue()) {
                    forest.add(new HaffmanTree(b, entry.getKey()));
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
                    mainTree = new HaffmanTree(mainTree, forest.get(0));
                    forest.remove(0);
                } else {
                    forest.set(0, new HaffmanTree(forest.get(0), forest.get(1)));
                    forest.remove(1);
                }
            }
            mainTree = new HaffmanTree(mainTree, forest.get(0));
        }
    }
}