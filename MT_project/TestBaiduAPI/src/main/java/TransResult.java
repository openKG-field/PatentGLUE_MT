import java.util.List;

/**
 *  翻译结果 实体
 */
public class TransResult {
    /**
     *翻译源语言
     */
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransData> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TransData> trans_result) {
        this.trans_result = trans_result;
    }

    /**
     *译文语言
     */
    private String to;
    /**
     *翻译结果
     */
    private List<TransData> trans_result;
}