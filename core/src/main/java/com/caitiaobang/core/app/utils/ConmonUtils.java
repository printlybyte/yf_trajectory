package com.caitiaobang.core.app.utils;

import android.text.TextUtils;

/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.core.app.utils
 * 类  名：ConmonUtils
 * 创建人：liuguodong
 * 创建时间：2019/8/5 14:03
 * ============================================
 **/
public class ConmonUtils {
    private ConmonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
    * 1 man 2 girl
    */
    public static int isSex(String idCard) {
        if (!TextUtils.isEmpty(idCard) && idCard.length() == 18) {
            if (Integer.parseInt(idCard.substring(16, 17)) / 2 == 0) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }
}
