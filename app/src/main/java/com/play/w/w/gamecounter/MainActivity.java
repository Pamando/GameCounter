package com.play.w.w.gamecounter;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DAO.CounterDbAdapter;
import Models.PersonModel;


public class MainActivity extends ActionBarActivity {

    private ArrayList<PersonModel> personModel = new ArrayList<>();
    static final int ADD_PLAYERS_ACTIVITY = 1;
    static final int COUNTER_ACTIVITY = 2;
    private CounterDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new CounterDbAdapter(this);
        mDbHelper.open();

        fillScreen();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.undoTurn) {
            if(personModel.isEmpty() || personModel.get(0).getScorePerRound() == null){
                Toast toast = Toast.makeText(this, R.string.cannot_undo, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return true;
            }
                ArrayList<PersonModel> tempModel = new ArrayList<>();
                for (int i = 0; i < personModel.size(); i++) {
                    PersonModel person = personModel.get(i);
                    ArrayList<Integer> list = person.getScorePerRound();
                    list.remove(list.size()-1);
                    person.setScorePerRound(list);
                    person.listToString(list);
                    tempModel.add(person);
                }
                personModel = tempModel;
                fillScreenTopStill();


        }

        return super.onOptionsItemSelected(item);
    }

    public void onPause() {
        super.onPause();  // Always call the superclass method first
        if(!personModel.isEmpty() && personModel.get(0).getScorePerRound() != null) {
            for (int i = 0; i < personModel.size(); i++) {
                PersonModel person = personModel.get(i);
                person.listToString();
                mDbHelper.updatePlayer(person);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if(personModel.isEmpty()){
            Cursor cursor = mDbHelper.fetchAllPlayers();

            if(cursor.moveToFirst()){
                cursor.moveToFirst();
                do{

                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String spr = cursor.getString(cursor.getColumnIndex("scorePerRound"));
                    int color = cursor.getInt(cursor.getColumnIndex("color"));
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    PersonModel person = new PersonModel();
                    if(!spr.isEmpty()){
                        person.setScorePerRoundString(spr);
                        person.stringToList();
                    }
                    person.setId(id);
                    person.setName(name);
                    person.setColor(color);
                    personModel.add(person);
                }while(cursor.moveToNext());
            }
            cursor.close();
            fillScreen();
        }
    }

    protected void addTableHeader(LinearLayout lv, ArrayList<PersonModel> pm){
        if(pm.isEmpty()){
            return;
        }
        int size = pm.size();
        lv.setWeightSum(size);
        for(int i=0; i<size; i++){
            TextView txtview = new TextView(this);
            txtview.setText(pm.get(i).getName());
            txtview.setGravity(Gravity.CENTER);
            TableRow.LayoutParams textlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            textlp.setMargins(10,0,10,20);
            txtview.setLayoutParams(textlp);
            lv.addView(txtview);
        }
    }

    protected void addTableRows(TableLayout tb, ArrayList<PersonModel> pm, LinearLayout lv){
        if(pm.isEmpty()){
            return;
        }
        int sizePersonList = pm.size();

        if(pm.get(0).getScorePerRound() == null){
            return;
        }
        int sizePoint = pm.get(0).getScorePerRound().size();
        int []sumPoints = new int[sizePersonList];


        for(int j=0;j<sizePoint;j++) {

            TableRow row = new TableRow(this);
            row.setWeightSum(sizePersonList);

            for (int i = 0; i < sizePersonList; i++) {
                TextView txtview = new TextView(this);
                int color = pm.get(i).getColor();
                txtview.setGravity(Gravity.CENTER);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                lp.setMargins(10, 10, 10, 10);
                txtview.setLayoutParams(lp);
                txtview.setText(String.valueOf(pm.get(i).getScorePerRound().get(j)));
                txtview.setTextColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                sumPoints[i] = sumPoints[i] + pm.get(i).getScorePerRound().get(j);
                row.addView(txtview);
                if(j%2 == 0) {
                    row.setBackgroundColor(getResources().getColor(R.color.indigo_white));
                }else{
                    row.setBackgroundColor(getResources().getColor(R.color.indigo_lessWhite));
                }
            }
            tb.addView(row);
        }

        lv.setWeightSum(sizePersonList);

        for(int i=0; i<sizePersonList; i++){
            TextView txtview = new TextView(this);
            txtview.setText(String.valueOf(sumPoints[i]));
            txtview.setGravity(Gravity.CENTER);
            txtview.setTypeface(null, Typeface.BOLD);

            TableRow.LayoutParams textlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            textlp.setMargins(10,0,10,20);
            txtview.setLayoutParams(textlp);
            lv.addView(txtview);
        }
    }

    public void addPoints(View view){
        if(personModel.isEmpty()) {
            Toast toast = Toast.makeText(this, R.string.noPlayersError, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            Intent intent = new Intent(this, CounterActivity.class);
            intent.putParcelableArrayListExtra("counterExtra", personModel);
            startActivityForResult(intent, COUNTER_ACTIVITY);
        }
    }

    public void newGame(View view){
        Intent intent = new Intent(this,AddPlayersActivity.class);
        startActivityForResult(intent, ADD_PLAYERS_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_PLAYERS_ACTIVITY && resultCode != RESULT_CANCELED){
            mDbHelper.deleteData();
            personModel = data.getParcelableArrayListExtra("addPlayerExtra");

            for(int i=0; i<personModel.size();i++){
                //personModel.get(i).stringToList();
                mDbHelper.addPlayer(personModel.get(i));
            }

            fillScreen();
        }

        if(requestCode == COUNTER_ACTIVITY && resultCode != RESULT_CANCELED){
            personModel = data.getParcelableArrayListExtra("returnCounterExtra");
            for(int i=0; i<personModel.size();i++){
                personModel.get(i).stringToList();
            }
            fillScreen();
        }

    }

    public void fillScreen(){

        TableLayout tb = (TableLayout) findViewById(R.id.tableMain);
        LinearLayout lv = (LinearLayout) findViewById(R.id.nameLinearLayoutMain);
        LinearLayout belowScroll = (LinearLayout) findViewById(R.id.sumLinearLayoutMain);

        tb.removeAllViews();
        lv.removeAllViews();
        belowScroll.removeAllViews();

        addTableHeader(lv, personModel);
        addTableRows(tb, personModel, belowScroll);
    }
    public void fillScreenTopStill(){

        TableLayout tb = (TableLayout) findViewById(R.id.tableMain);
        LinearLayout belowScroll = (LinearLayout) findViewById(R.id.sumLinearLayoutMain);

        tb.removeAllViews();
        belowScroll.removeAllViews();

        addTableRows(tb, personModel, belowScroll);
    }
}
