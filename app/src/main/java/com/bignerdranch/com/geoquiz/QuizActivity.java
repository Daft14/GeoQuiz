package com.bignerdranch.com.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private static final String KEY_IS_CHEATER = "is_cheater";

    private boolean mIsCheater;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;

    private ImageButton mNextButton;
    private ImageButton mPreviousButton;

    private TextView mQuestionTextView;
    private TextView mApiLevel;

    private final Question[] mQuestionBank = new Question[]{

            new Question(R.string.question_australia, true),

            new Question(R.string.question_oceans, true),

            new Question(R.string.question_mideast, false),

            new Question(R.string.question_africa, false),

            new Question(R.string.question_americas, true),

            new Question(R.string.question_asia, true),

    };

    private int mCurrentIndex = 0;
    double rightAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {

            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);

        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mApiLevel = findViewById(R.id.apiLevel);
        String keyapi = getResources().getString(R.string.api);
        mApiLevel.setText(String.format(keyapi, Build.VERSION.SDK_INT));


        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);

        mNextButton = findViewById(R.id.next_button);
        mPreviousButton = findViewById(R.id.previous_button);

        mCheatButton = findViewById(R.id.cheat_button);

        mQuestionTextView.setOnClickListener(v -> {

            mNextButton.callOnClick();
            setTrueButton();
        });

        mTrueButton.setOnClickListener(v -> {

            checkAnswer(true);
            setFalseButton();

        });

        mFalseButton.setOnClickListener(v -> {

            checkAnswer(false);
            setFalseButton();

        });

        mNextButton.setOnClickListener(v -> {

            if (mCurrentIndex == mQuestionBank.length - 1) {
                rightAnswer = (float) ((rightAnswer * 100) / mQuestionBank.length);
                Toast.makeText(QuizActivity.this, "Correct answers: " + String.format("%.1f", rightAnswer) + "%", Toast.LENGTH_SHORT).show();
                rightAnswer = 0;

            }
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            mIsCheater = false;
            updateQuestion();
            setTrueButton();


        });

        mPreviousButton.setOnClickListener(v -> {
            if (mCurrentIndex == 0) {
                mCurrentIndex = mQuestionBank.length - 1;
            } else {
                mCurrentIndex = mCurrentIndex - 1;
            }

            updateQuestion();
            setTrueButton();
        });


        mCheatButton.setOnClickListener(v -> {

            // Start CheatActivity
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

            Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);

            startActivityForResult(intent, REQUEST_CODE_CHEAT);

        });
        updateQuestion();
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {

            return;

        }

        if (requestCode == REQUEST_CODE_CHEAT) {

            if (data == null) {

                return;

            }

            mIsCheater = CheatActivity.wasAnswerShown(data);

        }

    }
    @Override

    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsCheater);

    }

    private void setTrueButton() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void setFalseButton() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void updateQuestion() {

        int question = mQuestionBank[mCurrentIndex].getTextResId();

        mQuestionTextView.setText(question);

    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                rightAnswer++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        }

    }