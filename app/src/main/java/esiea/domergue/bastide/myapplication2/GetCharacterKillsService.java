package esiea.domergue.bastide.myapplication2;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetCharacterKillsService extends IntentService {

    private static final String TAG = "SERVICE_GET_CHAR_KILLS";

    public GetCharacterKillsService() {
        super("GetCharacterKillsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String CHARACTER_ID = (String) intent.getSerializableExtra("character_id");
        if (intent != null) {
            Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
            URL url = null;
            try {
                url = new URL("https://census.daybreakgames.com/s:domergue/get/ps2/characters_stat/?character_id=" + CHARACTER_ID + "&stat_name=hit_count");
                Log.d(TAG, "url kills : " + "https://census.daybreakgames.com/s:domergue/get/ps2/characters_stat/?character_id=" + CHARACTER_ID + "&stat_name=hit_count");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "character_kills.json"));
                    Log.d(TAG, "character_kills.json downloaded!");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ScrollingActivity.CHAR_KILLS));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
