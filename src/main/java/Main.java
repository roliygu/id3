import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        List<List<String>> transData = FileUtils.readTrainData();

        // build tree
        TreeNode root = buildTree();

        List<List<String>> testData = FileUtils.readTestData();
        for(List<String> data: testData){
            String label = TreeNode.getLabel(data, root);
            System.out.println(String.format("label is %s", label));
        }

    }

    static TreeNode buildTree(){
        return new TreeNode();
    }

    static Double calculateEntropy(Map<String, Double> entropyMap){
        double sum = 0.0f;
        for(Double entropy: entropyMap.values()){
            sum += entropy;
        }
        return -sum;
    }

    static Map<String,Double> calculateEntropyMap(List<List<String>> data, String header){
        Map<String, Integer> countMap = groupAndCount(data, header);
        Map<String, Double> entropyMap = new HashMap<>();
        for(Map.Entry<String, Integer> entry: countMap.entrySet()){
            Double probability = entry.getValue()*1.0/data.size();
            Double entropy = probability * (Math.log(probability)/Math.log(2));
            entropyMap.put(entry.getKey(), entropy);
        }
        return entropyMap;
    }

    static Map<String, Integer> groupAndCount(List<List<String>> data, String header){
        Integer index = FileUtils.header2IndexMap.get(header);
        Map<String, Integer> res = new HashMap<>();
        for(List<String> row: data){
            String thisValue = row.get(index);
            if(!res.keySet().contains(thisValue)){
                res.put(thisValue, 1);
            }else{
                res.put(thisValue, res.get(thisValue) + 1);
            }
        }
        return res;
    }

}
