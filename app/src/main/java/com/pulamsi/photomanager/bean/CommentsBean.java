package com.pulamsi.photomanager.bean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-05
 * Time: 14:37
 * FIXME
 */
public class CommentsBean {

    private String id;
    private String comment_post_id;//贴id
    private String comment_author_ip;//评论者ip
    private String comment_date;//发布时间
    private String userId;//会员id
    private String comment_content;//评论的具体内容
    private Integer comment_approved;//状态
    private String comment_agent;//客户端消息
    private Integer comment_type;//类型
    private String comment_parent;//上级评论
    private String image;//头像
    private String name;//用户昵称
    private String goodsCount;//点赞数量
    private boolean isGoods;// 是否点赞


    public boolean isGoods() {
        return isGoods;
    }

    public void setIsGoods(boolean isGoods) {
        this.isGoods = isGoods;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment_post_id() {
        return comment_post_id;
    }

    public void setComment_post_id(String comment_post_id) {
        this.comment_post_id = comment_post_id;
    }

    public String getComment_author_ip() {
        return comment_author_ip;
    }

    public void setComment_author_ip(String comment_author_ip) {
        this.comment_author_ip = comment_author_ip;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public Integer getComment_approved() {
        return comment_approved;
    }

    public void setComment_approved(Integer comment_approved) {
        this.comment_approved = comment_approved;
    }

    public String getComment_agent() {
        return comment_agent;
    }

    public void setComment_agent(String comment_agent) {
        this.comment_agent = comment_agent;
    }

    public Integer getComment_type() {
        return comment_type;
    }

    public void setComment_type(Integer comment_type) {
        this.comment_type = comment_type;
    }

    public String getComment_parent() {
        return comment_parent;
    }

    public void setComment_parent(String comment_parent) {
        this.comment_parent = comment_parent;
    }


    @Override
    public String toString() {
        return "CommentsBean{" +
                "id='" + id + '\'' +
                ", comment_post_id='" + comment_post_id + '\'' +
                ", comment_author_ip='" + comment_author_ip + '\'' +
                ", comment_date='" + comment_date + '\'' +
                ", userId='" + userId + '\'' +
                ", comment_content='" + comment_content + '\'' +
                ", comment_approved=" + comment_approved +
                ", comment_agent='" + comment_agent + '\'' +
                ", comment_type=" + comment_type +
                ", comment_parent='" + comment_parent + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", goodsCount='" + goodsCount + '\'' +
                '}';
    }
}
