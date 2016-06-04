package com.example.ichat.bean;

import android.widget.ImageView;
import android.widget.TextView;

public class ChatItem {
	
	public int pictureId;
	public String content;
	
	public int getPictureId() {
		return pictureId;
	}
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
