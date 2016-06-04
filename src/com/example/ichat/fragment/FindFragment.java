package com.example.ichat.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ichat.ChatListView;
import com.example.ichat.R;
import com.example.ichat.adapter.FindAdapter;
import com.example.ichat.bean.FindItem;

public class FindFragment extends Fragment{
	
	private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
	private ListView mListView;
    private FindAdapter mAdapter;
    private List<FindItem> newsBeanList;
    private FindItem newsBean;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find, container , false);
		mListView = (ListView) view.findViewById(R.id.lv_find);
		newsBeanList = new ArrayList<FindItem>();
		
		new NewsAsyncTask().execute(URL);
		return view;
	}

    /**
     * 实现网络的异步访问
     */
    class NewsAsyncTask extends AsyncTask<String, Void ,List<FindItem>>{

		@Override
        protected List<FindItem> doInBackground(String... params) {
            return getJsonDatas(params[0]);
        }

		@Override
        protected void onPostExecute(List<FindItem> newsBeen) {
            super.onPostExecute(newsBeen);
            mAdapter = new FindAdapter(getContext(), newsBeen, mListView);
            mListView.setAdapter(mAdapter);
        }
    }

    /**
     * 将传入的url返回的JSON格式数据转化为封装的NewsBean
     * @param url
     * @return
     */
    private List<FindItem> getJsonDatas(String url){
        
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0; i<jsonArray.length()-5; i++){
                jsonObject = jsonArray.getJSONObject(i);
                newsBean = new FindItem();
                newsBean.newsIconUrl = jsonObject.getString("picSmall");
                newsBean.newsTitle = jsonObject.getString("name");
                newsBean.newsContent = jsonObject.getString("description");
                newsBeanList.add(newsBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }

    /**
     * 通过输入流解析网页返回的数据
     * @param inputStream
     * @return
     */
    private String readStream(InputStream inputStream){
        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(inputStream,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line=br.readLine())!=null){
                result +=line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
