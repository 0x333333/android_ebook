package com.jesusjzp.sanwensuibi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.jesusjzp.sanwensuibi.R;
import com.jesusjzp.db.DBManager;
import com.jesusjzp.entity.Article;
import com.jesusjzp.http.HttpGet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class ArticleListAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final Resources res;
    private Context mContext;
    private List<Article> articleList;
    private ArrayList<Article> arrayList;
    
    private ProgressDialog progressDialog;
    private Handler handler;
	private Runnable runnable;
	private String requestRes;
	
	private DBManager dbManager;
	
	private String id;
	private String url; 
	private String abstr;
	private String title; 
	private String status;
	private String type;
	
	@Override
	public int getCount() {
		return articleList.size();
	}

	@Override
	public Article getItem(int position) {
		return articleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    public ArticleListAdapter(Context context, List<Article> articleList) {
        inflater = LayoutInflater.from(context);
        res = context.getResources();
        this.mContext = context;
        this.articleList = articleList;
        this.arrayList = new ArrayList<Article>();
        this.arrayList.addAll(articleList);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
        	holder = new ViewHolder();
        	convertView = inflater.inflate(R.layout.item, null);
        	holder.text = (TextView) convertView.findViewById(R.id.text);
        	holder.abs = (TextView) convertView.findViewById(R.id.abstr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int colorResId = position % 2 == 0 ? R.color.even : R.color.odd;
        holder.text.setBackgroundColor(res.getColor(colorResId));
        holder.abs.setBackgroundColor(res.getColor(colorResId));
        holder.text.setText(formatTitle(articleList.get(position).getTitle()));
        holder.abs.setText(formatTitle(articleList.get(position).getAbstr()));
        
        convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				id = articleList.get(position).getId();
				url = articleList.get(position).getUrl();
				abstr = articleList.get(position).getAbstr();
				title = articleList.get(position).getTitle();
				status = articleList.get(position).getStatus();
				type = articleList.get(position).getType();
				dbManager = new DBManager(mContext);
				dbManager.openDatabase();
				dbManager.insertHistory(id, url, title, status, type, abstr);
				dbManager.closeDatabase();
				progressDialog = ProgressDialog.show(mContext,
						"正在下载", "稍等片刻，正在加载文章。");
				new Thread(runnable).start();
			}
        });
        
        runnable = new Runnable() {
			@Override
			public void run() {
				HttpGet httpGet = new HttpGet();
				String res = httpGet.getArticle(url);
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("res", res);
				msg.setData(data);
				handler.sendMessage(msg);
			}
        };
        
        handler = new Handler() {
        	public void handleMessage(Message msg) {
        		super.handleMessage(msg);
        		Bundle data = msg.getData();
				requestRes = data.getString("res");
				// close progress dialog
				progressDialog.dismiss();
				// send intent to next activity
				Intent intent = new Intent(mContext,
						ArticleContentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("res", requestRes);
				bundle.putString("id", id);
				bundle.putString("url", url);
				bundle.putString("abstr", abstr);
				bundle.putString("title", formatTitle(title));
				bundle.putString("status", status);
				bundle.putString("type", type);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
        	}
        };

        return convertView;
    }
    
    public String formatTitle(String title) {
    	title = title.replace("][", " ")
    			.replaceAll("\\[", "")
    			.replaceAll("\\]", "")
    			.replaceAll("【", "")
    			.replaceAll("】", "");
    	return title;
    }
    
    public void filter(String charText) {
    	charText = charText.toLowerCase(Locale.getDefault());
    	articleList.clear();
    	if (charText.length() == 0) {
    		articleList.addAll(arrayList);
		} else {
			for (Article article : arrayList) {
				if (article.getTitle().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					articleList.add(article);
				}
			}
		}
    	notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView text;
        TextView abs;
    }
}
