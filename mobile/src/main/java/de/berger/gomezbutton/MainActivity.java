package de.berger.gomezbutton;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import de.psdev.licensesdialog.LicensesDialog;

public class MainActivity extends AppCompatActivity {

    private HTextView gomezTextView;
    private Button playButton;
    private MediaPlayer mp2;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp2 = MediaPlayer.create(this, R.raw.mariogomez);
        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setText("Play");
            }

        });
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5435840786706371~8071851243");

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //FMS Token
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("FirebaseIDService", "Refreshed token: " + refreshedToken);

        initTextView();

        initImageView();

        initPlayBtn();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            boolean startedFromWear = b.getBoolean("wear", false);
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
        // Wir prüfen, ob Menü-Element mit der ID "action_daten_aktualisieren"
        // ausgewählt wurde und geben eine Meldung aus
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

    private void initTextView() {
        gomezTextView = (HTextView) findViewById(R.id.text);
        // be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
        gomezTextView.setAnimateType(HTextViewType.LINE);
        gomezTextView.animateText("Mario Gomez Button"); // animate
    }

    private void initImageView() {
        KenBurnsView gomezImageView = (KenBurnsView) findViewById(R.id.imageView);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(6000, new AccelerateDecelerateInterpolator());
        gomezImageView.setTransitionGenerator(generator);
        gomezImageView.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }
        });

        gomezImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://shop.vfl-wolfsburg.de/"));
                startActivity(intent);

            }
        });
    }

    private void initPlayBtn() {
        playButton = (Button) this.findViewById(R.id.play_button);


        playButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // If the music is playing
                if (mp2.isPlaying() == true) {
                    // Pause the music player
                    mp2.pause();
                    playButton.setText("Play");
//                    gomezImage.setVisibility(View.INVISIBLE);
                }
                // If it's not playing
                else {
                    // Resume the music player
                    mp2.start();
                    playButton.setText("Pause");
                    gomezTextView.animateText("Mario Gomez Button"); // animate

//                    gomezImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
