package esiea.domergue.bastide.myapplication2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

public class ScrollingActivity extends MainActivity {
    public static final String CHAR_UPDATE = "esiea.domergue.bastide.myapplication2.char_update";
    public static final String CHAR_KILLS = "esiea.domergue.bastide.myapplication2.char_kills";

    public static final String TAG = "SCROLLING_ACTIVITY";
    //final String EXTRA_NICKNAME = "user_nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Récupération du pseudo via l'intent
        Intent intent = getIntent();
        String EXTRA_NICKNAME = (String) getIntent().getSerializableExtra("user_nickname");

        //Si l'utilisateur a bien renseigné un pseudo, on change le titre et on affiche les paramètres récupérés sur le site
        if (EXTRA_NICKNAME.length()>= 1){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(EXTRA_NICKNAME.toString());
        }
        //Affichage d'un Toast si l'utilisateur n'a pas rentré de pseudo et retour à la première fenètre de la saisie
        else{
            Toast.makeText(getApplicationContext(),getString(R.string.toastEmptyNickname),Toast.LENGTH_LONG).show();
            intent = new Intent(ScrollingActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //Permet d'écouter le bouton qui permet de revenir à la première fenètre de selection du pseudo
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //Récupération des informations sur le personnage
        Log.d(TAG, "Starting character service");
        Intent intent2 = new Intent(getApplicationContext(), GetCharacterService.class);
        intent2.putExtra("user_nickname", EXTRA_NICKNAME);
        startService(intent2);
        //Enregistrement du broadcast manager pour savoir quand les caractéristiques du personnage ont été récupérées,
        //et executer la fonction dédiée.
        IntentFilter intentFilter = new IntentFilter(CHAR_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new CharacUpdate(), intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CharacUpdate extends BroadcastReceiver {

        private JSONObject character;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "character updates received");
            try {
                character = getCharacterFromFile("character.json").getJSONArray("character_list").getJSONObject(0);
                printFaction(character);
                printCreationDate(character);
                printLastLogin(character);
                printMinutesPlayed(character);
                printBattleRank(character);
                printCerts(character);
                printKills(character);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void printKills(JSONObject character) throws JSONException {
            //on doit fair une nouvelle requête JSON pour récupérer les kills
            String characterId = character.getString("character_id");
            IntentFilter intentFilter = new IntentFilter(CHAR_KILLS);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new CharacKillsUpdate(), intentFilter);
            Intent intent = new Intent(getApplicationContext(), GetCharacterKillsService.class);
            intent.putExtra("character_id", characterId);
            startService(intent);
        }

        private void printCerts(JSONObject character) throws JSONException {
            int certs = character.getJSONObject("certs").getInt("available_points");
            TextView tv = (TextView) findViewById(R.id.certs);
            tv.setText("" + certs);
            certs = character.getJSONObject("certs").getInt("earned_points");
            tv = (TextView) findViewById(R.id.certs_total);
            tv.setText("" + certs);
        }

        private void printBattleRank(JSONObject character) throws JSONException {
            int battleRank = character.getJSONObject("battle_rank").getInt("value");
            TextView tv = (TextView) findViewById(R.id.battle_rank);
            tv.setText("" + battleRank);
        }

        private void printMinutesPlayed(JSONObject character) throws JSONException {
            Long timePlayed = character.getJSONObject("times").getLong("minutes_played");
            TextView tv = (TextView) findViewById(R.id.minutes_played);
            if(timePlayed > 60){
                timePlayed /= 60;
                tv.setText(timePlayed + " heures");
            } else {
                tv.setText(timePlayed + " minutes");
            }
        }

        private void printLastLogin(JSONObject character) throws JSONException{
            long lastLogin = character.getJSONObject("times").getLong("last_login")*1000;
            String resultLastLogin = DateFormat.getInstance().format(new Date(lastLogin));
            TextView tv = (TextView) findViewById(R.id.last_login);
            tv.setText(resultLastLogin);
        }

        private void printCreationDate(JSONObject character) throws JSONException {
            long creationDate = character.getJSONObject("times").getLong("creation")*1000;
            String resultCreationDate = DateFormat.getInstance().format(new Date(creationDate));
            TextView tv = (TextView) findViewById(R.id.creation_date);
            tv.setText(resultCreationDate);
        }

        private void printFaction(JSONObject character) throws JSONException {
            TextView tv = (TextView) findViewById(R.id.faction);
            String faction = character.getString("faction_id");
            switch(faction){
                case "1":
                    tv.setText(R.string.vanu);
                    break;
                case "2":
                    tv.setText(R.string.conglo);
                    break;
                case "3":
                    tv.setText(R.string.terran);
                    break;
                default:
                    tv.setText(R.string.nanite);
                    break;
            }

        }

        public JSONObject getCharacterFromFile(String file) {
            try {
                InputStream is = new FileInputStream(getCacheDir() + "/" + file);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();
                String result = new String(buffer, "UTF-8");
                JSONObject object = new JSONObject(result);
                return object;

            } catch (IOException e) {
                e.printStackTrace();
                return new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }

        private class CharacKillsUpdate extends BroadcastReceiver {

            private JSONObject characters_stat_list;

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "character kills received");
                try {
                    characters_stat_list = getCharacterFromFile("character_kills.json").getJSONArray("characters_stat_list").getJSONObject(0);
                    printTotalKills(characters_stat_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void printTotalKills(JSONObject characters_stat_list) throws JSONException {
                String totalKills = characters_stat_list.getString("value_forever");
                TextView tv = (TextView) findViewById(R.id.total_kills);
                tv.setText(totalKills);
                String oneLiveTotalKilled = characters_stat_list.getString("value_one_life_max");
                tv = (TextView) findViewById(R.id.one_live_total_kills);
                tv.setText(oneLiveTotalKilled);
            }
        }
    }


}
