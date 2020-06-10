package sg.edu.np.WhackAMole;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /* Hint
        - The function setNewMole() uses the Random class to generate a random value ranged from 0 to 2.
        - The function doCheck() takes in button selected and computes a hit or miss and adjust the score accordingly.
        - The function doCheck() also decides if the user qualifies for the advance level and triggers for a dialog box to ask for user to decide.
        - The function nextLevelQuery() builds the dialog box and shows. It also triggers the nextLevel() if user selects Yes or return to normal state if user select No.
        - The function nextLevel() launches the new advanced page.
        - Feel free to modify the function to suit your program.
    */

    final String TAG = "Whackamole";
    Button button1;
    Button button2;
    Button button3;
    ArrayList<Button> buttonList = new ArrayList<Button>();
    int currentMole;
    int score;
    TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "OnCreate");

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        Log.v(TAG, "Buttons set!");

        for (Button button: buttonList) {
            button.setOnClickListener(moleListener);
        }
        Log.v(TAG, "Listeners set!");

        scoreText = findViewById(R.id.scoreText);
        score = 0;
        Log.v(TAG, "set score");
        scoreText.setText(Integer.toString(score));

        Log.v(TAG, "Finished Pre-Initialisation!");
    }

    @Override
    protected void onStart(){
        super.onStart();
        currentMole = setNewMole();
        Log.v(TAG, "Starting GUI!");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TAG, "Paused Whack-A-Mole!");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.v(TAG, "Stopped Whack-A-Mole!");
        finish();
    }

    private void doCheck(int checkButtonId) {
        if (checkButtonId == buttonList.get(currentMole).getId()){
            //add score
            score += 1;
            scoreText.setText(Integer.toString(score));
            //check if score is multiple of 10
            if (score%10 == 0) {
                nextLevelQuery();
            }
        }
        else{
            //log wrong button pressed
            Log.v(TAG, "Wrong Button Pressed!");
        }
        //set all moles to "O"
        for (Button button: buttonList) {
            button.setText("O");
        }
        //set new mole
        currentMole = setNewMole();
    }

    private void nextLevelQuery(){

        //Builds alert here.
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        Log.v(TAG, "new stage dialog building");
        alertBuilder.setTitle("New Stage Unlocked!");
        alertBuilder.setMessage("You have unlocked a special stage! Would you like to play?");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v(TAG, "Starting new stage");
                nextLevel();
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v(TAG, "User decline new stage");
            }
        });

        //show alert
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void nextLevel(){
        /* Launch advanced page */
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("currentPoints", score);
        startActivity(intent);
    }

    public int setNewMole()
    {
        Random ran = new Random();
        int randomLocation = ran.nextInt(3);
        buttonList.get(randomLocation).setText("*");
        return randomLocation;
    }

    View.OnClickListener moleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doCheck(v.getId());
        }
    };
}