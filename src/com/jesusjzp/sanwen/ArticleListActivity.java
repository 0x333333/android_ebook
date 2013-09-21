package com.jesusjzp.sanwen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.SubMenu;
import com.jeremyfeinstein.slidingmenu.example.R;
import com.jesusjzp.db.DBManager;
import com.jesusjzp.entity.Article;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class ArticleListActivity extends BaseActivity {
	
	private EditText editsearch;
	
	private static final String KEY_TRANSITION_EFFECT = "transition_effect";
	private static final int LIMIT = 60;
	private static String title;
	private static String id;

    private JazzyListView mList;
    private HashMap<String, Integer> mEffectMap;
    private int mCurrentTransitionEffect = JazzyHelper.TILT;
	
    private ArticleListAdapter adapter;
    private ArrayList<Article> arraylist = new ArrayList<Article>();
    
    DBManager dbManager;
	Cursor cursor;
    
	public ArticleListActivity() {
		super(R.string.app_name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		
		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		title = bundle.getString("title");
		id = bundle.getString("id");
		
//		Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
		
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		dbManager = new DBManager(this);
		dbManager.openDatabase();
		if (id.equals("-2")) {
			cursor = dbManager.fetchFavorite();
		} else if (id.equals("-1")) {
			cursor = dbManager.fetchHistory(LIMIT);
		} else if (id.equals("7")) {
			return;
		} else if (id.equals("8")) {
			return;
		} else if (id.equals("9")) {
			return;
		} else {
			cursor = dbManager.fetchArticle(id, LIMIT);
		}
		
		dbManager.closeDatabase();
		
		for (int i = 0; i < cursor.getCount(); i++) {
			Article article = new Article(cursor.getString(0),   //id
					cursor.getString(1),    //url
					cursor.getString(2),    //title
					cursor.getString(3),    //abstr
					cursor.getString(4),    //status
					cursor.getString(5));   //type
			arraylist.add(article);
			cursor.moveToNext();
		}
		
		mList = (JazzyListView) findViewById(android.R.id.list);
		adapter = new ArticleListAdapter(this, arraylist);
        mList.setAdapter(adapter);
        
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.TILT);
            setupJazziness(mCurrentTransitionEffect);
        }
		
		setSlidingActionBarEnabled(true);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// set search action
		getSupportMenuInflater().inflate(R.menu.menu_search, menu);
		editsearch = (EditText) menu.findItem(R.id.menu_search).getActionView();
		editsearch.addTextChangedListener(textWatcher);
		MenuItem menuSearch = menu.findItem(R.id.menu_search);
		menuSearch.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				editsearch.setText("");
				editsearch.clearFocus();
				return true;
			}
			// Menu Action Expand
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				editsearch.requestFocus();
				// Force the keyboard to show on EditText focus
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});
		
		// set more effects action
		SubMenu subMenu = menu.addSubMenu("Action Item");
        mEffectMap = new HashMap<String, Integer>();
        int i = 0;
        String[] effects = this.getResources().getStringArray(R.array.jazzy_effects);
        for (String effect : effects) {
            mEffectMap.put(effect, i++);
        }
        List<String> effectList = new ArrayList<String>(Arrays.asList(effects));
        Collections.sort(effectList);
        effectList.remove(getString(R.string.standard));
        effectList.add(0, getString(R.string.standard));
        for (String effect : effectList) {
        	subMenu.add(0, mEffectMap.get(effect), 0, effect);
        }
        MenuItem subMenu1Item = subMenu.getItem();
        subMenu1Item.setIcon(R.drawable.action_overflow);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_search ||
			item.getItemId() == 0) {
            return false;
        } else if (item.getItemId() == android.R.id.home) {
        	return super.onOptionsItemSelected(item);
        } else {
        	String strEffect = item.getTitle().toString();
//	        Toast.makeText(this, strEffect, Toast.LENGTH_SHORT).show();
	        setupJazziness(mEffectMap.get(strEffect));
	        return true;
        }
    }
	
	// EditText TextWatcher
	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable arg0) {
			String text = editsearch.getText().toString()
					.toLowerCase(Locale.getDefault());
			adapter.filter(text);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
	};
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TRANSITION_EFFECT, mCurrentTransitionEffect);
    }
	
	private void setupJazziness(int effect) {
        mCurrentTransitionEffect = effect;
        mList.setTransitionEffect(mCurrentTransitionEffect);
    }
	
}
