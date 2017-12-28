package de.berger.gomezbutton;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import de.psdev.licensesdialog.LicensesDialog;

/**
 * @author berger
 *
 * MainActivity Mario Gomez Button.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "GOMEZBUTTON";

    private HTextView gomezTextView;
    private Button playButton;
    private MediaPlayer mp2;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mp2 = MediaPlayer.create(this, R.raw.mariogomez);
        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setText("Play");
            }

        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //FMS Token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseIDService", "Refreshed token: " + refreshedToken);

        initTextView();

        initImageView();

        initPlayBtn();

        //Android Wear Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            boolean startedFromWear = bundle.getBoolean("wear", false);
            if (startedFromWear)
                playButton.performClick();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //License Dialog
        int id = item.getItemId();
        if (id == R.id.about_menuitem) {
            new LicensesDialog.Builder(this)
                    .setNotices(R.raw.notices)
                    .setIncludeOwnLicense(true)
                    .build()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mp2.stop();
        super.onBackPressed();
        this.finish();
    }

    /**
     * TextView initialisieren.
     */
    private void initTextView() {
        gomezTextView = (HTextView) findViewById(R.id.text);
        // be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
        gomezTextView.setAnimateType(HTextViewType.LINE);
        gomezTextView.animateText("Mario Gomez Button"); // animate
    }

    /**
     * ImageView initialisieren.
     */
    private void initImageView() {
        KenBurnsView gomezImageView = (KenBurnsView) findViewById(R.id.imageView);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(6000, new AccelerateDecelerateInterpolator());
        gomezImageView.setTransitionGenerator(generator);
        gomezImageView.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                //..
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                //..
            }
        });

        gomezImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://shop.vfb.de/heimtrikot-17-18-2950-08.html"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * Play Button initialisieren
     */
    private void initPlayBtn() {
        playButton = (Button) this.findViewById(R.id.play_button);


        playButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "play");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "playBtn");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                // If the music is playing
                if (mp2.isPlaying()) {
                    // Pause the music player
                    mp2.pause();
                    playButton.setText("Play");
                }
                // If it's not playing
                else {
                    // Resume the music player
                    mp2.start();
                    playButton.setText("Pause");
                    gomezTextView.animateText("Mario Gomez Button"); // animate
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}