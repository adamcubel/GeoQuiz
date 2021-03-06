package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.text.DecimalFormat;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String CHEAT_COUNT = "cheat_count";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mCheatCountTextView;

    private Question[] mQuestionsBank = new Question[] {
        new Question(R.string.question_text, true),
        new Question(R.string.question_africa, true),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_asia,true),
        new Question(R.string.question_mideast, true),
        new Question(R.string.question_oceans, true),
    };

    private int mCurrentIndex = 0;
    private int mCheatCount = 0;
    private boolean mIsCheater;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(CHEAT_COUNT, mCheatCount);
    }

    // called when an instance of the activity subclass is created.
    // When an activity is created, it needs a UI to manage.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        // To get this UI, call the following method. This method inflates a layout and puts
        // it on screen. You specify which layout to inflate by passing in the layout's resource ID
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCheatCount = savedInstanceState.getInt(CHEAT_COUNT, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                mIsCheater = false;
                if (mCurrentIndex == 0) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    Toast.makeText(QuizActivity.this,
                            df.format(calculateScore()) + "%",
                                    Toast.LENGTH_SHORT).show();
                    resetAnswers();
                }
                updateQuestion();
            }
        });

        mCheatCountTextView = (TextView) findViewById(R.id.cheat_count_view);
        DecimalFormat df = new DecimalFormat("#");
        mCheatCountTextView.setText(df.format(mCheatCount));

        // getting the view object for each of the buttons.
        // takes the button's resource ID as an argument.
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                mQuestionsBank[mCurrentIndex].setQuestionAnswered(true);
            }
        });

        // getting the view object for each of the buttons.
        // takes the button's resource ID as an argument.
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                mQuestionsBank[mCurrentIndex].setQuestionAnswered(true);
            }
        });

        // getting the view object for each of the buttons.
        // takes the button's resource ID as an argument.
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                mIsCheater = false;
                if (mCurrentIndex == 0) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    Toast.makeText(QuizActivity.this,
                                    df.format(calculateScore()) + "%",
                                    Toast.LENGTH_SHORT).show();
                    resetAnswers();
                }
                updateQuestion();
            }
        });

        // getting the view object for each of the buttons.
        // takes the button's resource ID as an argument.
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (mCheatCount < 3) {
                boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mCheatCount++;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        boolean questionAnswered = mQuestionsBank[mCurrentIndex].isQuestionAnswered();

        if (!questionAnswered) {
            int messageResId = 0;
            if (mIsCheater) {
                messageResId = R.string.judgement_toast;
            }
            else {
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct_toast;
                    mQuestionsBank[mCurrentIndex].setQuestionAnsweredCorrect(true);
                } else {
                    messageResId = R.string.incorrect_toast;
                    mQuestionsBank[mCurrentIndex].setQuestionAnsweredCorrect(false);
                }
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    private void resetAnswers() {
        int currentIndex = 0;

        do {
            mQuestionsBank[mCurrentIndex].setQuestionAnswered(false);
            mQuestionsBank[currentIndex].setQuestionAnsweredCorrect(false);
            currentIndex = (currentIndex + 1) % mQuestionsBank.length;
        } while (currentIndex != 0);
    }

    private double calculateScore() {
        int currentIndex = 0;
        double questionsAnsweredCorrect = 0.0;
        double totalQuestionsAnswered = 0.0;

        do {
            totalQuestionsAnswered++;
            if (mQuestionsBank[currentIndex].isQuestionAnsweredCorrect()) {
                questionsAnsweredCorrect++;
            }
            currentIndex = (currentIndex + 1) % mQuestionsBank.length;
        } while (currentIndex != 0);
        return (questionsAnsweredCorrect / totalQuestionsAnswered * 100);
    }
}
