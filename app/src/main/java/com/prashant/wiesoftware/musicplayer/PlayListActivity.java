package com.prashant.wiesoftware.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayListActivity extends Activity {
	// Songs list
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);

		String currentSongIndex = getIntent().getStringExtra("currentSongIndex");

		SongsManager plm = new SongsManager(this);
		// get all songs from sdcard
		this.songsList = plm.retriveMediaByContentProvider();

		RecyclerView playlist_recycler = (RecyclerView) findViewById(R.id.playlist_recycler);
		playlist_recycler.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		playlist_recycler.setLayoutManager(mLayoutManager);
		playlist_recycler.setItemAnimator(new DefaultItemAnimator());

		RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, songsList);
		playlist_recycler.setAdapter(adapter);

	}
}
