import javax.annotation.processing.Generated;
import java.util.*;

/**
 * @author 17291
 * @version 1.0
 * @className Haffman
 * @description TODO 哈夫曼算法
 * @date 2022/6/17 22:53
 */
public class Haffman {
    private final HashMap<Byte, Integer> frequency;
    class HaffmanTree {
        byte data;
        int freq;
        HaffmanTree leftNode;
        HaffmanTree rightNode;
    }
    public Haffman() {
         frequency = new HashMap<>();
    }
    public void generateFrequency(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (frequency.containsKey(buffer[i])) {
                frequency.put(buffer[i], frequency.get(buffer[i]) + 1);
            } else {
                frequency.put(buffer[i], 1);
            }
        }
    }
    public void structurehaffmanTree() {
        //按频率大小排列数据
        TreeMap comparedNode = new TreeMap();
        Iterator<Map.Entry<Byte, Integer>> iterator = frequency.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Byte, Integer> entry = iterator.next();
            //如果频率相同
            if (comparedNode.containsKey(entry.getValue())) {
                //如果此频率之前只有一个数据
                if (comparedNode.get(entry.getValue()) instanceof Byte) {
                    ArrayList<Byte> dataList = new ArrayList<>();
                    dataList.add((Byte)comparedNode.get(entry.getValue()));
                    dataList.add(entry.getKey());
                    comparedNode.put(entry.getValue(), dataList);
                } else {
                    ArrayList<Byte> arrayList = (ArrayList)comparedNode.get(entry.getValue());
                    arrayList.add(entry.getKey());
                }
            }
        }
        //贪心算法构造哈夫曼树
    }
}