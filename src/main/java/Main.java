import java.util.List;

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

}
