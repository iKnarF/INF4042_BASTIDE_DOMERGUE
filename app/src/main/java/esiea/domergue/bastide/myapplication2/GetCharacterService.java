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


public class GetCharacterService extends IntentService {

    private static final String TAG = "SERVICE_GET_CHARACTER";

    public GetCharacterService() {
        super("GetCharacterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String EXTRA_NICKNAME = (String) intent.getSerializableExtra("user_nickname");
        if (intent != null) {
            Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
            URL url = null;
            try {
                url = new URL("https://census.daybreakgames.com/s:domergue/get/ps2/character/?name.first=" + EXTRA_NICKNAME);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "character.json"));
                    Log.d(TAG, "character.json downloaded!");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ScrollingActivity.CHAR_UPDATE));
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
