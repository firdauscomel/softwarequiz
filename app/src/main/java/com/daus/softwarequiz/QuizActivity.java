package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    static Button mAButton, mBButon, mCButton;//public static because these buttons need to be accessed by ShakeService.java
    TextView questionText, hintText;
    static int index;//static for the same reason as above. Only a single instance is needed at a given time
    int score;
    int mQuestion;
    int questionNumber = 1;
    public static int hintCount = 3;
    static boolean hintIsShown = false;//flag to check if hint is shown
    String mQuizType;
    ImageView mCloseImg;
    TextView scoreTextView;
    ProgressBar progressBar;
    private String mUsername;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    User user, userNew;
    boolean isNew;


    private static Quiz[] mQuestionBank = new Quiz[]{};

    private Quiz[] mWebQuestionBank = new Quiz[]{
            new Quiz(R.string.web_question_1, 'c', R.string.web_question_1_answer_a, R.string.web_question_1_answer_b, R.string.web_question_1_answer_c),
            new Quiz(R.string.web_question_2, 'b', R.string.web_question_2_answer_a, R.string.web_question_2_answer_b, R.string.web_question_2_answer_c),
            new Quiz(R.string.web_question_3, 'c', R.string.web_question_3_answer_a, R.string.web_question_3_answer_b, R.string.web_question_3_answer_c),
            new Quiz(R.string.web_question_4, 'a', R.string.web_question_4_answer_a, R.string.web_question_4_answer_b, R.string.web_question_4_answer_c),
            new Quiz(R.string.web_question_5, 'a', R.string.web_question_5_answer_a, R.string.web_question_5_answer_b, R.string.web_question_5_answer_c),
            new Quiz(R.string.web_question_6, 'c', R.string.web_question_6_answer_a, R.string.web_question_6_answer_b, R.string.web_question_6_answer_c),
            new Quiz(R.string.web_question_7, 'c', R.string.web_question_7_answer_a, R.string.web_question_7_answer_b, R.string.web_question_7_answer_c),
            new Quiz(R.string.web_question_8, 'c', R.string.web_question_8_answer_a, R.string.web_question_8_answer_b, R.string.web_question_8_answer_c),
            new Quiz(R.string.web_question_9, 'a', R.string.web_question_9_answer_a, R.string.web_question_9_answer_b, R.string.web_question_9_answer_c),
            new Quiz(R.string.web_question_10, 'c', R.string.web_question_10_answer_a, R.string.web_question_10_answer_b, R.string.web_question_10_answer_c),
            new Quiz(R.string.web_question_11, 'b', R.string.web_question_11_answer_a, R.string.web_question_11_answer_b, R.string.web_question_11_answer_c),
            new Quiz(R.string.web_question_12, 'b', R.string.web_question_12_answer_a, R.string.web_question_12_answer_b, R.string.web_question_12_answer_c),
            new Quiz(R.string.web_question_13, 'c', R.string.web_question_13_answer_a, R.string.web_question_13_answer_b, R.string.web_question_13_answer_c),
            new Quiz(R.string.web_question_14, 'c', R.string.web_question_14_answer_a, R.string.web_question_14_answer_b, R.string.web_question_14_answer_c),
            new Quiz(R.string.web_question_15, 'a', R.string.web_question_15_answer_a, R.string.web_question_15_answer_b, R.string.web_question_15_answer_c)

    };

    private Quiz[] mDSQuestionBank = new Quiz[]{
            new Quiz(R.string.ds_question_1, 'b', R.string.ds_question_1_answer_a, R.string.ds_question_1_answer_b, R.string.ds_question_1_answer_c),
            new Quiz(R.string.ds_question_2, 'a', R.string.ds_question_2_answer_a, R.string.ds_question_2_answer_b, R.string.ds_question_2_answer_c),
            new Quiz(R.string.ds_question_3, 'c', R.string.ds_question_3_answer_a, R.string.ds_question_3_answer_b, R.string.ds_question_3_answer_c),
            new Quiz(R.string.ds_question_4, 'b', R.string.ds_question_4_answer_a, R.string.ds_question_4_answer_b, R.string.ds_question_4_answer_c),
            new Quiz(R.string.ds_question_5, 'c', R.string.ds_question_5_answer_a, R.string.ds_question_5_answer_b, R.string.ds_question_5_answer_c),
            new Quiz(R.string.ds_question_6, 'b', R.string.ds_question_6_answer_a, R.string.ds_question_6_answer_b, R.string.ds_question_6_answer_c),
            new Quiz(R.string.ds_question_7, 'a', R.string.ds_question_7_answer_a, R.string.ds_question_7_answer_b, R.string.ds_question_7_answer_c),
            new Quiz(R.string.ds_question_8, 'b', R.string.ds_question_8_answer_a, R.string.ds_question_8_answer_b, R.string.ds_question_8_answer_c),
            new Quiz(R.string.ds_question_9, 'a', R.string.ds_question_9_answer_a, R.string.ds_question_9_answer_b, R.string.ds_question_9_answer_c),
            new Quiz(R.string.ds_question_10, 'c', R.string.ds_question_10_answer_a, R.string.ds_question_10_answer_b, R.string.ds_question_10_answer_c),
            new Quiz(R.string.ds_question_11, 'c', R.string.ds_question_11_answer_a, R.string.ds_question_11_answer_b, R.string.ds_question_11_answer_c),
            new Quiz(R.string.ds_question_12, 'b', R.string.ds_question_12_answer_a, R.string.ds_question_12_answer_b, R.string.ds_question_12_answer_c),
            new Quiz(R.string.ds_question_13, 'b', R.string.ds_question_13_answer_a, R.string.ds_question_13_answer_b, R.string.ds_question_13_answer_c),
            new Quiz(R.string.ds_question_14, 'a', R.string.ds_question_14_answer_a, R.string.ds_question_14_answer_b, R.string.ds_question_14_answer_c),
            new Quiz(R.string.ds_question_15, 'b', R.string.ds_question_15_answer_a, R.string.ds_question_15_answer_b, R.string.ds_question_15_answer_c)

    };

    private Quiz[] mOOPQuestionBank = new Quiz[]{
            new Quiz(R.string.oop_question_1, 'c', R.string.oop_question_1_answer_a, R.string.oop_question_1_answer_b, R.string.oop_question_1_answer_c),
            new Quiz(R.string.oop_question_2, 'b', R.string.oop_question_2_answer_a, R.string.oop_question_2_answer_b, R.string.oop_question_2_answer_c),
            new Quiz(R.string.oop_question_3, 'a', R.string.oop_question_3_answer_a, R.string.oop_question_3_answer_b, R.string.oop_question_3_answer_c),
            new Quiz(R.string.oop_question_4, 'b', R.string.oop_question_4_answer_a, R.string.oop_question_4_answer_b, R.string.oop_question_4_answer_c),
            new Quiz(R.string.oop_question_5, 'a', R.string.oop_question_5_answer_a, R.string.oop_question_5_answer_b, R.string.oop_question_5_answer_c),
            new Quiz(R.string.oop_question_6, 'c', R.string.oop_question_6_answer_a, R.string.oop_question_6_answer_b, R.string.oop_question_6_answer_c),
            new Quiz(R.string.oop_question_7, 'b', R.string.oop_question_7_answer_a, R.string.oop_question_7_answer_b, R.string.oop_question_7_answer_c),
            new Quiz(R.string.oop_question_8, 'c', R.string.oop_question_8_answer_a, R.string.oop_question_8_answer_b, R.string.oop_question_8_answer_c),
            new Quiz(R.string.oop_question_9, 'c', R.string.oop_question_9_answer_a, R.string.oop_question_9_answer_b, R.string.oop_question_9_answer_c),
            new Quiz(R.string.oop_question_10, 'a', R.string.oop_question_10_answer_a, R.string.oop_question_10_answer_b, R.string.oop_question_10_answer_c),
            new Quiz(R.string.oop_question_11, 'b', R.string.oop_question_11_answer_a, R.string.oop_question_11_answer_b, R.string.oop_question_11_answer_c),
            new Quiz(R.string.oop_question_12, 'b', R.string.oop_question_12_answer_a, R.string.oop_question_12_answer_b, R.string.oop_question_12_answer_c),
            new Quiz(R.string.oop_question_13, 'a', R.string.oop_question_13_answer_a, R.string.oop_question_13_answer_b, R.string.oop_question_13_answer_c),
            new Quiz(R.string.oop_question_14, 'a', R.string.oop_question_14_answer_a, R.string.oop_question_14_answer_b, R.string.oop_question_14_answer_c),
            new Quiz(R.string.oop_question_15, 'b', R.string.oop_question_15_answer_a, R.string.oop_question_15_answer_b, R.string.oop_question_15_answer_c)
    };

    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / mWebQuestionBank.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        mAuth = FirebaseAuth.getInstance();

        setupUserName();


        FirebaseDatabase.getInstance().getReference("users-scoreboard").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if(snapshot.getValue()==null){
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    user = new User(mUsername);
                    Log.i("user","created new user");
                } else {
                    userNew = snapshot.getValue(User.class);
                    userNew.getWebScore();
                    userNew.getDataStrucScore();
//                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        System.out.println(snapshot.getValue().toString());
//                        Log.i("username", ""+postSnapshot.child("webScore").getValue());
//                        String name = postSnapshot.child("userName").getValue().toString();
//                        int web = Integer.parseInt(postSnapshot.child("webScore").getValue().toString());
//                        int oop= Integer.parseInt(postSnapshot.child("oopScore").getValue().toString());
//                        int sad = Integer.parseInt(postSnapshot.child("sadScore").getValue().toString());
//                        int data = Integer.parseInt(postSnapshot.child("dataStrucScore").getValue().toString());
//                        userNew = new User(name,web,oop,sad,data);
//                        FirebaseDatabase.getInstance().getReference("users-scoreboard").child(mAuth.getUid()).push().setValue(userNew);

                    }
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        mQuizType = getIntent().getExtras().getString("Quiz_type", "defaultKey");
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("ScoreKey");
            questionNumber = savedInstanceState.getInt("QuestionNumberKey");
            index = savedInstanceState.getInt("IndexKey");
            hintCount = savedInstanceState.getInt("HintCount");
        } else {
            score = 0;
            questionNumber = 1;
            index = 0;
            hintCount = 3;
        }

        mAButton = findViewById(R.id.quiz_answer_a);
        mBButon = findViewById(R.id.quiz_answer_b);
        mCButton = findViewById(R.id.quiz_answer_c);
        questionText = findViewById(R.id.quiz_question_text);
        scoreTextView = findViewById(R.id.quiz_result_text);
        progressBar = findViewById(R.id.quiz_progress_bar);
        mCloseImg = findViewById(R.id.scoreboard_close_button);
        hintText = findViewById(R.id.hint_text);
        scoreTextView.setText("Question " + (questionNumber) + " Score " + score + "/" + mWebQuestionBank.length);
        hintText.setText(hintCount + " hints remaining.");

        //TODO Do the same for OOP and SAD
        if (mQuizType.equals("web")) {
            mQuestionBank = fullCopy(mWebQuestionBank);
            mQuestion = mQuestionBank[index].getQuestionId();
            mAButton.setText(mQuestionBank[index].getQuiz_answer_a());
            mBButon.setText(mQuestionBank[index].getQuiz_answer_b());
            mCButton.setText(mQuestionBank[index].getQuiz_answer_c());
        }else if (mQuizType.equals("ds")) {
            mQuestionBank = fullCopy(mDSQuestionBank);
            mQuestion = mQuestionBank[index].getQuestionId();
            mAButton.setText(mQuestionBank[index].getQuiz_answer_a());
            mBButon.setText(mQuestionBank[index].getQuiz_answer_b());
            mCButton.setText(mQuestionBank[index].getQuiz_answer_c());
        }else if (mQuizType.equals("oop")) {
            mQuestionBank = fullCopy(mOOPQuestionBank);
            mQuestion = mQuestionBank[index].getQuestionId();
            mAButton.setText(mQuestionBank[index].getQuiz_answer_a());
            mBButon.setText(mQuestionBank[index].getQuiz_answer_b());
            mCButton.setText(mQuestionBank[index].getQuiz_answer_c());
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

        index = (index + 1) % mQuestionBank.length;
        if (questionNumber < mQuestionBank.length) {
            questionNumber++;
        }
        if (index == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Game Over");
            alert.setCancelable(false);
            alert.setMessage("You scored " + score + " points!");

            if(userNew != null) {
                if(mQuizType.equals("web")){
                    Log.i("webscore", String.valueOf(userNew.getWebScore()));
                    if (userNew.getWebScore() < score) {
                        userNew.setWebScore(score);
                    }
                }else if(mQuizType.equals("ds")){
                    if (userNew.getDataStrucScore() < score) {
                        userNew.setDataStrucScore(score);
                    }
                }else if(mQuizType.equals("oop")){
                    if (userNew.getOopScore() < score) {
                        userNew.setOopScore(score);
                    }
                }else if(mQuizType.equals("sad")){
                    if (userNew.getSadScore() < score) {
                        userNew.setSadScore(score);
                    }
                }
                userNew.setTotalScore();
                updateDatabase(userNew);
            }else{
                if(mQuizType.equals("web")) {
                    Log.i("webscore", String.valueOf(user.getWebScore()));
                    if (user.getWebScore() < score) {
                        user.setWebScore(score);
                    }
                }else if(mQuizType.equals("ds")){
                    if (user.getDataStrucScore() < score) {
                        user.setDataStrucScore(score);
                    }
                }else if(mQuizType.equals("oop")){
                    if (user.getOopScore() < score) {
                        user.setOopScore(score);
                    }
                }else if(mQuizType.equals("sad")){
                    if (user.getSadScore() < score) {
                        user.setSadScore(score);
                    }
                }
                user.setTotalScore();
                updateDatabase(user);
            }
            alert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressBar.setProgress(0);
                    score = 0;
                    index = 0;
                    hintCount = 3;
                    hintText.setText(hintCount + " hints remaining.");
                    questionNumber = 1;
                    scoreTextView.setText("Question " + (index + 1) + " Score " + score + "/" + mQuestionBank.length);


                }
            });
            alert.setNeutralButton("Main Menu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Log.d("QUIZ", "Main menu btn: score = " + score);
//                    user.setWebScore(score);
//                    updateDatabase(user);
                    signOut();
                }
            });
            alert.show();
        }

        mQuestion = mQuestionBank[index].getQuestionId();
        mAButton.setText(mQuestionBank[index].getQuiz_answer_a());
        mBButon.setText(mQuestionBank[index].getQuiz_answer_b());
        mCButton.setText(mQuestionBank[index].getQuiz_answer_c());
        questionText.setText(mQuestion);
        progressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        scoreTextView.setText("Question " + (questionNumber) + " Score " + score + "/" + mQuestionBank.length);


        Log.d("QUIZ", "hintIsShown(updateQuestion): " + hintIsShown);

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
        if (hintIsShown) {
            hintIsShown = false;
            hintCount--;
        }

        hintText.setText(hintCount + " hints remaining.");

        Log.d("QUIZ", "hintCount: " + hintCount);
    }

    private void checkAnswer(char userSelected) {
        char correctAnswer = mQuestionBank[index].getAnswer();

        Log.d("QUIZ", "checkAnswer: " + correctAnswer);

        if (userSelected == correctAnswer) {
            Toast.makeText(getApplicationContext(), R.string.correct_toast, Toast.LENGTH_SHORT).show();
            score += 1;
        } else {
            Toast.makeText(getApplicationContext(), R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
    }


    public void getCorrectAnswer(Context context) {
        Animation blink = AnimationUtils.loadAnimation(context, R.anim.blink);
        Animation fadeToGrey = AnimationUtils.loadAnimation(context, R.anim.fade_to_grey);
        char correctAnswer = mQuestionBank[index].getAnswer();

//        Log.d("QUIZ", "hintIsShown(getCorrectAnswer): "+hintIsShown);

//        Log.d("QUIZ", "getCorrectAnswer: "+correctAnswer + "Index:  "+index);

        if (hintCount > 0) {
            Random rand = new Random();
            int number = rand.nextInt(2);
            int color = Color.argb(230, 207, 235, 30);
//            Toast.makeText(this, "Correct Answer:  " + correctAnswer, Toast.LENGTH_SHORT).show();
            if (correctAnswer == 'a') {
                //Randomizing answers
                if (!hintIsShown) {
                    if (number == 1) {
                        //Highlight A and B
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        mCButton.startAnimation(fadeToGrey);
                        mCButton.setEnabled(false);
                    } else {
                        //Highlight A and C
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mBButon.startAnimation(fadeToGrey);
                        mBButon.setEnabled(false);
                    }
                }
            } else if (correctAnswer == 'b') {
                //Randomizing answers
                if (!hintIsShown) {
                    if (number == 1) {
                        //Highlight A and B
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        mCButton.startAnimation(fadeToGrey);
                        mCButton.setEnabled(false);
                    } else {
                        //Highlight B and C
                        QuizActivity.mBButon.setBackgroundColor(color);
                        mBButon.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mAButton.startAnimation(fadeToGrey);
                        mAButton.setEnabled(false);
                    }
                }
            } else if (correctAnswer == 'c') {
                //Randomizing answers
                if (!hintIsShown) {
                    if (number == 1) {
                        //Highlight A and C
                        QuizActivity.mAButton.setBackgroundColor(color);
                        mAButton.startAnimation(blink);
                        QuizActivity.mCButton.setBackgroundColor(color);
                        mCButton.startAnimation(blink);
                        mBButon.startAnimation(fadeToGrey);
                        mBButon.setEnabled(false);
                    } else {
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
        } else {
            Toast.makeText(context, "Out of hints!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupUserName() {
//        SharedPreferences prefs = getSharedPreferences(register.QUIZ_PREFS, MODE_PRIVATE);
//        mUsername = prefs.getString(register.USERNAME, null);
//        if (mUsername == null) mUsername = "Anon";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mUsername = user.getDisplayName();
        }
        Log.i("username", mUsername);
    }

    private void updateDatabase(User user) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users-scoreboard");
        mDatabaseReference.child(mAuth.getUid()).setValue(user);
    }

    private void signOut() {
        //FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(QuizActivity.this, MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Deep copy of the one of the 4 question banks to mQuestionBank
    private static Quiz[] fullCopy(Quiz[] source) {
        Quiz[] destination = new Quiz[source.length];

        for(int i=0; i< source.length; i++) {
            Quiz q = source[i];
            if(q!=null){
                destination[i]=new Quiz(q);
            }
        }
        return destination;
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
