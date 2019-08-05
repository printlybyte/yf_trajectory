package com.caitiaobang.core.app.tools.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.caitiaobang.core.R;
import com.caitiaobang.core.app.utils.GlideRoundTransform;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by qin on 2018/5/5.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        RequestOptions options = new RequestOptions()
//                .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, 5)).placeholder(com.lgd.conmoncore.R.drawable.ic_eletric_fan)
//                //加载成功之前占位图
//                .error(com.lgd.conmoncore.R.drawable.ic_loading)
//                //加载错误之后的错误图
//                .override(400, 400)
//                //指定图片的尺寸
//                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .centerCrop()
////                .skipMemoryCache(true)	//跳过内存缓存
////                .diskCacheStrategy(DiskCacheStrategy.ALL)	//缓存所有版本的图像
////                .diskCacheStrategy(DiskCacheStrategy.NONE)	//跳过磁盘缓存
////                .diskCacheStrategy(DiskCacheStrategy.DATA)	//只缓存原来分辨率的图片
////                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)	//只缓存最终的图片
//                ;
//        Glide.with(context)
//                .load(path)
//                .dontAnimate()
//                .into(imageView);

        Glide.with(context).load(path).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).placeholder(R.drawable.ic_glide_placeholder)
                .dontAnimate().error(R.drawable.ic_glide_error).dontAnimate().into(imageView );


    }
}
