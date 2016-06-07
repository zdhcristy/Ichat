package com.example.ichat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.example.ichat.ChatListView;
import com.example.ichat.R;
import com.example.ichat.R.id;
import com.example.ichat.R.layout;
import com.example.ichat.bean.FindItem;
import com.example.ichat.util.ImageLoader;

public class FindAdapter extends BaseAdapter implements OnScrollListener{

    private List<FindItem> mList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    public static String[] URLS;
    private int mStart,mEnd;
    private boolean mFirstIn;

    public FindAdapter(Context context, List<FindItem> data, ListView listView) {
        this.mList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i=0; i< data.size(); i++){
            URLS[i] = data.get(i).newsIconUrl;
        }
        mFirstIn = true;
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsViewHolder viewHolder;
        if (convertView == null ){
            viewHolder = new NewsViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_find_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.lv_find_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.lv_find_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.lv_find_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (NewsViewHolder) convertView.getTag();
        }
        String url = mList.get(position).newsIconUrl;
        viewHolder.imageView.setTag(url);
        mImageLoader.getImageByAsyncTask(viewHolder.imageView,url);
        viewHolder.title.setText(mList.get(position).newsTitle);
        viewHolder.content.setText(mList.get(position).newsContent);

        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	
    	if (scrollState == SCROLL_STATE_IDLE){
            //加载可见项
    		if(mEnd<=URLS.length){
    			mImageLoader.loadImages(mStart, mEnd);
    		}
        }else {
            //停止所有任务
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, 
    		int visibleItemCount, int totalItemCount) {
        
    	mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        //只有在第一次显示被调用
        if(mFirstIn && visibleItemCount > 0){
            mImageLoader.loadImages(mStart, mEnd);
            mFirstIn = false;
        }
    }
   
    public class NewsViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView content;
    }
}
