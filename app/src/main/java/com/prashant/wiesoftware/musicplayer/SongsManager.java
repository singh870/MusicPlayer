package com.prashant.wiesoftware.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
	// SDCard Path
	final String MEDIA_PATH = new String("/media/audio/");
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private Context context;
	MediaMetadataRetriever metaRetriver;
	private Bitmap songImage;

	// Constructor
	public SongsManager(Context context){
		this.context = context;
	}
	
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
/*	public ArrayList<HashMap<String, String>> getPlayList(){
		File home = new File(Environment.getExternalStorageDirectory().getPath()+MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());
				
				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}*/
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}

	public ArrayList<HashMap<String, String>> retriveMediaByContentProvider(){
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(Environment.getExternalStorageDirectory().getPath());
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
		String[] selectionArgsMp3 = new String[]{ mimeType };
		String sortOrder = "LOWER ("+MediaStore.Audio.Media.TITLE + ") ASC";

		Cursor cursor = contentResolver.query(uri, null, selectionMimeType, selectionArgsMp3, sortOrder);
		if (cursor != null) {
			if (!cursor.moveToFirst()) {
                // no media on the device
                Log.d("Result", "no media on the device");
            } else {
                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int artistColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
                int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                do {
                    long thisId = cursor.getLong(idColumn);
                    String thisTitle = cursor.getString(titleColumn);
                    String pathId = cursor.getString(column_index);
                    String thisArtist = cursor.getString(artistColumn);
                    String thisAlbumName = cursor.getString(albumColumn);
                    Log.d("Title Name", thisTitle+"::"+thisArtist+"::"+thisAlbumName);

                    // ...process entry... audio/mpeg
                    HashMap<String, String> song = new HashMap<String, String>();
                    song.put("songTitle", thisTitle);
                    song.put("songPath", String.valueOf(thisId));
                    song.put("pathId", pathId);
                    song.put("artistName", thisArtist);
                    song.put("albumName", thisAlbumName);

                    // Adding each song to SongList
                    songsList.add(song);

                } while (cursor.moveToNext());
            }
			/*	Close the cursor after completion of task.	*/
			cursor.close();
		}
		return songsList;
	}
}
