package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrik Granec on 19.2.2015.
 */
public class PersonModel implements Parcelable {

    private int id;
    private String name;
    private int score;
    private ArrayList<Integer> scorePerRound;
    private String scorePerRoundString;
    private int color;
    private boolean isAdded;

    public void PersonModel(){
        scorePerRound = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScorePerRoundString(String score){this.scorePerRoundString = score;}

    public String getScorePerRoundString(){return scorePerRoundString;}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Integer> getScorePerRound() {
        return scorePerRound;
    }

    public void setScorePerRound(ArrayList<Integer> scorePerRound) {
        this.scorePerRound = scorePerRound;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static final Parcelable.Creator<PersonModel> CREATOR = new Creator<PersonModel>() {

        public PersonModel createFromParcel(Parcel source) {
            PersonModel mPerson = new PersonModel();
            mPerson.id = source.readInt();
            mPerson.name = source.readString();
            mPerson.score = source.readInt();
            mPerson.color = source.readInt();
            mPerson.scorePerRoundString = source.readString();
            return mPerson;
        }


        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(score);
        parcel.writeInt(color);
        parcel.writeString(scorePerRoundString);
    }

    public void stringToList(){
        ArrayList<Integer> list = new ArrayList<>();
        for(String s : getScorePerRoundString().split(",")){
            list.add(Integer.valueOf(s));
        }
        setScorePerRound(list);
    }

    public void listToString(ArrayList<Integer> list){
        String spr = Joiner.on(",").join(list);
        setScorePerRoundString(spr);
    }

    public void listToString(){
        String spr = Joiner.on(",").join(getScorePerRound());
        setScorePerRoundString(spr);
    }
}
