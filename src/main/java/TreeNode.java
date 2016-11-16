import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {

    // 如果是叶子节点的话,label就为"是"or"不是",内部节点则仍为null
    private String label = null;

    // signature告知了当前节点判断的依据,比如"色泽"or"根蒂"等
    private String signature = null;
    // children的key其实是signature对应数据的值域,比如"青绿"or"乌黑"等,而value则是真正的子节点
    private Map<String, TreeNode> children = new HashMap<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Map<String, TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeNode> children) {
        this.children = children;
    }

    static public String getLabel(List<String> data, TreeNode thisNode){
        if(thisNode.getLabel() != null){
            return thisNode.getLabel();
        }
        String header = thisNode.getSignature();
        Integer index = FileUtils.header2IndexMap.get(header);
        String dataValue = data.get(index);
        if(thisNode.getChildren()==null){
            throw new IllegalArgumentException("当前决策树无法判断该数据");
        }
        TreeNode child = thisNode.getChildren().get(dataValue);
        if(child == null){
            throw new IllegalArgumentException("当前决策树无法判断该数据");
        }
        return getLabel(data, child);
    }
}
