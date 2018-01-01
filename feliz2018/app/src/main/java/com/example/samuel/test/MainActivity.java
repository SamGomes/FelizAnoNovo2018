package com.example.samuel.test;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity {
    private void allowElementToBeDragged(final View element){
        element.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int windowW = displayMetrics.widthPixels;
                int windowH = displayMetrics.heightPixels;
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int eventX = (int)event.getRawX();
                        int eventY = (int)event.getRawY();

                        if(eventX>windowW){eventX=windowW;}
                        if(eventY>windowH){eventY=windowH;}

                        element.setX(eventX - element.getWidth()/1.5f);
                        element.setY(eventY - element.getHeight()/1.5f);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);

        //------------------------------- PREPARE VIEW ELEMENTS ------------------------------------
        //set images
        //set background
        ImageView backgroundImage = (ImageView) findViewById(R.id.background_image);
        backgroundImage.setImageResource(R.drawable.background);
        //set bottle
        final ImageView bottleImage = (ImageView) findViewById(R.id.bottle_image);
        bottleImage.setImageResource(R.drawable.closed_bottle_image);

        //set card text
        Typeface newYearFont = Typeface.createFromAsset(getAssets(),  "fonts/PWHappyNewYear.ttf");

        TextView cardTextTitle = (TextView) findViewById(R.id.card_text_title);
        cardTextTitle.setTypeface(newYearFont,Typeface.BOLD);
        cardTextTitle.setText("Feliz Ano Novo!");
        final TextView cardTextBody = (TextView) findViewById(R.id.card_text_body);
        cardTextBody.setTypeface(newYearFont);

        TextView cardTextFooter = (TextView) findViewById(R.id.card_text_footer);
        cardTextFooter.setText("Copyright Samuel Gomes 2017. All rights reserved");

        //-------------------------- SET DATE COUNTDOWN TEXT ---------------------------------------

        //Set dates stuff
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date newYearDate = new Date();
        try {
            newYearDate = format.parse("01/01/2018 00:00:00");
            //newYearDate = format.parse("31/12/2017 00:07:00");
        }catch(java.text.ParseException e){
            Log.d("message","caught a parse exception");
        }

        final Date newYearFinalDate = newYearDate;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private void newYearAnimation(){
                cardTextBody.setText("Que as luzes de 2018 brilhem e tragam a todos novos " +
                        "desafios, projetos e muito sucesso!!!");
                bottleImage.setImageResource(R.drawable.opened_bottle_image);
            }
            private void applyCountdownToGUI(){
                Date currentTime = Calendar.getInstance().getTime();
                long timeToNewYear = newYearFinalDate.getTime() - currentTime.getTime();
                if(timeToNewYear <= 1000){
                    newYearAnimation();
                    return;
                }

                String cardDisplayText = "Espera que ainda falta(m) ";

                double timeToNewYearInHours = (double) timeToNewYear/ (double)(60*60*1000);
                int hoursToNewYear = (int) Math.floor(timeToNewYearInHours);

                double timeToNewYearInMinutes = (timeToNewYearInHours - hoursToNewYear)*60;
                int minutesToNewYear = (int) Math.floor(timeToNewYearInMinutes);

                double timeToNewYearInSeconds = (timeToNewYearInMinutes - minutesToNewYear)*60;
                int secondsToNewYear = (int) Math.floor(timeToNewYearInSeconds);

                if(hoursToNewYear>0){
                    cardDisplayText += hoursToNewYear+" hora(s)";
                }
                if(minutesToNewYear>0){
                    if(hoursToNewYear>0){
                        if(secondsToNewYear>0) {
                            cardDisplayText += ",";
                        }else{
                            cardDisplayText += " e";
                        }
                    }
                    cardDisplayText += " ";
                    cardDisplayText += minutesToNewYear+" minuto(s) ";
                }
                if(secondsToNewYear>0) {
                    if(minutesToNewYear>0){
                        cardDisplayText +="e ";
                    }
                    cardDisplayText += secondsToNewYear + " segundo(s) ";
                }

                cardDisplayText+= "para chegarmos a 2018...";

                cardTextBody.setText(cardDisplayText);

            }

            @Override
            public void run() {
                applyCountdownToGUI();
                /* the "trick" for a timed update*/
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        //------------------------------ ADDED FUN STUFF -----------------------------------
        allowElementToBeDragged(cardTextTitle);
        allowElementToBeDragged(cardTextBody);
        allowElementToBeDragged(bottleImage);

    }
}
