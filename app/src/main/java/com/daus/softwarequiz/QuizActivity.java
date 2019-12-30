package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    public static Button mAButton, mBButon,mCButton;
    public static TextView questionText;//Testing shake detection. Colour will change when shaken
    int index;
    int score;
    int mQuestion;
    int questionNumber =1;
    int hintCount = 3;
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
        } else {
            score = 0;
            questionNumber =1;
            index = 0;
        }

        mAButton = findViewById(R.id.quiz_answer_a);
        mBButon = findViewById(R.id.quiz_answer_b);
        mCButton = findViewById(R.id.quiz_answer_c);
        questionText = findViewById(R.id.quiz_question_text);
        scoreTextView = findViewById(R.id.quiz_result_text);
        progressBar = findViewById(R.id.quiz_progress_bar);
        mCloseImg = findViewById(R.id.quiz_close_button);
        scoreTextView.setText("Question "+ (questionNumber) + " Score " + score + "/" + mWebQuestionBank.length);

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
                mAButton.setBackgroundColor(getResources().getColor(R.color.brown_red));

            }
        });
        mBButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer('b');
                updateQuestion();
                mBButon.setBackgroundColor(getResources().getColor(R.color.brown_red));
            }
        });
        mCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer('c');
                updateQuestion();
                mCButton.setBackgroundColor(getResources().getColor(R.color.brown_red));
            }
        });

        mCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Shake for hints
        showHint();

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
    }

    private void checkAnswer(char userSelected) {
        char correctAnswer = mWebQuestionBank[index].getAnswer();

        if (userSelected == correctAnswer) {
            Toast.makeText(getApplicationContext(), R.string.correct_toast, Toast.LENGTH_SHORT).show();
            score += 1;
        } else {
            Toast.makeText(getApplicationContext(), R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
    }

    public char getCorrectAnswer(){
        char correctAnswer = mWebQuestionBank[index].getAnswer();

        return correctAnswer;
    }

    private void showHint() {
        //Shake detection service
        Intent intent = new Intent(this, ShakeService.class);
        if(hintCount>0){
            startService(intent);
            hintCount--;
        }else{
            Toast.makeText(this, "Out of hints!", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("ScoreKey", score);
        outState.putInt("QuestionNumberKey", questionNumber);
        outState.putInt("IndexKey", index);
    }
}
