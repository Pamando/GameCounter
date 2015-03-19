package com.play.w.w.gamecounter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ColorPicker.ColorPickerDialog;
import Models.PersonModel;


public class AddPlayersActivity extends ActionBarActivity {

    private SeekBar mSeekBar;
    private TextView mSeekBarNumber;
    private TableLayout mtl;
    static final int ADD_PLAYERS_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        mSeekBar = (SeekBar) findViewById(R.id.seekBarAddPlayer);
        mSeekBarNumber = (TextView) findViewById(R.id.numberTextViewAddPlayer);
        mtl = (TableLayout) findViewById(R.id.scrollAddPlayer);

        /**
         * listener for seekbar
         */
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int tempOverall = 0;
            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {
                mtl.removeAllViews();
                buildNameCreator(tempOverall, mtl);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekbar, int progress,
                boolean fromUser) {
                    mSeekBarNumber.setText(String.valueOf(progress));
                    tempOverall = progress;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_players, menu);
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

    public void buildNameCreator(int size, TableLayout tl){

        for(int i=0; i<size;i++){
            TableRow row = new TableRow(this);
            row.setWeightSum(2);

            TextView txtview = new TextView(this);
            txtview.setText(R.string.name);
            TableRow.LayoutParams textlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            txtview.setLayoutParams(textlp);

            EditText editxt = new EditText(this);
            //editxt.setTag(1,i);
            TableRow.LayoutParams editlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            editlp.bottomMargin=40;
            editxt.setLayoutParams(editlp);

            row.addView(txtview);
            row.addView(editxt);

            // ROW 2 -----------------------------------
            TableRow row2 = new TableRow(this);
            row2.setWeightSum(2);
            txtview = new TextView(this);
            txtview.setText(R.string.color);
            textlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            txtview.setLayoutParams(textlp);

            final Button button = new Button(this);
            button.setText(R.string.color);
            final int id=0;
            button.setId(id);

            button.setOnClickListener(new View.OnClickListener() {
                int myLovelyVariable;
                @Override
                public void onClick(View v) {
                    showColorPickerDialogDemo(button);

                }
            });
            TableRow.LayoutParams buttonlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            button.setLayoutParams(buttonlp);

            row2.addView(txtview);
            row2.addView(button);
            //-----------------------------------------



            TableRow row3 = new TableRow(this);
            row3.setWeightSum(1);
            View separator = new View(this);
            separator.setBackgroundColor(Color.GRAY);
            TableRow.LayoutParams separatorlp = new TableRow.LayoutParams(0, 5,1);
            separatorlp.setMargins(0,35,0,25);
            separator.setLayoutParams(separatorlp);
            row3.addView(separator);
            tl.addView(row);
            tl.addView(row2);
            tl.addView(row3);
        }

    }

    public void savePlayers(View view){
        TableLayout table = (TableLayout) findViewById(R.id.scrollAddPlayer);
        int tableSize = table.getChildCount();
        ArrayList<PersonModel> personList = new ArrayList<>();
        int id = 1;
        for(int i=0; i<tableSize ;i = i+3){
            PersonModel mPerson = new PersonModel();

            TableRow rowWithName = (TableRow)table.getChildAt(i);
            TableRow rowWithColor = (TableRow)table.getChildAt(i + 1);

            EditText et=(EditText ) rowWithName.getChildAt(1);
            Button colorButton = (Button) rowWithColor.getChildAt(1);

            String name=et.getText().toString();
            int backgroundColor = colorButton.getCurrentTextColor();

            mPerson.setId(id);
            mPerson.setColor(backgroundColor);
            mPerson.setName(name);
            mPerson.setScorePerRound(new ArrayList<Integer>());
            mPerson.setScore(0);
            mPerson.setColor(backgroundColor);

            personList.add(mPerson);
            id++;

        }
        //ending Activity here
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("addPlayerExtra", personList);
        setResult(ADD_PLAYERS_ACTIVITY, intent);
        finish();

    }

    private void showColorPickerDialogDemo(final Button button) {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, button, initialColor,  new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                //showToast(color);

                button.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                button.setTextColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));

            }

        });
        colorPickerDialog.show();

    }
}
