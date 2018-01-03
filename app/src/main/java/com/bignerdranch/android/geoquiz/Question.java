package com.bignerdranch.android.geoquiz;

/**
 * Created by adamc on 12/31/2017.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mQuestionAnswered;
    private boolean mQuestionAnsweredCorrect;

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mQuestionAnswered = false;
        mQuestionAnsweredCorrect = false;
    }

    public boolean isQuestionAnswered() {
        return mQuestionAnswered;
    }

    public void setQuestionAnswered(boolean questionAnswered) {
        mQuestionAnswered = questionAnswered;
    }

    public boolean isQuestionAnsweredCorrect() {
        return mQuestionAnsweredCorrect;
    }

    public void setQuestionAnsweredCorrect(boolean questionAnsweredCorrect) {
        mQuestionAnsweredCorrect = questionAnsweredCorrect;
    }
}
