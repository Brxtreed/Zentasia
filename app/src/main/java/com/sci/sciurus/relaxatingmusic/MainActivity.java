package com.sci.sciurus.relaxatingmusic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {
    ListView list;
    Button stop;
    static MediaPlayer mediaPlayer = null;
    static MediaPlayer effects = null;
    int[] tracks = new int[5];
    int[] track_effects = new int[4];
    int maxVolume;
    int curVolume;
    private RadioGroup radioGroup;
    private RadioButton none, rain, beach, crickets;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    SeekBar seekbar;
    SeekBar seekbar2;
    AudioManager am;

    private int progress;
    private boolean b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] codeLearnChapters = new String[] { "No Soundtrack","Tropical Scenery", "SlipStream", "Angelic Strings", "Sea Breeze", "Centineal"};
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);




        seekbar  =(SeekBar) findViewById(R.id.seekBar);
        seekbar2  =(SeekBar) findViewById(R.id.seekBar2);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        stop = (Button)findViewById(R.id.button);


        //Ititilizes the soundtracks and sound effects
        tracks[0] = R.raw.green;
        tracks[1] = R.raw.neon;
        tracks[2] = R.raw.white;
        tracks[3] = R.raw.seabreeze;
        tracks[4] = R.raw.centineal;

        track_effects[0] = R.raw.rain;
        track_effects[1] = R.raw.beach;
        track_effects[2] = R.raw.rain;
        track_effects[3] = R.raw.storm;






        //Initilizes the seekbars
        seekbar.setMax(maxVolume);
        seekbar.setProgress(curVolume);
        seekbar2.setMax(maxVolume);
        seekbar2.setProgress(curVolume);


        //Initilizes the radio buttons
        rain = (RadioButton) findViewById(R.id.radioButton1);
        beach = (RadioButton) findViewById(R.id.radioButton2);
        crickets = (RadioButton) findViewById(R.id.radioButton3);
        none = (RadioButton)findViewById(R.id.radioButton4);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        none.setChecked(true);



        //Sets the volume for the soundtrack view
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            //When progress level of seekbar2 is changed
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {

                int maxVolume = 15;

                float volume = (float) (1 - (Math.log(100 - progress) / Math.log(100)));

                float log1 = (float) (Math.log(maxVolume - progress) / Math.log(maxVolume));

                mediaPlayer.setVolume(1 - log1, 1 - log1);

                mediaPlayer.start();

            }
        });

        //Sets the volume for the soundeffect view

        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            //When progress level of seekbar2 is changed
            public void onProgressChanged(SeekBar arg0,
                                          int progress, boolean arg2) {

                int maxVolume = 15;
                float volume = (float) (1 - (Math.log(100 - progress) / Math.log(100)));

                float log1 = (float) (Math.log(maxVolume - progress) / Math.log(maxVolume));


                effects.setVolume(1 - log1, 1 - log1);

                effects.start();

            }
        });


        //Itnitlizes the content view flipper
        mContext = this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });










        //Sets the soundeffect listener

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton1) {
                    playeffect(1);
                } else if(checkedId == R.id.radioButton2) {
                    playeffect(2);
                } else if (checkedId == R.id.radioButton3) {
                    playeffect(3);
                }
                    else{
                    stopeffect();

                }
            }

        });








        ArrayAdapter<String> codeLearnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, codeLearnChapters);
        list = (ListView)findViewById(R.id.listView);
        list.setAdapter(codeLearnArrayAdapter);



        //mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
        mediaPlayer = new MediaPlayer();
        effects = new MediaPlayer();
        list.setItemChecked(0, true);


        //Soundtrack selection list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {




                if (position == 0) {
                    stopMusic();
                }
                if (position == 1) {
                    playSong(0);
                }

                if (position == 2) {
                    playSong(1);
                }

                if (position == 3) {
                    playSong(2);
                }

                if (position == 4) {
                    playSong(3);
                }

                if (position == 5) {
                    playSong(4);
                }

            }
        });


        //Music Stop Button

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                mediaPlayer.stop();
                mediaPlayer.reset();
                effects.stop();
                effects.reset();
                list.setItemChecked(0, true);
                radioGroup.check(R.id.radioButton4);
            }
        });






    }



    //Plays the song selection
    public void playSong(int songIndex) {

        mediaPlayer.reset();// stops any current playing song
        mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[songIndex]);
        mediaPlayer.start(); // starting mediaplayer
        mediaPlayer.setLooping(true);

    }

    //Plays effect Selection

    public void playeffect(int track) {
        effects.reset();// stops any current playing song
        effects = MediaPlayer.create(getApplicationContext(), track_effects[track]);// create's
        effects.start();
        effects.setLooping(true);
    }



    //Stops all of the effects
    public void stopeffect() {

        effects.reset();
    }


    //Stops all music
    public void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        effects.stop();
        effects.reset();
    }




    //Implements the Gesture Dectector Class
    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {

                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY && mViewFlipper.getDisplayedChild() < 1) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY && mViewFlipper.getDisplayedChild() > 0) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_out));
                    mViewFlipper.showPrevious();
                    return true;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}








