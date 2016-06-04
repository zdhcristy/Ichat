package com.example.ichat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.example.ichat.adapter.FindAdapter;

public class ImageLoader {

    private LruCache<String , Bitmap> mCaches;
    private ListView mListView;
    private Set<ImageAsyncTask> mTask;

    public ImageLoader(ListView listView){
        mListView = listView;
        mTask = new HashSet<ImageAsyncTask>();
//        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置缓存大小
        int cacheSize = maxMemory/4;
        mCaches = new LruCache<String , Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存时调用,明确图片大小
                return value.getByteCount();
            }
        };
    }
    //将图片存入缓存
    public void storeBitmapToCache(String url, Bitmap bitmap){
        if (getBitmapFromCache(url) == null){
            mCaches.put(url, bitmap);
        }
    }
    //从缓存取出照片
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    public void getImageByAsyncTask(ImageView imageView, String url){
        //从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中没有，则必须去下载
        if (bitmap == null){
            imageView.setImageResource(R.drawable.ic_launcher);
        }else {
            //如果缓存有图片，直接给ImageView设置
            imageView.setImageBitmap(bitmap);
        }
    }

    public void cancelAllTasks(){
        if (mTask != null){
            for (ImageAsyncTask task : mTask){
                task.cancel(false);
            }
        }
    }
    //加载从滑动开始到结束的所有图片
    public void loadImages(int start, int end){
    	
        for (int i=start; i<end; i++){
            String url = FindAdapter.URLS[i];
            //从缓存中取出对应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            if (bitmap == null){
                //如果缓存中没有，则必须去下载
                ImageAsyncTask task = new ImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                //如果缓存有图片，直接给ImageView设置
            	ImageView imageView = (ImageView) mListView.findViewWithTag(url);
            		imageView.setImageBitmap(bitmap);
            }
        }
    }
    
    public Bitmap getBitmapFromURL(String url){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap>{

        private String mUrl;

        public ImageAsyncTask(String url) {
            mUrl = url;
        }
        
        @Override
        protected Bitmap doInBackground(String... params) {
            //从网络中获取图片
            Bitmap bitmap = getBitmapFromURL(params[0]);
              //将不在缓存的图片加入缓存
            if (bitmap != null){
                storeBitmapToCache(params[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }
}
