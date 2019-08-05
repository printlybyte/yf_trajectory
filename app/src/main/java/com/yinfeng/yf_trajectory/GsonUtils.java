package com.yinfeng.yf_trajectory;

import com.google.gson.Gson;

/**
 * ============================================
 * 描  述：gosn辅助类
 * 包  名：com.yinfeng.yf_trajectory
 * 类  名：GsonUtils
 * 创建人：liuguodong
 * 创建时间：2019/8/2 10:54
 * ============================================
 **/
public class GsonUtils {

        private static GsonUtils instance;
        private Gson gson;

        private GsonUtils() {
            gson = new Gson();
        }

        public static GsonUtils getInstance() {
            if (instance == null) {
                instance = new GsonUtils();
            }
            return instance;
        }

        public String toJson(Object object){
            return gson.toJson(object);
        }

        /**
         * 泛型：http://www.cnblogs.com/iyangyuan/archive/2013/04/09/3011274.html
         * @param json
         * @param classOfT
         * @return
         */
        public <T> T fromJson(String json,Class<T> classOfT){
            return gson.fromJson(json, classOfT);
        }

}
