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
//        //��ȡ�������ڴ�
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //���û����С
        int cacheSize = maxMemory/4;
        mCaches = new LruCache<String , Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //��ÿ�δ��뻺��ʱ����,��ȷͼƬ��С
                return value.getByteCount();
            }
        };
    }
    //��ͼƬ���뻺��
    public void storeBitmapToCache(String url, Bitmap bitmap){
        if (getBitmapFromCache(url) == null){
            mCaches.put(url, bitmap);
        }
    }
    //�ӻ���ȡ����Ƭ
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    public void getImageByAsyncTask(ImageView imageView, String url){
        //�ӻ�����ȡ����Ӧ��ͼƬ
        Bitmap bitmap = getBitmapFromCache(url);
        //���������û�У������ȥ����
        if (bitmap == null){
            imageView.setImageResource(R.drawable.ic_launcher);
        }else {
            //���������ͼƬ��ֱ�Ӹ�ImageView����
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
    //���شӻ�����ʼ������������ͼƬ
    public void loadImages(int start, int end){
    	
        for (int i=start; i<end; i++){
            String url = FindAdapter.URLS[i];
            //�ӻ�����ȡ����Ӧ��ͼƬ
            Bitmap bitmap = getBitmapFromCache(url);
            if (bitmap == null){
                //���������û�У������ȥ����
                ImageAsyncTask task = new ImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                //���������ͼƬ��ֱ�Ӹ�ImageView����
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
            //�������л�ȡͼƬ
            Bitmap bitmap = getBitmapFromURL(params[0]);
              //�����ڻ����ͼƬ���뻺��
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
