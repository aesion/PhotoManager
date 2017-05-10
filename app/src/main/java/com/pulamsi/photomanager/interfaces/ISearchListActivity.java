package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.Gallery;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-06
 * Time: 17:36
 * FIXME
 */
public interface ISearchListActivity extends BaseInterface {


    void updateListData(List<Gallery> galleryList);

    void loadingListData(List<Gallery> galleryList);
}
