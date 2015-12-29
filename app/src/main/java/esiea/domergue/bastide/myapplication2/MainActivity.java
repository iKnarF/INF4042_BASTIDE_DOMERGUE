package esiea.domergue.bastide.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import static esiea.domergue.bastide.myapplication2.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    //Variable de récupération du texte du pseudo du joueur
    String EXTRA_NICKNAME = "user_nickname";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print("MAIN ACTIVITY");

        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        //
        //getSearchEvent().getInputDevice(SearchView.inflate(R.id.searchView);
        //final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // SearchView searchView = (SearchView) findViewById(R.id.searchView);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.print("MAIN ACTIVITY");
        super.title_activity_main = "ESSAI";
        toolbar.setTitle("EEEEESSSAAAAI");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText2);
                String EXTRA_NICKNAME = editText.getText().toString();
                //Création d'un intent pour changer d'activité et transmettre le pseudo rentré
                //EXTRA_NICKNAME = searchView.getTransitionName();
                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                intent.putExtra("user_nickname", EXTRA_NICKNAME);
                //Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                //intent.putExtra(EXTRA_NICKNAME, searchManager.getSearchableInfo(getComponentName()));

                startActivity(intent);
            }
        });

        //final FloatingActionButton login = EditText;

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
