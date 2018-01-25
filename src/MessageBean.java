/**
 * Created by qingliu on 1/24/18.
 */

import javax.xml.crypto.Data;
import java.io.Serializable;

/**
 * Class MessageBean
 * 用来存储 json 解析出来的消息内容*/
public class MessageBean implements Serializable{

    //message property
    private String type;
    private String from;
    private String to;
    private String content;
    private Data   time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Data getTime() {
        return time;
    }

    public void setTime(Data time) {
        this.time = time;
    }
}
