package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.User;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-09
 * Time: 16:19
 * FIXME
 */
public interface IInfoDataActivity extends BaseInterface {
    void backHeadSuccess();

    void backUpdateDataSuccess();

    void backDataSuccess(User mUser);
}
