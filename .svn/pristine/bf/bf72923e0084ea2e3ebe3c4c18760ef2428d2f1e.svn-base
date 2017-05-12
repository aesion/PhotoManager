package com.pulamsi.photomanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class SearchHistory {
    @Id(autoincrement = true)
    private Long id;

    @Unique//其值必须唯一
    private String searchContent;

    @Generated(hash = 495444001)
    public SearchHistory(Long id, String searchContent) {
        this.id = id;
        this.searchContent = searchContent;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchContent() {
        return this.searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }
}