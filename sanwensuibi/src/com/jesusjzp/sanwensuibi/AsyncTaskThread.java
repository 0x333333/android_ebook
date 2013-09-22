package com.jesusjzp.sanwensuibi;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskThread extends AsyncTask<Integer, Integer, String>{

	private String tag = "AsyncTaskThread";
	
	public AsyncTaskThread() {
		super();
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		Log.i(tag, "Start --doInBackground");
		try {
			for (int i = 20; i <= 100; i = i + 20) {
				Thread.sleep(params[0] * 1);
				publishProgress(i);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		Log.i(tag, "Start --onProgressUpdate");
		super.onProgressUpdate(values);
	}

}
