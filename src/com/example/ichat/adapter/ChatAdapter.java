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
import com.example.ichat.bean.ChatItem;
import com.example.ichat.bean.FindItem;
import com.example.ichat.util.ImageLoader;

public class ChatAdapter extends BaseAdapter{

	private Context mContext;
    private List<ChatItem> mList;
    private LayoutInflater mInflater;

    public ChatAdapter(Context context, List<ChatItem> list) {
    	mContext = context;
    	mList = list;
        mInflater = LayoutInflater.from(context);
    }
    //ListView的数据发生改变需要更新
    public void onDataChange(List<ChatItem> newList){
    	mList = newList;
    	this.notifyDataSetChanged();
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
    	ChatViewHolder chatHolder;
        if (convertView == null ){
        	chatHolder = new ChatViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_chat_item, null);
            chatHolder.pic = (ImageView) convertView.findViewById(R.id.lv_chat_picture);
            chatHolder.content = (TextView) convertView.findViewById(R.id.lv_chat_content);
            convertView.setTag(chatHolder);
        }else {
        	chatHolder = (ChatViewHolder) convertView.getTag();
        }
        ChatItem chatItem = mList.get(position);
        chatHolder.pic.setImageResource(chatItem.getPictureId());
        chatHolder.content.setText(chatItem.getContent());

        return convertView;
    }

    public class ChatViewHolder {
    	
        ImageView pic;
        TextView content;
        
    }
}
