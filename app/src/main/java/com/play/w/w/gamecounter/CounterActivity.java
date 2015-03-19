package com.play.w.w.gamecounter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

import java.util.ArrayList;

import Models.PersonModel;


public class CounterActivity extends ActionBarActivity {

    private SeekArc mSeekArc;
    private TextView mSeekArcProgress;
    private TextView mRound;
    private TextView mOverall;
    private ArrayList<PersonModel> mPersonModel;
    static final int COUNTER_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        final Spinner selectPlayer = (Spinner) findViewById(R.id.playerSelectSpinner);
        mSeekArc = (SeekArc) findViewById(R.id.seekArc);
        mSeekArcProgress = (TextView) findViewById(R.id.seekArcProgress);
        mRound = (TextView) findViewById(R.id.thisRoundID);
        mOverall = (TextView) findViewById(R.id.overallID);
        Intent intent = getIntent();

        /**
         * listener for seekbar
         */
        selectPlayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //int pos = Integer.valueOf(parent.getItemAtPosition(position).toString());
                PersonModel person = mPersonModel.get(position);

                if (person.getScorePerRoundString() != null && !person.getScorePerRoundString().isEmpty()){
                    person.stringToList();
                    int last = person.getScorePerRound().size();
                    //if(last != 1){
                        mOverall.setText(String.valueOf(person.getScorePerRound().get(last-1)));
                    //}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
                for(int i=0; i<mPersonModel.size();i++){
                    if(selectPlayer.getSelectedItem().toString().equals(mPersonModel.get(i).getName())){
                        mPersonModel.get(i).setScore(Integer.valueOf(mRound.getText().toString()));
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mSeekArcProgress.setText(String.valueOf(progress));
                mRound.setText(String.valueOf(progress));
            }
        });


        /**
         * temporary list for testing
         */
        mPersonModel = intent.getParcelableArrayListExtra("counterExtra");
        ArrayList<String> tempPlayers = new ArrayList<>();
        for(int i=0; i<mPersonModel.size();i++) {
            tempPlayers.add(mPersonModel.get(i).getName());
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempPlayers);

        selectPlayer.setAdapter(itemsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
/*
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
    }*/

    public void addPointsToPlayer(View view){
        ArrayList<PersonModel> personList = new ArrayList<>();

        for(int i=0; i<mPersonModel.size();i++){
            PersonModel person = mPersonModel.get(i);
            int score = person.getScore();
            ArrayList<Integer> spr = new ArrayList<>();
            if(person.getScorePerRoundString() != null && !person.getScorePerRoundString().isEmpty()){
                person.stringToList();
                spr = person.getScorePerRound();
            }
            //spr = person.getScorePerRound();
            spr.add(score);
            person.setScore(0);
            person.listToString(spr);
            personList.add(person);
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("returnCounterExtra", personList);
        setResult(COUNTER_ACTIVITY, intent);
        finish();
    }
}
/*
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
            </intent-filter>
 */