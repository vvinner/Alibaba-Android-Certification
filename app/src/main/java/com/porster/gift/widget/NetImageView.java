package com.porster.gift.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.porster.gift.R;
import com.porster.gift.widget.glide.GlideCircleTransform;
import com.porster.gift.widget.glide.GlideRoundedTransform;


/**
*   依赖于Glide的ImageView<p><p/>
 *  https://github.com/bumptech/glide
*   @author Porster
*   @time   16/11/1 10:37
**/
public class NetImageView extends ImageView{


    private RequestListener<String, GlideDrawable> mListener;

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NetImageView(Context context) {
        super(context);
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setListener(RequestListener<String, GlideDrawable> listener) {
        mListener = listener;
    }

    /**
     * 自定义参数构造,最后调用into方法即可,如果方法比较常用,请在本类中直接添加/修改
     * @param uri
     * @return  DrawableRequestBuilder<String>
     */
    public DrawableRequestBuilder<String> loadWithMe(String uri){
        DrawableRequestBuilder<String> builder=Glide.with(getContext()).load(uri);
        return builder;
    }

    /**
     * 加载图片
     * @param uri http://...;如果是本地图片请加前缀file://
     */
    public void load(String uri){
        load(uri, R.drawable.icon,null);
    }

    /**
     *
     * @param uri
     * @param defDrawable   默认图
     */
    public void load(String uri,int defDrawable){
        load(uri,defDrawable,null);
    }

    /**
     * 圆图
     * @param uri
     * @param defDrawable
     */
    public void loadWithCircle(String uri,int defDrawable){
        load(uri,defDrawable,new GlideCircleTransform(getContext()));
    }

    /**
     * 圆角图
     * @param uri
     * @param defDrawable
     */
    public void loadWithRounded(String uri,int defDrawable){
        load(uri,defDrawable,new GlideRoundedTransform(getContext()));
    }

    /**
     * 圆角图
     * @param uri
     * @param defDrawable
     * @param radius        指定圆角度数
     */
    public void loadWithRounded(String uri,int defDrawable,int radius){
        load(uri,defDrawable,new GlideRoundedTransform(getContext(),radius));
    }

    /**
     *
     * @param uri
     * @param defDrawable
     * @param mTransformation       自定义Bitmap
     */
    public void load(String uri, int defDrawable, BitmapTransformation mTransformation){
        DrawableRequestBuilder<String> builder=Glide.with(getContext()).load(uri).placeholder(defDrawable);
        //监听
        if(mListener!=null){
            builder.listener(mListener);
        }
        //特殊图像
        if(mTransformation!=null){
            builder.transform(mTransformation).into(this);
        }else{
            builder.into(this);
        }
    }

}
