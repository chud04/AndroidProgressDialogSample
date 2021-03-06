package com.ecs.android.listview.sample;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Instead of doing everything on the UI thread, this activity will use an AsyncTask.
 * We'll still show the dialog, refresh the list, and dismiss the dialog, but this time these
 * actions will be done in different threads.
 * On the start of the task, we'll show the dialog on the UI thread.
 * When executing the task (in the background thread), we'll refresh our list.
 * When the task has finished, we'll dismiss the dialog (back in the UI thread).
 * 
 * As opposed to the AllOnUIThread activity, this time, the progress dialog will be shown properly.
 * However, this example causes a problem during a screen orientation change.
 * 
 */
public class AsyncTaskSimple extends AbstractListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new ListRefresher().execute(); 
	}
	
	@Override
	protected void onDestroy() {
		removeDialog(LOADING_DIALOG);
		super.onDestroy();
	}
	
    @Override 
    public Object onRetainNonConfigurationInstance() {
    	return super.onRetainNonConfigurationInstance();
    } 	
    
    
	
	/**
	 * Our menu trigger will launch the asynctask, allowing a background thread to be spawned.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.refresh_list:
		    	new ListRefresher().execute(); 
		        return true;	
		    case R.id.clear_list:
		    	clearList();
		    	refreshListView();
		        return true;		        
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	    
	}
	
	/**
	 * Our background task split up into 3 sections
	 * 
	 * onPreExecute - showing the dialog in the UI thread (just before doInBackground is called).
	 * doInBackground - launches a seperate thread to refresh the list data
	 * onPostExecute - dismisses the dialog and refreshes the list control.
	 * 
	 */
	private class ListRefresher extends AsyncTask<Uri, Void, Void> {

		/**
		 * This is executed in the UI thread. The only place where we can show the dialog.
		 */
		@Override
		protected void onPreExecute() {
			showDialog(LOADING_DIALOG);
		}
		
		/**
		 * This is executed in the background thread. 
		 */
		@Override
		protected Void doInBackground(Uri... params) {
			listItems = retrieveListLongRunning();
			return null;
		}

		/**
		 * This is executed in the UI thread. The only place where we can show the dialog.
		 */
	   @Override 
       protected void onPostExecute(Void unused) { 
    	   dismissDialog(LOADING_DIALOG);
    	   refreshListView();
       } 
	}
	
}
