package com.atguigu.qqzone.pojo;

import java.sql.Timestamp;

public class HostReply {
    private Integer id;
    private String content;
    private Timestamp hostReplyDate;
    private UserBasic author;       // M:1
    private Reply reply;            // 1:1

    public HostReply() {
    }

    public HostReply(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getHotsReplyDate() {
        return hostReplyDate;
    }

    public void setHotsReplyDate(Timestamp hotsReplyDate) {
        this.hostReplyDate = hotsReplyDate;
    }

    public UserBasic getAuthor() {
        return author;
    }

    public void setAuthor(UserBasic author) {
        this.author = author;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }
}
