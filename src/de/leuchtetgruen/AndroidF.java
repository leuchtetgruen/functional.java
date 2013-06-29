package de.leuchtetgruen;

import java.util.List;

import android.os.AsyncTask;
import de.leuchtetgruen.F.Runner;

public class AndroidF {
	public static <T> void performOnListAsync(final List<T> l, int index, final Runner<T> r) {
		AsyncTask<Integer, Integer, T> at = new AsyncTask<Integer, Integer, T>() {

			@Override
			protected T doInBackground(Integer... params) {
				return l.get(params[0]);
			}
			
			
			@Override
			protected void onPostExecute(T result) {
				r.run(result);
			}
		};
		at.execute(index);
	}
}