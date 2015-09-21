package nl.beeradio.streamtester;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {

    public static final int PLAYING = 1;
    public static final int STOPPED = 0;

    private boolean streamSet;
    private int playerState = 0;

    private Button buttonStreamSubmit;
    private EditText editTextStreamSubmit;

    private TextView textViewCurrentStream;
    private Button buttonStop, buttonStart;

    private String currentStream;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TESTER", "Tester initiated");

        buttonStreamSubmit = (Button) findViewById(R.id.buttonStreamSubmit);
        editTextStreamSubmit = (EditText) findViewById(R.id.editTextStreamSubmit);

        textViewCurrentStream = (TextView) findViewById(R.id.textViewCurrentStream);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStart = (Button) findViewById(R.id.buttonStart);

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        buttonStreamSubmit.setOnClickListener(new SubmitListener());

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayer!=null && mPlayer.isPlaying()){
                    mPlayer.stop();
                }
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayer!=null && !mPlayer.isPlaying()){
                    mPlayer.start();
                }
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SubmitListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            currentStream = editTextStreamSubmit.getText().toString();
            textViewCurrentStream.setText(currentStream);

            if(mPlayer!=null && mPlayer.isPlaying()){
                mPlayer.stop();
            } else if (mPlayer==null){
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            try {
                mPlayer.reset();
                mPlayer.setDataSource(currentStream);
            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepare();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
