package com.jesusjzp.http;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

public class HttpGet {
	
	public String getArticle(String url) {
		Document doc = null;
		try {
			String id = url.replace("/article/", "").replace(".html", "");
			Log.v("id", id);
			Log.v("url", "http://www.duanwenxue.com"+url);
			doc = Jsoup.connect("http://www.duanwenxue.com"+url).get();
			Element n_bd = doc.getElementById("aid-"+id);
			if(n_bd != null) {
				String res = n_bd.html();
				Log.v("article", res);
				return res;
			} else {
				String res = "&nbsp;&nbsp;&nbsp;&nbsp;十分抱歉，服务器暂时没有反应，还请谅解！"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;你可以尝试以下操作:<br><br>"
						+ "&nbsp;&nbsp;-&nbsp;检查您的网络连接，确保网络可用；<br>"
						+ "&nbsp;&nbsp;-&nbsp;点击上面的添加收藏夹，一会儿再试。<br>"
						+ "<br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;谢谢！"; 
				return res;
			}
		} catch (IOException e) {
			String res = "&nbsp;&nbsp;&nbsp;&nbsp;十分抱歉，服务器暂时没有反应，还请谅解！"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;你可以尝试以下操作:<br><br>"
					+ "&nbsp;&nbsp;-&nbsp;检查您的网络连接，确保网络可用；<br>"
					+ "&nbsp;&nbsp;-&nbsp;点击上面的添加收藏夹，一会儿再试。<br>"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;谢谢！"; 
			return res;
		}
	}
}
