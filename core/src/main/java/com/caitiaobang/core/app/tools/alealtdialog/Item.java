package com.caitiaobang.core.app.tools.alealtdialog;

/**
 * ============================================
 * 描  述：
 * 包  名：com.lgd.conmoncore.tools.dialog
 * 类  名：Item
 * 创建人：lgd
 * 创建时间：2018/11/16 13:46
 * ============================================
 **/

import android.graphics.drawable.Drawable;

/**
 * Created by raphaelbussa on 19/01/16.
 */
public class Item {

    private int id;
    private String title;
    private Drawable icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}