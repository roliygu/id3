import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static final String labelHeader = "好瓜";

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

    static Double calculteGain(List<List<String>> data, String header){
        double hd = calculateExperienceEntropy(data);
        double chd = calculateConditionalEntropy(data, header);
        return hd - chd;
    }

    static Double calculateExperienceEntropy(List<List<String>> data){
        return calculateEntropy(data, labelHeader);
    }

    static Double calculateEntropy(List<List<String>> data, String header){
        Map<String, Double> entropyMap = calculateEntropyMap(data, header);
        double sum = 0.0f;
        for(Double entropy: entropyMap.values()){
            sum += entropy;
        }
        return -sum;
    }

    static Double calculateConditionalEntropy(List<List<String>> data, String header){
        Map<String, List<List<String>>> groupDataMap = groupByHeader(data, header);
        double sum = 0.0f;
        for(Map.Entry<String, List<List<String>>> groupDataEntry: groupDataMap.entrySet()){
            List<List<String>> groupData = groupDataEntry.getValue();
            Double _entropy = calculateEntropy(groupData, labelHeader);
            Double probability = groupData.size()*1.0 / FileUtils.totalDataNumber;
            sum += probability * _entropy;
        }
        return sum;
    }

    static private Map<String,Double> calculateEntropyMap(List<List<String>> data, String header){
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

    static Map<String, List<List<String>>> groupByHeader(List<List<String>> data, String header){
        Integer index = FileUtils.header2IndexMap.get(header);
        Map<String, List<List<String>>> res = new HashMap<>();
        for(List<String> row: data){
            String labelValue = row.get(index);
            if(!res.keySet().contains(labelValue)){
                List<List<String>> _data = new ArrayList<>();
                _data.add(row);
                res.put(labelValue, _data);
            }else{
                res.get(labelValue).add(row);
            }
        }
        return res;
    }

    static Map<String, List<List<String>>> groupByLabelValue(List<List<String>> data){
        return groupByHeader(data, labelHeader);
    }

}
