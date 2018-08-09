package com.porster.gift.model;

import java.io.Serializable;

/**
 * Created by Porster on 17/4/4.
 */

public class AnswerModel implements Serializable{
    /**答案*/
    public String title;
    /**是否被选择*/
    public boolean isSelected;
    /**是不是正确答案*/
    public boolean isRightAnswer;

    /**为true说明是正确答案*/
    public boolean result;

    public AnswerModel copy(){
        AnswerModel answerModel=new AnswerModel();
        answerModel.title=title;
        answerModel.result=result;
        return answerModel;
    }

    @Override
    public String toString() {
        return "AnswerModel{" +
                "title='" + title + '\'' +
                ", isSelected=" + isSelected +
                ", isRightAnswer=" + isRightAnswer +
                '}';
    }
}
