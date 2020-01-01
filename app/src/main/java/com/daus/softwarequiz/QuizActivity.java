package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    static Button mAButton, mBButon,mCButton;//public static because these buttons need to be accessed by ShakeService.java
    TextView questionText, hintText;
    static int index;//static for the same reason as above. Only a single instance is needed at a given time
    int score;
    int mQuestion;
    int questionNumber =1;
    public static int hintCount = 3;
    static boolean hintIsShown = false;//flag to check if hint is shown
    String mQuizType;
    ImageView mCloseImg;
    TextView scoreTextView;
    ProgressBar progressBar;

    private Quiz[] mWebQuestionBank = new Quiz[]{
            new Quiz(R.string.web_question_1, 'c', R.string.web_question_1_answer_a, R.string.web_question_1_answer_b, R.string.web_question_1_answer_c),
            new Quiz(R.string.web_question_2, 'b',R.string.web_question_2_answer_a, R.string.web_question_2_answer_b, R.string.web_question_2_answer_c),
            new Quiz(R.string.web_question_3, 'c',R.string.web_question_3_answer_a, R.string.web_question_3_answer_b, R.string.web_question_3_answer_c),
            new Quiz(R.string.web_question_4, 'a',R.string.web_question_4_answer_a, R.string.web_question_4_answer_b, R.string.web_question_4_answer_c),
            new Quiz(R.string.web_question_5, 'a',R.string.web_question_5_answer_a, R.string.web_question_5_answer_b, R.string.web_question_5_answer_c),
            new Quiz(R.string.web_question_6, 'c',R.string.web_question_6_answer_a, R.string.web_question_6_answer_b, R.string.web_question_6_answer_c),
            new Quiz(R.string.web_question_7, 'c',R.string.web_question_7_answer_a, R.string.web_question_7_answer_b, R.string.web_question_7_answer_c),
            new Quiz(R.string.web_question_8, 'c',R.string.web_question_8_answer_a, R.string.web_question_8_answer_b, R.string.web_question_8_answer_c),
            new Quiz(R.string.web_question_9, 'a',R.string.web_question_9_answer_a, R.string.web_question_9_answer_b, R.string.web_question_9_answer_c),
            new Quiz(R.string.web_question_10, 'c',R.string.web_question_10_answer_a, R.string.web_question_10_answer_b, R.string.web_question_10_answer_c),
            new Quiz(R.string.web_question_11, 'b',R.string.web_question_11_answer_a, R.string.web_question_11_answer_b, R.string.web_question_11_answer_c),
            new Quiz(R.string.web_question_12, 'b',R.string.web_question_12_answer_a, R.string.web_question_12_answer_b, R.string.web_question_12_answer_c),
            new Quiz(R.string.web_question_13, 'c',R.string.web_question_13_answer_a, R.string.web_question_13_answer_b, R.string.web_question_13_answer_c),
            new Quiz(R.string.web_question_14, 'c',R.string.web_question_14_answer_a, R.string.web_question_14_answer_b, R.string.web_question_14_answer_c),
            new Quiz(R.string.web_question_15, 'a',R.string.web_question_15_answer_a, R.string.web_question_15_answer_b, R.string.web_question_15_answer_c)

    };

    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / mWebQuestionBank.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        mQuizType = getIntent().getExtras().getString("Quiz_type","defaultKey");

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("ScoreKey");
            questionNumber = savedInstanceState.getInt("QuestionNumberKey");
            index = savedInstanceState.getInt("IndexKey");
            hintCount = savedInstanceState.getInt("HintCount");
        } else {
            score = 0;
            questionNumber =1;
            index = 0;
            hintCount = 3;
        }

        mAButton = findViewById(R.id.quiz_answer_a);
        mBButon = findViewById(R.id.quiz_answer_b);
        mCButton = findViewById(R.id.quiz_answer_c);
        questionText = findViewById(R.id.quiz_question_text);
        scoreTextView = findViewById(R.id.quiz_result_text);
        progressBar = findViewById(R.id.quiz_progress_bar);
        mCloseImg = findViewById(R.id.quiz_close_button);
        hintText = findViewById(R.id.hint_text);
        scoreTextView.setText("Question "+ (questionNumber) + " Score " + score + "/" + mWebQuestionBank.length);
        hintText.setText(hintCount + " hints remaining.");


        if(mQuizType.equals("web")){
            mQuestion = mWebQuestionBank[index].getQuestionId();
            mAButton.setText(mWebQuestionBank[index].getQuiz_answer_a());
            mBButon.setText(mWebQuestionBank[index].getQuiz_answer_b());
            mCButton.setText(mWebQuestionBank[index].getQuiz_answer_c());
        }

        questionText.setText(mQuestion);



        mAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer('a');
                updateQuestion();
            }
        });
        mBButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer('b');
                updateQuestion();
            }
        });
        mCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer('c');
                updateQuestion();
            }
        });

        mCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Shake for hints service
        Intent intent = new Intent(this, ShakeService.class);
        startService(intent);

    }
    private void updateQuestion() {

        index = (index + 1) % mWebQuestionBank.length;
        if(questionNumber < mWebQuestionBank.length){
            questionNumber++;
        }
        if (index == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Game Over");
            alert.setCancelable(false);
            alert.setMessage("You scored " + score + " points!");
            alert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressBar.setProgress(0);
                    score = 0;
                    index = 0;
                    questionNumber=1;
                    scoreTextView.setText("Question "+ (index+1) + " Score " + score + "/" + mWebQuestionBank.length);
                }
            });
            alert.setNeutralButton("Main Menu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }

        mQuestion = mWebQuestionBank[index].getQuestionId();
        mAButton.setText(mWebQuestionBank[index].getQuiz_answer_a());
        mBButon.setText(mWebQuestionBank[index].getQuiz_answer_b());
        mCButton.setText(mWebQuestionBank[index].getQuiz_answer_c());
        questionText.setText(mQuestion);
        progressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        scoreTextView.setText("Question "+ (questionNumber) + " Score " + score + "/" + mWebQuestionBank.length);


        Log.d("QUIZ", "hintIsShown(updateQuestion): "+hintIsShown);

        mAButton.setBackgroundColor(getResources().getColor(R.color.brown_red));
        mBButon.setBackgroundColor(getResources().getColor(R.color.brown_red));
        mCButton.setBackgroundColor(getResources().getColor(R.color.brown_red));

        mAButton.setEnabled(true);
        mBButon.setEnabled(true);
        mCButton.setEnabled(true);

        mAButton.clearAnimation();
        mBButon.clearAnimation();
        mCButton.clearAnimation();

        //Handle hint count
        if(hintIsShown){
            hintIsShown = false;
            hintCount--;
        }

        hintText.setText(hintCount + " hints remaining.");

        Log.d("QUIZ", "hintCount: "+hintCount);
    }

    private void checkAnswer(char userSelected) {
        char correctAnswer = mWebQuestionBank[index].getAnswer();

        Log.d("QUIZ", "checkAnswer: "+correctAnswer);

        if (userSelected == correctAnswer) {
            Toast.makeText(getApplicationContext(), R.string.correct_toast, Toast.LENGTH_SHORT).show();
            score += 1;
        } else {
            Toast.makeText(getApplicationContext(), R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
    }


    public void getCorrectAnswer(Context context){
        Animation blink = AnimationUtils.loadAnimation(context, R.anim.blink);
        Animation fadeToGrey = AnimationUtils.loadAnimation(context, R.anim.fade_to_grey);
        char correctAnswer = mWebQuestionBank[index].getAnswer();

//        Log.d("QUIZ", "hintIsShown(getCorrectAnswer): "+hintIsShown);

//        Log.d("QUIZ", "getCorrectAnswer: "+correctAnswer + "Index:  "+index);

        if(hintCount>0){
            Random rand = new Random();
            int number =  rand.nextInt(2);
            int color = Color.argb(230, 207, 235, 30);
//            Toast.makeText(this, "Correct Answer:  " + correctAnswer, Toast.LENGTH_SHORT).show();
            if (correctAnswer == 'a' || correctAnswer == 'A') {
                //Randomizing answers
                if(!hintIsShown){
                    if(number==1){
                        //Highlight A and B
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        mCButton.startAnimation(fadeToGrey);
                        mCButton.setEnabled(false);
                    }else{
                        //Highlight A and C
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mBButon.startAnimation(fadeToGrey);
                        mBButon.setEnabled(false);
                    }
                }
            } else if (correctAnswer == 'b' || correctAnswer == 'B') {
                //Randomizing answers
                if(!hintIsShown){
                    if(number==1){
                        //Highlight A and B
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        mCButton.startAnimation(fadeToGrey);
                        mCButton.setEnabled(false);
                    }else{
                        //Highlight B and C
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mAButton.startAnimation(fadeToGrey);
                        mAButton.setEnabled(false);
                    }
                }
            } else if (correctAnswer == 'c' || correctAnswer == 'C') {
                //Randomizing answers
                if(!hintIsShown){
                    if(number==1){
                        //Highlight A and C
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mBButon.startAnimation(fadeToGrey);
                        mBButon.setEnabled(false);
                    }else{
                        //Highlight B and C
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mAButton.startAnimation(fadeToGrey);
                        mAButton.setEnabled(false);

                    }
                }
            }
            hintIsShown = true;
        }else{
            Toast.makeText(context, "Out of hints!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("ScoreKey", score);
        outState.putInt("QuestionNumberKey", questionNumber);
        outState.putInt("IndexKey", index);
        outState.putInt("HintCount", hintCount);
    }
}
