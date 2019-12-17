package com.example.sokoban;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class PickLevelActivity extends AppCompatActivity {
	private Level[] levels;
	ListView lv;
	LevelAdapter la;
	int lastLevelPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_level);

		Package pckg = (Package)getIntent().getSerializableExtra("level_package");

		levels = new LevelParser(getApplicationContext()).parse(pckg);

		initMyListView();
	}

	private void initMyListView() {
		lv = findViewById(R.id.level_picker);
		la = new LevelAdapter(this, R.layout.pick_level_item_layout, levels);
		lv.setAdapter(la);

		lv.setOnItemClickListener((parent, view, position, id) -> {
			Intent intent = new Intent(PickLevelActivity.this, GameActivity.class);
			intent.putExtra("level", levels[position]);
			startActivityForResult(intent, 1);
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				if (data.getBooleanExtra("won", false))
					levels[lastLevelPos].setDone(true);
				int opt = data.getIntExtra("option", 2);
				Log.d("MySokoban", "Option: " + opt);
				if (opt == 1) {
					lastLevelPos++;
					Intent intent = new Intent(this, GameActivity.class);
					intent.putExtra("level", levels[lastLevelPos]);
					startActivityForResult(intent, 1);
				} else if (opt == 2) {
					la.notifyDataSetChanged();
				}
			}
		}

	}
}
