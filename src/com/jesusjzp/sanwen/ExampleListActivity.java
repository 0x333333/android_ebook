package com.jesusjzp.sanwen;

import java.net.URLEncoder;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.crittercism.app.Crittercism;
import com.jeremyfeinstein.slidingmenu.example.R;
import com.jesusjzp.db.DBManager;

public class ExampleListActivity extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		getSupportActionBar().setTitle(R.string.app_name);

		this.addPreferencesFromResource(R.xml.main);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen screen,
			Preference pref) {
		Class<?> cls = null;
		String title = pref.getTitle().toString();
		int id = 0;
		if (title.equals(getString(R.string.shoucangjia))) {
			cls = ArticleListActivity.class;
			id = -2;
		} else if (title.equals(getString(R.string.lishijilu))) {
			id = -1;
			cls = ArticleListActivity.class;
		} else if (title.equals(getString(R.string.dushijiqing))) {
			cls = ArticleListActivity.class;
			id = 1;
		} else if (title.equals(getString(R.string.xiaoyuanchunse))) {
			cls = ArticleListActivity.class;
			id = 2;
		} else if (title.equals(getString(R.string.jiatingluanlun))) {
			cls = ArticleListActivity.class;
			id = 3;
		} else if (title.equals(getString(R.string.xingaijiaohuan))) {
			cls = ArticleListActivity.class;
			id = 4;
		} else if (title.equals(getString(R.string.renqijiaohuan))) {
			cls = ArticleListActivity.class;
			id = 5;
		} else if (title.equals(getString(R.string.gudianwuxia))) {
			cls = ArticleListActivity.class;
			id = 6;
		} else if (title.equals(getString(R.string.update))) {
//			Uri uriUrl = Uri.parse("http://ebooklyonfrance.github.io/version.html");
//			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
//			startActivity(launchBrowser);
			Toast.makeText(this, "当前应用已是最新版本。", Toast.LENGTH_SHORT).show();
			return true;
		} else if (title.equals(getString(R.string.delfavorite))) {
			id = 7;
			dialogShow(id);
			return true;
		} else if (title.equals(getString(R.string.delhistory))) {
			id = 8;
			dialogShow(id);
			return true;
		} else if (title.equals(getString(R.string.fontAndBg))) {
			Toast.makeText(this, "自定义字体功能正在拼命开发中！", Toast.LENGTH_SHORT).show();
			return true;
		} else if (title.equals(getString(R.string.about))) {
			new AlertDialog.Builder(this).setTitle(R.string.about)
					.setMessage(Html.fromHtml(getString(R.string.about_msg)))
					.show();
			return true;
		} 
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("id", id + "");
		intent.putExtras(bundle);
		intent.setClass(this, cls);
		startActivity(intent);
		return true;
	}

	public void clearData(int id) {
		DBManager dbManager = new DBManager(this);
		dbManager.openDatabase();
		if (id == 7) {
			dbManager.clearFAVORITE();
			Toast.makeText(this, "已成功清空收藏夹", Toast.LENGTH_SHORT).show();
		}
		else if (id == 8) {
			dbManager.clearHistory();
			Toast.makeText(this, "已成功清空历史记录", Toast.LENGTH_SHORT).show();
		}
	}

	public void dialogShow(final int id) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认清除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearData(id);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}
