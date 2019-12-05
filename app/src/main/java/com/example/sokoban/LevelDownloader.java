package com.example.sokoban;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class LevelDownloader extends AsyncTask<String, Void, Void> {
	private Context context = null;

	public LevelDownloader() {

	}

	public LevelDownloader(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(String... args) {
		try {
			URL url = new URL(args[0]);
			URLConnection conn = url.openConnection();
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			OutputStream output = new FileOutputStream(args[1]);
			byte data[] = new byte[1024];

			int count;
			while ((count = input.read(data)) != -1)
				output.write(data, 0, count);

			output.flush();
			output.close();
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		if (context != null)
			Toast.makeText(context, "Level downloaded", Toast.LENGTH_SHORT).show();
	}
}
