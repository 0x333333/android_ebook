package com.jesusjzp.sanwensuibi;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.jesusjzp.sanwensuibi.R;
import com.jesusjzp.db.DBManager;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleContentActivity extends BaseActivity {

	private DBManager dbManager;
	private Cursor cursor;

	private String id;
	private String url;
	private String abstr;
	private String title;
	private String status;
	private String type;
	private String res;
	private boolean isRated;
	
	private TextView doc;

	public ArticleContentActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSlidingActionBarEnabled(true);

		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		id = bundle.getString("id");
		url = bundle.getString("url");
		abstr = bundle.getString("abstr");
		title = bundle.getString("title");
		status = bundle.getString("status");
		type = bundle.getString("type");
		res = bundle.getString("res");
		
		String comment = "<br><br>------<br><br>"
				+ "文章摘自http://www.duanwenxue.com"+url+"<br><br>"
				+ "如果喜欢这些文章，还希望能向您的朋友推荐，谢谢！";
		
		if (res.length() > 200)
			res = res+comment;
		
		isRated = checkRating();

		getSupportActionBar().setTitle(title);
		
		setContentView(R.layout.doc_content);
		
		doc = (TextView) findViewById(R.id.textView1);
		res = res.replaceAll("<a(.*?)href=\"(.*?)\"(.*?)>", "").replace("</a>", "");
		Log.v("res", res);
		Spanned wordspan = Html.fromHtml(res);
		doc.setText(wordspan);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu = menu.addSubMenu("Action Rating");
		MenuItem subMenu1Item = subMenu.getItem();
		if (checkRating())
			subMenu1Item.setIcon(R.drawable.rating_important);
		else
			subMenu1Item.setIcon(R.drawable.rating_not_important);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show();
			if (checkRating()) {
				item.setIcon(R.drawable.rating_not_important);
				deleteRating();
			} else {
				item.setIcon(R.drawable.rating_important);
				addRating();
			}
			return true;
		} else if (item.getItemId() == android.R.id.home) {
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	public boolean checkRating() {
		dbManager = new DBManager(this);
		dbManager.openDatabase();
		cursor = dbManager.fetchFavoriteById(id);
		dbManager.closeDatabase();
		if (cursor.getCount() == 0)
			return false;
		else
			return true;
	}

	public void addRating() {
		dbManager = new DBManager(this);
		dbManager.openDatabase();
		dbManager.insertFavorit(id, url, title, status, type, abstr);
		dbManager.closeDatabase();
	}

	public void deleteRating() {
		dbManager = new DBManager(this);
		dbManager.openDatabase();
		dbManager.deleteFavoriteById(id);
		dbManager.closeDatabase();
	}
}
