package com.bignerdranch.com.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    private static final String TAG = "CheatActivity";
    private static final String KEY_ANSWER_SHOWN = "answer_shown";

    private boolean mAnswerIsTrue;

    private boolean isAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {

        Intent intent = new Intent(packageContext, CheatActivity.class);

        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);

        return intent;
    }
    public static boolean wasAnswerShown(Intent result) {

        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);

        if (savedInstanceState != null) {

            if(mAnswerIsTrue){
                mAnswerTextView.setText(R.string.true_button);
            }
            else{
                mAnswerTextView.setText(R.string.false_button);
            }

            isAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            setAnswerShownResult();

        }




        mShowAnswerButton.setOnClickListener(v -> {

                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
            isAnswerShown = true;
            setAnswerShownResult();

        });
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, isAnswerShown);

    }

    private void setAnswerShownResult() {

        Intent data = new Intent();

        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);

        setResult(RESULT_OK, data);

    }

}