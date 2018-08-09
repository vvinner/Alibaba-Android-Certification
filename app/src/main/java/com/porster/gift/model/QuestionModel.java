package com.porster.gift.model;

import android.text.TextUtils;

import com.porster.gift.utils.JSONOpUtils;
import com.porster.gift.utils.UnsupportedTypeException;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 *
 {
 "id": 1,
 "title": "将()属性设置为false，防止adbbackup导出数据",
 "multi": false,
 "choose": [
 {
 "title": "android:allowBackup",
 "result": true
 },
 {
 "title": "android:debuggable"
 },
 {
 "title": "android:permission"
 },
 {
 "title": "android:theme"
 }
 ]
 }
 * Created by Porster on 17/2/28.
 */

public class QuestionModel implements Serializable,Cloneable{
    public String title;

    /**是否多选*/
    public boolean multi;
    /**题目ID*/
    public String id;
    /**正确答案*/
    public String rightAnswer;
    /**答案*/
    public String ansers;

    /**是否已经回答过*/
    public boolean hasAnswer;

    /**答题结果true：答对；false：答错*/
    public boolean resultIsRight;

    /**错误次数,为0则移除*/
    public int errorCount;

    /**答案集合*/
    public List<AnswerModel> choose;

    public void setChoose(JSONArray choose) {
        try {
            this.choose = JSONOpUtils.jsonToList(choose,AnswerModel.class);
        } catch (UnsupportedTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof QuestionModel){
            QuestionModel giftModel= (QuestionModel) o;
            return TextUtils.equals(giftModel.id,id);
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "GiftModel{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", ansers='" + ansers + '\'' +
                ", hasAnswer='" + hasAnswer + '\'' +
                '}';
    }
}
