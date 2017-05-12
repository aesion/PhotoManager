package com.pulamsi.photomanager.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:16
 * FIXME
 */
public class Gallery implements Serializable {


    private String id;// id
    private String title;// 标题
    private String content;// 内容
    private Integer status;// 状态
    // 当前的状态，枚举enum(’0’,’1’,’2)值，0为已发表，1为草稿，2为私人内容(不会被公开)，默认为0。
    private Integer commentStatus;// 评论设置状态 0允许评价 1不允许评价
    private Integer orderList;// 排序
    private String url;// url地址 主图路径 展示图片
    private Integer postType;// 类型 0文字+图片 1文字 2视频
    private String video;
    private int sourceImgWidth;// 展示图长
    private int sourceImgHeight;// 展示图高

    Map<String, String> pasterImages;// 图片集
    private List<String> labels;// 标签组

    private String memberId;// 用户id
    private String userName;// 用户名//不可用
    private String memberName;// 用户名
    private String pasterCatId;// 分类id
    private String pasterCatName;// 分类名称
    private Integer commentCount;// 评论计数
    private Integer thumbs_upCount;// 点赞计数
    private Integer collectCount;// 收藏计数
    private long releaseTime; // 发布时间
    private boolean isGoods;//是否点赞
    private boolean isCollection;// 是否收藏

    private Integer isRecommended;// 是否推荐 0为否 1是

    private Integer auditStatus;// 审核状态 0 不通过 1通过 2待审核


    public Integer getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public boolean isGoods() {
        return isGoods;
    }

    public void setIsGoods(boolean isGoods) {
        this.isGoods = isGoods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getOrderList() {
        return orderList;
    }

    public void setOrderList(Integer orderList) {
        this.orderList = orderList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Map<String, String> getPasterImages() {
        return pasterImages;
    }

    public void setPasterImages(Map<String, String> pasterImages) {
        this.pasterImages = pasterImages;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUsernmae() {
        return userName;
    }

    public void setUsernmae(String userName) {
        this.userName = userName;
    }

    public String getPasterCatId() {
        return pasterCatId;
    }

    public void setPasterCatId(String pasterCatId) {
        this.pasterCatId = pasterCatId;
    }

    public String getPasterCatName() {
        return pasterCatName;
    }

    public void setPasterCatName(String pasterCatName) {
        this.pasterCatName = pasterCatName;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getThumbs_upCount() {
        return thumbs_upCount;
    }

    public void setThumbs_upCount(Integer thumbs_upCount) {
        this.thumbs_upCount = thumbs_upCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }



    public int getSourceImgWidth() {
        return sourceImgWidth;
    }

    public void setSourceImgWidth(int sourceImgWidth) {
        this.sourceImgWidth = sourceImgWidth;
    }

    public int getSourceImgHeight() {
        return sourceImgHeight;
    }

    public void setSourceImgHeight(int sourceImgHeight) {
        this.sourceImgHeight = sourceImgHeight;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", commentStatus=" + commentStatus +
                ", orderList=" + orderList +
                ", url='" + url + '\'' +
                ", postType=" + postType +
                ", video='" + video + '\'' +
                ", sourceImgWidth=" + sourceImgWidth +
                ", sourceImgHeight=" + sourceImgHeight +
                ", pasterImages=" + pasterImages +
                ", labels=" + labels +
                ", memberId='" + memberId + '\'' +
                ", userName='" + userName + '\'' +
                ", pasterCatId='" + pasterCatId + '\'' +
                ", pasterCatName='" + pasterCatName + '\'' +
                ", commentCount=" + commentCount +
                ", thumbs_upCount=" + thumbs_upCount +
                ", collectCount=" + collectCount +
                ", releaseTime=" + releaseTime +
                ", isGoods=" + isGoods +
                ", isCollection=" + isCollection +
                ", auditStatus=" + auditStatus +
                '}';
    }
}
