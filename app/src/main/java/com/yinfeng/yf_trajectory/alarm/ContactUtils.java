package com.yinfeng.yf_trajectory.alarm;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.caitiaobang.core.app.app.Latte;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

    public static void savePhone(String name, String phone) {
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phone);
        ContentResolver resolver = Latte.getApplicationContext().getContentResolver();
//        ContactsContract.Data.DISPLAY_NAME 查询 该电话的客户姓名
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.HAS_PHONE_NUMBER, ContactsContract.Data.DISPLAY_NAME}, null, null, null); //从raw_contact表中返回display_name

        if (cursor == null) {
            insertPhone(name, phone);
            return;
        }
        int count = cursor.getCount();
        if (count > 0) {
            if (cursor.moveToFirst()) {
                String hasPhone = cursor.getString(0);  //查询该电话有没有人
                String hasName = cursor.getString(1);  //查询该电话有没有人
                if (TextUtils.isEmpty(hasPhone)) { //没有该电话
                    insertPhone(name, phone);
                } else if ("0".equals(hasPhone)) {  //没有该电话
                    insertPhone(name, phone);
                } else {
                    if (!hasName.equals(name)) {
                        Log.i("testcx", "检测到姓名不一致  原名称：" + hasName + "  网络：  " + name);
                        testDelete(hasName);
                        insertPhone(name, phone);
                    }
//                    Logger.i("该电话号码已存在");
                }

            }
        } else {
            insertPhone(name, phone);
        }

        cursor.close();
    }

//    public static void insertPhone(String name, String phone) {
//        if (TextUtils.isEmpty(name)) {
//            name = phone;
//        }
//        ContentValues values = new ContentValues(); //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
//        Uri rawContactUri = Latte.getApplicationContext().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);//获取id
//        long rawContactId = ContentUris.parseId(rawContactUri); //往data表入姓名数据
//        values.clear();
//        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId); //添加id
//        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);//添加内容类型（MIMETYPE）
//        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);//添加名字，添加到first name位置
//        Latte.getApplicationContext().getContentResolver().
//                insert(ContactsContract.Data.CONTENT_URI, values); //往data表入电话数据
//
//
//        values.clear();
//        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
//        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//        Latte.getApplicationContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
//        Logger.i("联系人写入成功 : " + name + " " + phone);
//    }



    /**
     * 添加联系人信息
     *
     */
    public static void insertPhone(String name, String phone) {
        try {
            //插入raw_contacts表，并获取_id属性
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            ContentResolver resolver = Latte.getApplicationContext() .getContentResolver();
            ContentValues values = new ContentValues();
            long contact_id = ContentUris.parseId(resolver.insert(uri, values));
            //插入data表
            uri = Uri.parse("content://com.android.contacts/data");
            //add Name
            values.put("raw_contact_id", contact_id);
            values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/name");
//        values.put("data2", "zdong");
            values.put("data1", name);
            resolver.insert(uri, values);
            values.clear();
            //add Phone
            values.put("raw_contact_id", contact_id);
            values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/phone_v2");
//        values.put("data2", "2");   //手机
            values.put("data1", phone);
            resolver.insert(uri, values);
//        values.clear();
//        //add email
//        values.put("raw_contact_id", contact_id);
//        values.put(ContactsContract.Data.MIMETYPE,"vnd.android.cursor.item/email_v2");
//        values.put("data2", "2");   //单位
//        values.put("data1", "xzdong@xzdong.com");
//        resolver.insert(uri, values);

        }catch (Exception e){
            Logger.i(e.getMessage()+"");
        }

    }



    public static void testDelete(String name) {
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = Latte.getApplicationContext().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name}, null);

        if (cursor!=null){
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
        }
        }
    }


}
