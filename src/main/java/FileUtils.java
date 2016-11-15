import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FileUtils {

    final static private String TRANS_DATA_PATH = "./src/main/resources/train_data";

    final static private String TEST_DATA_PATH = "./src/main/resources/test_data";

    static Map<String, List<String>> header2Field = new HashMap<>();
    static Map<String, Integer> header2IndexMap = new HashMap<>();
    static Map<Integer, String> header2StrMap = new HashMap<>();

    static private BufferedReader buildBufferedReader(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
    }

    static public List<List<String>> readTrainData() throws IOException {
        try (BufferedReader bf = buildBufferedReader(TRANS_DATA_PATH);) {
            String thisRow;
            List<List<String>> data = new ArrayList<>();
            Boolean first = true;
            while ((thisRow = bf.readLine()) != null) {
                String[] slices = deleteFirstCol(thisRow.split(","));
                if(first){
                    for(int i=0;i!=slices.length;i++){
                        header2IndexMap.put(slices[i], i);
                        header2StrMap.put(i, slices[i]);
                    }
                    first = false;
                    continue;
                }
                String[] newSlices = discrete(slices);
                updateField(newSlices);
                data.add(Arrays.asList(newSlices));
            }
            return data;
        }
    }

    static public List<List<String>> readTestData() throws IOException {
        try (BufferedReader bf = buildBufferedReader(TEST_DATA_PATH);) {
            String thisRow;
            List<List<String>> data = new ArrayList<>();
            while ((thisRow = bf.readLine()) != null) {
                String[] slices = deleteFirstCol(thisRow.split(","));
                String[] newSlices = discrete(slices);
                updateField(newSlices);
                data.add(Arrays.asList(newSlices));
            }
            return data;
        }
    }

    static private String[] deleteFirstCol(String[] slices){
        String[] res = new String[slices.length-1];
        for(int i=1;i!=slices.length;i++){
            res[i-1] = slices[i];
        }
        return res;
    }

    static void updateField(String[] slices){
        for(int i=0;i!=slices.length;i++){
            String header = header2StrMap.get(i);
            List<String> headerField = header2Field.get(header);
            String value = slices[i];
            if(headerField == null){
                headerField = new ArrayList<>();
            }
            if(!headerField.contains(value)){
                headerField.add(value);
            }
            header2Field.put(header, headerField);
        }
    }

    // 进行离散化
    static private String[] discrete(String[] slices){
        return slices;
    }

}
