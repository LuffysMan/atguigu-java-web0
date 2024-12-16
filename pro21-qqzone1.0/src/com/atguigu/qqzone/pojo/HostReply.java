package com.atguigu.qqzone.pojo;

import java.sql.Date;

public class HostReply {
    private Integer id;
    private String content;
    private Date hotsReplyDate;
    private UserBasic author;       // M:1
    private Reply reply;            // 1:1

    public HostReply() {
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

    public Date getHotsReplyDate() {
        return hotsReplyDate;
    }

    public void setHotsReplyDate(Date hotsReplyDate) {
        this.hotsReplyDate = hotsReplyDate;
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
