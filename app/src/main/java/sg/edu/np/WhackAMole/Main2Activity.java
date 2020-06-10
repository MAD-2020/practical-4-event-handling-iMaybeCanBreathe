package sg.edu.np.WhackAMole;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
    /* Hint
        - The function setNewMole() uses the Random class to generate a random value ranged from 0 to 8.
        - The function doCheck() takes in button selected and computes a hit or miss and adjust the score accordingly.
        - The functions readTimer() and placeMoleTimer() are to inform the user X seconds before starting and loading new mole.
        - Feel free to modify the function to suit your program.
    */

    final String TAG = "Whackamole";
    int currentMole;
    int score;
    TextView scoreText;
    Toast countdownToast = null;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */

        CountDownTimer readyCountdown = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                Log.v(TAG, "Countdown: " + l/1000);
                if (countdownToast != null) {countdownToast.cancel();}
                countdownToast = Toast.makeText(getApplicationContext(), "Game starts in: " + l/1000, Toast.LENGTH_SHORT);
                countdownToast.show();
            }

            @Override
            public void onFinish() {
                if (countdownToast != null) {countdownToast.cancel();}
                Toast.makeText(getApplicationContext(), "Game starts now!" , Toast.LENGTH_SHORT).show();

                //set onclick listener after ready countdown finish
                for (Button button: buttonList) {
                    button.setOnClickListener(moleListener);
                }
                placeMoleTimer();
            }
        };

        readyCountdown.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (final Button button: buttonList) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            button.setText("O");

                        }
                    });
                }
                currentMole = setNewMole();
                Log.v(TAG, "tick");
            }
        },100, 1000);
    }
    private static final int[] BUTTON_ID = {
        /* HINT:
            Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
            You may use if you wish to change or remove to suit your codes.*/
        R.id.button1,R.id.button2,R.id.button3,
            R.id.button4,R.id.button5,R.id.button6,
            R.id.button7,R.id.button8,R.id.button9
    };

    private static ArrayList<Button> buttonList = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares the existing score brought over.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Log.v(TAG, "Current User Score: " + String.valueOf(advancedScore));


        for(final int id : BUTTON_ID){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            buttonList.add((Button) findViewById(id));
        }

        Intent receivedIntent = getIntent();
        score = receivedIntent.getIntExtra("currentPoints", 0);
        scoreText = findViewById(R.id.scoreText);
        scoreText.setText(Integer.toString(score));

        readyTimer();
    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    private void doCheck(Button checkButtonId)
    {
        if (checkButtonId == buttonList.get(currentMole)){
            //add score
            score += 1;
            scoreText.setText(Integer.toString(score));

            //set new mole
            currentMole = setNewMole();
        }
        else{
            //log wrong button pressed
            Log.v(TAG, "Wrong Button Pressed!");
        }
    }

    public int setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole.
         */
        Log.v(TAG, "setNewMole");
        Random ran = new Random();
        final int randomLocation = ran.nextInt(9);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                buttonList.get(randomLocation).setText("*");

            }
        });

        return randomLocation;
    }

    View.OnClickListener moleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doCheck((Button) v);
        }
    };
}

