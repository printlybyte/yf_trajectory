package com.yinfeng.yf_trajectory.alarm;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.caitiaobang.core.app.app.Latte;
import com.orhanobut.logger.Logger;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.alarm
 * 类  名：ContactUtils
 * 创建人：liuguodong
 * 创建时间：2020/2/19 16:42
 * ============================================
 **/
public class ContactUtils {
    /*保存电话*/
    //根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
    public static   void savePhone(String name, String phone) {
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phone);
        ContentResolver resolver = Latte.getApplicationContext().getContentResolver();
//        ContactsContract.Data.DISPLAY_NAME 查询 该电话的客户姓名
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.HAS_PHONE_NUMBER}, null, null, null); //从raw_contact表中返回display_name
        int count = cursor.getCount();
        if (count > 0) {
            if (cursor.moveToFirst()) {
                String hasPhone = cursor.getString(0);//查询该电话有没有人
                if (TextUtils.isEmpty(hasPhone)) {//没有该电话
                    insertPhone(name, phone);
                } else if ("0".equals(hasPhone)) { //没有该电话
                    insertPhone(name, phone);
                } else {
                    Log.i("testrx","该电话号码已存在");
                }

            }
        } else {
            insertPhone(name, phone);
        }

        cursor.close();
    }
    public static void insertPhone(String name, String phone) {
        if (TextUtils.isEmpty(name)) {
            name = phone;
        }

//        //插入raw_contacts表，并获取_id属性
//        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
//        ContentResolver resolver = this.getContext().getContentResolver();
//        ContentValues values = new ContentValues();
//        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
//        //插入data表
//        uri = Uri.parse("content://com.android.contacts/data");
//        //add Name
//        values.put("raw_contact_id", contact_id);
//        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/name");
//        values.put("data2", name);
//        values.put("data1", name);
//        resolver.insert(uri, values);
//        values.clear();
//        //add Phone
//        values.put("raw_contact_id", contact_id);
//        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
//        values.put("data2", "2");   //手机
//        values.put("data1", phone);
//        resolver.insert(uri, values);
//        values.clear();
        ContentValues values = new ContentValues(); //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = Latte.getApplicationContext().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);//获取id
        long rawContactId = ContentUris.parseId(rawContactUri); //往data表入姓名数据
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId); //添加id
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);//添加内容类型（MIMETYPE）
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);//添加名字，添加到first name位置
        Latte.getApplicationContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values); //往data表入电话数据
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        Latte.getApplicationContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        Log.i("testrx","联系人写入成功 : "+name +" "+ phone);


//        createAlarm();
    }
}
