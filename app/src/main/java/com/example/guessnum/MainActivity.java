package com.example.guessnum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int randnum = 0, trys = 0 , timeleft;

    boolean checkstart=false;
    String historique="";
    Button confirm,reset;
    TextView result,histo,counter;
    EditText num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confirm=(Button) findViewById(R.id.confirm);
        reset=(Button) findViewById(R.id.reset);
        result=(TextView) findViewById(R.id.result);
        histo=(TextView) findViewById(R.id.historique);
        counter=(TextView) findViewById(R.id.counter);
        num=(EditText) findViewById(R.id.num);
        if (checkstart==false)
        {
            reset.setText("Start");
            num.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);
        }
        CountDownTimer countdownjeux = new CountDownTimer(100000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText( + millisUntilFinished / 1000 +"S remaining");
                timeleft = (int) (millisUntilFinished / 1000);
            }

            public void onFinish() {
                counter.setText("done!");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Score");
                builder.setMessage("U have Failed 0 Score");
                AlertDialog alert = builder.create();
                alert.show();
            }

        };

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newgame();
                countdownjeux.start();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trys++;
                int newchif;
                newchif=Integer.valueOf(num.getText().toString());
                if (newchif > randnum) {
                    result.setText("Lower");
                } else if (newchif < randnum) {
                    result.setText("Higher");
                } else {
                    result.setText(randnum +" : Good Job !!! ");
                    countdownjeux.cancel();
                    int score=timeleft-trys;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Score");
                    if(score>0)
                        {
                        builder.setMessage("U won with  :"+ score +" Score ");
                         }
                    else
                        {
                        builder.setMessage("U have Failed : 0 Score");
                        };
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                historique = historique + "\r\n" + num.getText().toString();
                histo.setText(historique);
            }
        });

    }
    public void newgame()
    {
        trys = 0 ;
        timeleft = 0;
        Random numRandom = new Random();
        randnum = numRandom.nextInt(1000);
        checkstart=true;
        historique="";
        num.setText("0");
        reset.setText("New Game");
        num.setVisibility(View.VISIBLE);
        histo.setText("");
        confirm.setVisibility(View.VISIBLE);
        result.setText("Guess Number!");
    }

}