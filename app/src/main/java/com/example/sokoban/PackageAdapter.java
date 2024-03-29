package com.example.sokoban;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PackageAdapter extends ArrayAdapter<Package> {
	private Context context;
	private int resource;
	private List<Package> packages;
	private PackageManager pm;

	public PackageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Package> data) {
		super(context, resource, data);

		this.context = context;
		pm = new PackageManager(context);

		File levelStorage = new File(Utilities.getMapFolderPath());
		if (!levelStorage.exists())
			levelStorage.mkdir();

		this.resource = resource;
		this.packages = data;
	}


	// FIXME is not called
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View row = convertView;
		final LevelPackageHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false);
			holder = new LevelPackageHolder();
			holder.name = row.findViewById(R.id.package_name);
			holder.downloadBtn = row.findViewById(R.id.download_btn);

			row.setTag(holder);
		}
		else {
			holder = (LevelPackageHolder) row.getTag();
		}

		final Package pckg = packages.get(position);

		holder.name.setText(pckg.getName().replace("_", " "));
		if (pckg.isDownloaded())
			holder.downloadBtn.setVisibility(View.INVISIBLE);

		holder.downloadBtn.setOnClickListener(v -> {
			String conn = Settings.getConnectionMethod();
			try {
				if (((conn.equals("wifi") || conn.equals("any")) && Utilities.isWifiConnected()) ||
						(conn.equals("mobile") || conn.equals("any")) && Utilities.isMobileConnected()) {
					pm.downloadPackage(pckg);
					Level[] levels = (new LevelParser(context)).quickParse(pckg);
					DataMapper.getInstance().packageDownloaded(pckg, levels);
					pm.update();
					packages = pm.getPackages();
					notifyDataSetChanged();
				} else {
					Toast.makeText(context, "Internet connection not available", Toast.LENGTH_SHORT).show();
				}
			}
			catch (Exception e) {
				Toast.makeText(context, "Internet connection not available", Toast.LENGTH_SHORT).show();
			}
		});

		return row;
	}


	static class LevelPackageHolder implements Serializable {
		TextView name;
		ImageView downloadBtn;
	}
}
