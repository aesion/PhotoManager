package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.CollectionTitle;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-10
 * Time: 17:16
 * FIXME
 */
public interface ICollectionFragment extends BaseInterface {

    void updateCollectionData(List<CollectionTitle> CollectionTitleList);

}
