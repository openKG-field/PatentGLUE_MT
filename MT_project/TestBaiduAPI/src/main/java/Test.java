import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        String transResult = TranslateUtil.getTransResult("浮箱堰", "auto", "en");
        System.out.println(transResult);
    }
}