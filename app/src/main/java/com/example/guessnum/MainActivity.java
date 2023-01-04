package com.example.guessnum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.InputDevice;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int randnum = 0, trys = 0 , timeleft;

    boolean checkstart=false;
    String historique="";
    Button confirm,reset,show;
    TextView result,histo,counter;
    EditText num;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        confirm=(Button) findViewById(R.id.confirm);
        reset=(Button) findViewById(R.id.reset);
        result=(TextView) findViewById(R.id.result);
        histo=(TextView) findViewById(R.id.historique);
        counter=(TextView) findViewById(R.id.counter);
        num=(EditText) findViewById(R.id.num);
        show=findViewById(R.id.showData);
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
                    if (score>50){
                        builder.setMessage("U won with  :"+ score +" Score ");
                        final EditText name = new EditText(MainActivity.this);
                        name.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(name);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String scoreFinal=Integer.toString(score);
                                String nameFinal = name.getText().toString();
                                Boolean checkinsert= db.insertData(nameFinal,scoreFinal);
                                if (checkinsert == true){
                                    Toast.makeText(getApplicationContext(),"Score Added",Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Score Not Added",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                    }
                    else
                    {
                        builder.setMessage("U have Failed : 0 Score");
                    };
                    AlertDialog alert = builder.create();
                    alert.show();
                    confirm.setVisibility(View.INVISIBLE);

                }
                historique = historique + "\r\n" + num.getText().toString();
                histo.setText(historique);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.showAllData();
                if (res.getCount()==0){
                    Toast.makeText(getApplicationContext(),"No score exists",Toast.LENGTH_LONG).show();
                }
                StringBuffer buffer= new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Name :"+res.getString(0)+"\n");
                    buffer.append("Score :"+res.getString(1)+"\n");
                }

                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("All Scores ");
                builder.setMessage(buffer.toString());
                builder.show();
            }

        });

    }
    public void newgame()
    {
        trys = 0 ;
        timeleft = 0;
        Random numRandom = new Random();
        randnum = numRandom.nextInt(100);
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
