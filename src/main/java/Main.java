import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws IOException {

        List<List<String>> data = FileUtils.readTestData();
        System.out.println(data);

    }
}
