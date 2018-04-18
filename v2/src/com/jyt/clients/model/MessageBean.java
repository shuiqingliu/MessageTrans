package com.jyt.clients.model; /**
 * Created by qingliu on 1/24/18.
 */

import java.util.Date;

/**
 * Class Bean.MessageBean
 * 用来存储 json 解析出来的消息内容*/
public class MessageBean{

    //message property
    private String type;
    private String from;
    private String to;
    private String content;
    private Date   time;

    public MessageBean(){

    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
