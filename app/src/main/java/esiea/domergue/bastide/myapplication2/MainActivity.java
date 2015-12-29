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
    public String title = "PlanetSide 2 Statsii";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText2);
                String EXTRA_NICKNAME = editText.getText().toString();
                //Création d'un intent pour changer d'activité et transmettre le pseudo rentré
                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                intent.putExtra("user_nickname", EXTRA_NICKNAME);
                startActivity(intent);
            }
        });

    }

}
