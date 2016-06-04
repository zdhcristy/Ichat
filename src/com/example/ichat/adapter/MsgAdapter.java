package com.example.ichat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.ichat.R;
import com.example.ichat.R.id;
import com.example.ichat.bean.Msg;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MsgAdapter extends ArrayAdapter<Msg>{

	private int resourceId;
	private List<Msg> msgList = new ArrayList<Msg>();
	
	public MsgAdapter(Context context, int textViewResourceId,
			List<Msg> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Msg msg = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
			viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
			viewHolder.leftMsg= (TextView) view.findViewById(R.id.left_msg);
			viewHolder.rightMsg= (TextView) view.findViewById(R.id.right_msg);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		//如果是收到消息就显示左边消息布局，将右边布局隐藏
		if(msg.getType() == Msg.TYPE_RECEIVED){
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			viewHolder.leftMsg.setText(msg.getContent());
		//如果是发送消息就显示右边消息布局，将左边布局隐藏	
		}else if(msg.getType() == Msg.TYPE_SEND){
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			viewHolder.rightMsg.setText(msg.getContent());
		}
		return view;
	}
	class ViewHolder{
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		TextView leftMsg;
		TextView rightMsg;
	}
}
