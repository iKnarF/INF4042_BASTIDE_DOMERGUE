package esiea.domergue.bastide.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ScrollingActivity extends MainActivity {

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
        if (EXTRA_NICKNAME.length()> 1){
            //R.string.title_activity_main nic = title_activity_main;
            toolbar.setTitle(EXTRA_NICKNAME.toString());
        }
        //Affichage d'un Toast si l'utilisateur n'a pas rentré de pseudo
        else{
            Toast.makeText(getApplicationContext(),getString(R.string.toastEmptyNickname),Toast.LENGTH_LONG).show();
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
}
