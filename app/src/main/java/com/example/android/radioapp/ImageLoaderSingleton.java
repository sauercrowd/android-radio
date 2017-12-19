package com.example.android.radioapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by jotten on 14.12.17.
 */

public class ImageLoaderSingleton {
    private static ImageLoaderSingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private final int CACHE_SIZE = 20;

    private ImageLoaderSingleton(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache(){
                    private final LruCache<String, Bitmap> cache = new LruCache<>(CACHE_SIZE);

                    @Override
                    public Bitmap getBitmap(String url){
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap){
                        cache.put(url, bitmap);
                    }
                });

    }

    public static synchronized ImageLoaderSingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new ImageLoaderSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
