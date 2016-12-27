package bayes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {

    // 第一维是y取值,第二维是特征数
    static Map<String,Double>[][] probability;
    static Map<String, Double> yProbability;
    static Map<String, Integer> labelIndexMap;
    static Map<Integer, String> indexLabelMap;

    final static private String TRANS_DATA_PATH = "./src/main/resources/bayes_train_data";

    public static void main(String[] args) throws Exception {
        int ySize = 2, featureSize = 4;
        buildProbability(ySize, featureSize);
        System.out.println(test("Rain,Cool,Normal,Weak", ySize, featureSize));
    }


    static private BufferedReader buildBufferedReader(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
    }

    static private String test(String data, int ySize, int featureSize){
        String[] slices = data.split(",");
        Double maxLabelPro = -1D;
        String maxLabel = null;
        for(int i=0;i!=ySize;i++){
            Double res = yProbability.get(indexLabelMap.get(i));
            for(int j=0;j!=featureSize;j++){
                Double v = probability[i][j].get(slices[j]);
                res *= v;
            }
            if(res > maxLabelPro){
                maxLabel = indexLabelMap.get(i);
                maxLabelPro = res;
            }
        }
        return maxLabel;
    }

    static private void buildProbability(int ySize, int featureSize) throws IOException {

        probability = new HashMap[ySize][featureSize];
        yProbability = new HashMap<>();
        Map<String, Integer> yCount = new HashMap<>();
        Map<String, Integer>[][] xCount = new HashMap[ySize][featureSize];

        labelIndexMap = new HashMap<>();
        indexLabelMap = new HashMap<>();

        int size = 0;
        try (BufferedReader bf = buildBufferedReader(TRANS_DATA_PATH);) {
            String thisRow;
            while ((thisRow = bf.readLine()) != null) {
                size += 1;
                String[] slices = thisRow.split(",");

                String label = slices[featureSize];
                if(labelIndexMap.get(label) == null){
                    labelIndexMap.put(label, labelIndexMap.size());
                    indexLabelMap.put(indexLabelMap.size(), label);
                }

                for(int i=0;i!=featureSize;i++){
                    int labelIndex = labelIndexMap.get(label);
                    if(xCount[labelIndex][i] == null){
                        xCount[labelIndex][i] = new HashMap<>();
                    }
                    Integer count = xCount[labelIndex][i].get(slices[i]);
                    if(count == null) {
                        xCount[labelIndex][i].put(slices[i], 1);
                    }else{
                        xCount[labelIndex][i].put(slices[i], count+1);
                    }
                }
                Integer count = yCount.get(label);
                if(count==null){
                    yCount.put(label, 1);
                }else{
                    yCount.put(label, count+1);
                }
            }
        }

        for(Map.Entry<String, Integer> entry: yCount.entrySet()){
            String key = entry.getKey();
            yProbability.put(key, entry.getValue()*1.0/size);
        }

        for(int i=0;i!=ySize;i++){
            for(int j=0;j!=featureSize;j++){
                Map<String, Integer> cMap = xCount[i][j];
                Integer labelCount = yCount.get(indexLabelMap.get(i));
                Map<String, Double> xProMap = new HashMap<>();
                for(Map.Entry<String, Integer> entry: cMap.entrySet()){
                    xProMap.put(entry.getKey(), entry.getValue()*1.0/labelCount);
                }
                probability[i][j] = xProMap;
            }
        }

    }

}
