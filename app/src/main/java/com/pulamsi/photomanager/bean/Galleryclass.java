package com.pulamsi.photomanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-10
 * Time: 17:08
 * FIXME
 */
@Entity
public class Galleryclass {
    @Id
    private String id;
    @NotNull
    private String catName;// 分类名称
    @NotNull
    private String categoryNicename;// 分类别名
    private Integer orderList;// 排序
    private String categoryDescription;// 详细说明
    private String memberId;// 用户id
    private String userName;// 用户名
    private String parentId;// 上级分类id
    private String parentName;// 上级分类名称
    private Boolean isSelect;// 是否选中作为保留分类
    private Integer type;// 用户权限类型 0默认 1代理



    @Generated(hash = 1316675416)
    public Galleryclass(String id, @NotNull String catName, @NotNull String categoryNicename,
            Integer orderList, String categoryDescription, String memberId, String userName,
            String parentId, String parentName, Boolean isSelect, Integer type) {
        this.id = id;
        this.catName = catName;
        this.categoryNicename = categoryNicename;
        this.orderList = orderList;
        this.categoryDescription = categoryDescription;
        this.memberId = memberId;
        this.userName = userName;
        this.parentId = parentId;
        this.parentName = parentName;
        this.isSelect = isSelect;
        this.type = type;
    }

    @Generated(hash = 19167771)
    public Galleryclass() {
    }


    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryNicename() {
        return categoryNicename;
    }

    public void setCategoryNicename(String categoryNicename) {
        this.categoryNicename = categoryNicename;
    }

    public Integer getOrderList() {
        return orderList;
    }

    public void setOrderList(Integer orderList) {
        this.orderList = orderList;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Galleryclass{" +
                "id='" + id + '\'' +
                ", catName='" + catName + '\'' +
                ", categoryNicename='" + categoryNicename + '\'' +
                ", orderList=" + orderList +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", memberId='" + memberId + '\'' +
                ", userName='" + userName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }


    /**
     * 重写他的equals方法，才可以取出交集
     * @return
     */

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Galleryclass)obj).id);
    }
}
