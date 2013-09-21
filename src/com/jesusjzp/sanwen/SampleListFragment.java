package com.jesusjzp.sanwen;


import com.jeremyfeinstein.slidingmenu.example.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SampleListFragment extends ListFragment {
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem(R.string.home, R.drawable.ic_home));
		adapter.add(new SampleItem(R.string.mine, 0));
		adapter.add(new SampleItem(R.string.shoucangjia, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.lishijilu, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.article_list, 0));
		adapter.add(new SampleItem(R.string.dushijiqing, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.xiaoyuanchunse, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.jiatingluanlun, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.xingaijiaohuan, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.renqijiaohuan, R.drawable.ic_folder));
		adapter.add(new SampleItem(R.string.gudianwuxia, R.drawable.ic_folder));
//		adapter.add(new SampleItem(R.string.setting, 0));
//		adapter.add(new SampleItem(R.string.update, R.drawable.ic_download));
//		adapter.add(new SampleItem(R.string.delfavorite, R.drawable.ic_delete));
//		adapter.add(new SampleItem(R.string.delhistory, R.drawable.ic_delete));
//		adapter.add(new SampleItem(R.string.fontAndBg, R.drawable.ic_font));
//		adapter.add(new SampleItem(R.string.about, R.drawable.ic_about));
		setListAdapter(adapter);
	}

	private class SampleItem {
		public int tagId;
		public int iconRes;
		public SampleItem(int tagId, int iconRes) {
			this.tagId = tagId; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {
		
		private Context mContext;
		
		private class ViewHolder {
			public TextView title;
			public ImageView icon;
		}

		public SampleAdapter(Context context) {
			super(context, 0);
			mContext = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
				holder.title = (TextView) convertView.findViewById(R.id.row_title);
				holder.icon = (ImageView) convertView.findViewById(R.id.row_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.title.setText(getResources().getString(getItem(position).tagId));
			holder.icon.setImageResource(getItem(position).iconRes);
			if (getItem(position).iconRes == 0) {
				convertView.setBackgroundColor(Color.GRAY);
				holder.title.setTextColor(Color.WHITE);
				holder.icon.setVisibility(8); // 2 is gone
				Log.v("title", getResources().getString(getItem(position).tagId));
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int id = 0;
					Class<?> cls = null;
					String title = holder.title.getText().toString();
					if (title.equals(getString(R.string.home))) {
						cls = ExampleListActivity.class;
					} else if (title.equals(getString(R.string.shoucangjia))) {
						cls = ArticleListActivity.class;
						id = -2;
					} else if (title.equals(getString(R.string.lishijilu))) {
						id = -1;
						cls = ArticleListActivity.class;
					} else if (title.equals(getString(R.string.dushijiqing))) {
						cls = ArticleListActivity.class;
						id = 0;
					} else if (title.equals(getString(R.string.xiaoyuanchunse))) {
						cls = ArticleListActivity.class;
						id = 1;
					} else if (title.equals(getString(R.string.jiatingluanlun))) {
						cls = ArticleListActivity.class;
						id = 2;
					} else if (title.equals(getString(R.string.xingaijiaohuan))) {
						cls = ArticleListActivity.class;
						id = 3;
					} else if (title.equals(getString(R.string.renqijiaohuan))) {
						cls = ArticleListActivity.class;
						id = 4;
					} else if (title.equals(getString(R.string.gudianwuxia))) {
						cls = ArticleListActivity.class;
						id = 5;
					}
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("id", id+"");
					intent.putExtras(bundle);
					intent.setClass(mContext, cls);
					startActivity(intent);
				}
			});	
			return convertView;
		}

	}
}
