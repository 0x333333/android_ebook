package com.jesusjzp.sanwen;

import java.io.BufferedReader;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.UI;
import com.jeremyfeinstein.slidingmenu.example.R;
import com.jeremyfeinstein.slidingmenu.example.fragments.ReadView;
import com.jesusjzp.sanwen.ArticleListAdapter.ViewHolder;

public class ArticleReadViewAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int position_str = 0;
	private CharBuffer buffer = CharBuffer.allocate(8000);
	private ArrayList<Integer> startPos = new ArrayList<Integer>(){{add(0);}};
	private ArrayList<Integer> strlength = new ArrayList<Integer>();
	private int curPage = 0;
	
	public ArticleReadViewAdapter(Context context, CharBuffer buffer) {
		inflater = LayoutInflater.from(context);
		this.buffer = buffer;
	}

	@Override
	public int getCount() {
		return 15;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		View layout = convertView;
		if (layout == null) {
			layout = inflater.inflate(R.layout.readview, null);
			holder.readView = (ReadView)layout.findViewById(R.id.read_view);
			holder.textView = (TextView)layout.findViewById(R.id.page);
			layout.setTag(holder);
		} else {
            holder = (ViewHolder) convertView.getTag();
        }
		Log.v("created new view from adapter:", position+"");
		
		// set readview
		Log.v("Page "+position+" starts from", startPos.get(position)+"");
		buffer.position(startPos.get(position));
		Spanned wordspan = Html.fromHtml(buffer.toString());
		holder.readView.setText(wordspan);
		holder.readView.resize();
		Log.v("str length", holder.readView.getCharNum()+"");
		int pageNum = position + 1;
		holder.textView.setText("- "+pageNum+" -");
		
		if (position == 0) {
			startPos.add(0);
		} else {
			startPos.add(pageNum, startPos.get(pageNum-1)+holder.readView.getCharNum());
		}
		
		
		return layout;
	}
	
	private class ViewHolder {
		public ReadView readView;
		public TextView textView;
	}
}
