package com.porster.gift.core;

import android.content.Context;

import com.porster.gift.core.async.XAsyncTask;
import com.porster.gift.core.async.XAsyncTaskListenerCompat;
import com.porster.gift.model.AnswerModel;
import com.porster.gift.model.QuestionModel;
import com.porster.gift.utils.AppConstants;
import com.porster.gift.utils.JSONOpUtils;
import com.porster.gift.utils.LogCat;
import com.porster.gift.utils.UnsupportedTypeException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Porster on 17/2/28.
 */
public class DataManager {
    private final String FXXK_DMG = "questions.json";
    private final String CACHE_GIFTS = "CACHE_GIFTS.AIR";


    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mGiftModels = new ArrayList<>();
    }

    public ArrayList<QuestionModel> getGiftModels() {
        return mGiftModels;
    }

    /**
     * 返回指定数量的题目
     *
     * @param readCount 数量
     * @return 题目
     */
    public ArrayList<QuestionModel> getGiftModels(int readCount) {

        ArrayList<QuestionModel> result = new ArrayList<>(mGiftModels);

        Collections.shuffle(result);

        ArrayList<QuestionModel> data = new ArrayList<>();

        for (int i = 0; i < readCount; i++) {
            QuestionModel giftModel = result.get(i);

            QuestionModel bean = new QuestionModel();
            bean.id = giftModel.id;
            bean.ansers = giftModel.ansers;
            bean.title = giftModel.title;

            List<AnswerModel> choose=new ArrayList<>();
            for (AnswerModel answerModel : giftModel.choose) {
                choose.add(answerModel.copy());
            }
            bean.choose = choose;



            bean.multi=giftModel.multi;

            data.add(bean);
        }

        return data;
    }


    private ArrayList<QuestionModel> mGiftModels;
    private OnAnalysisStateListener mOnAnalysisStateListener;

    public void setOnAnalysisStateListener(OnAnalysisStateListener onAnalysisStateListener) {
        mOnAnalysisStateListener = onAnalysisStateListener;
    }

    //保存数据
    public void saveList(final Context mCtx, final String key, final Object list) {

        //将数据保存到本地
        FileOutputStream fos = null;
        try {

            fos = mCtx.openFileOutput(key, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos) {
                @Override
                protected void writeStreamHeader() throws IOException {
//                            super.writeStreamHeader();
                }
            };

            oos.writeObject(list);

            oos.flush();
            oos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveListAsync(final Context mCtx, final String key, final Object list) {
        XAsyncTask.execute(mCtx, new XAsyncTaskListenerCompat<Object, Object>() {
            @Override
            public Object doInBackground(Context context, Object... objects) {
                saveList(mCtx, key, list);
                return null;
            }
        });
    }

    //保存数据
    public Object readList(final Context mCtx, final String key) throws Exception {
        return queryCacheList(mCtx, key);
    }

    public void readListAsync(final Context mCtx, final String key, final OnReadListener mResult) {
        XAsyncTask.execute(mCtx, new XAsyncTaskListenerCompat<Object, Object>() {
            @Override
            public Object doInBackground(Context context, Object... objects) {
                try {

                    Object list = readList(mCtx, key);
                    return list;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            public void onPostExecute(Context context, Object o) {
                super.onPostExecute(context, o);
                mResult.onSuccess(o);
            }
        });
    }

    private Object queryCacheList(final Context mCtx, final String key) throws IOException, Exception {

        FileInputStream fis = mCtx.openFileInput(key);

        ObjectInputStream ois = new ObjectInputStream(fis) {
            @Override
            protected void readStreamHeader() throws IOException {
//                super.readStreamHeader();
            }
        };

        Object cache = ois.readObject();


        if (cache != null) {
            return cache;
        }
        return null;
    }


    public void init(final Context mCtx) {
        //复制到内存
        XAsyncTask.execute(mCtx, new XAsyncTaskListenerCompat<Object, Object>() {
            long startTime;

            @Override
            public void onPreExecute(Context context) {
                super.onPreExecute(context);
                if (mOnAnalysisStateListener != null) {
                    mOnAnalysisStateListener.onStart();
                }
                startTime = System.currentTimeMillis();
            }

            @Override
            public Object doInBackground(Context context, Object... objects) {
                File fs = new File(mCtx.getFilesDir(), CACHE_GIFTS);
                if (fs.exists()) {

                    try {
                        mGiftModels = (ArrayList<QuestionModel>) readList(mCtx, CACHE_GIFTS);
                        LogCat.i(AppConstants.TAG, "从本地读取完成");
                    } catch (Exception e) {
                        LogCat.i(AppConstants.TAG, "从本地读取异常,即将重试" + fs.delete());
                        init(mCtx);
                        e.printStackTrace();
                    }
                    return null;
                }

                try {
                    //复制到内部存储
                    InputStream in = mCtx.getAssets().open(FXXK_DMG);

                    FileOutputStream out = mCtx.openFileOutput(FXXK_DMG, Context.MODE_PRIVATE);

                    byte[] buf = new byte[1024];

                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.close();
                    in.close();

                    //解密文件

                    //读取内容
                    FileInputStream fin = mCtx.openFileInput(FXXK_DMG);

                    InputStreamReader isr = new InputStreamReader(fin, "utf-8");

                    BufferedReader br = new BufferedReader(isr);

                    StringBuilder stringBuilder=new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        LogCat.i(AppConstants.TAG, line);
                        stringBuilder.append(line);

                    }
                    try {
                        mGiftModels= JSONOpUtils.jsonToList(new JSONArray(stringBuilder.toString()),QuestionModel.class);
                    } catch (UnsupportedTypeException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    };
                    br.close();
                    isr.close();
                    fin.close();

                    saveList(mCtx, CACHE_GIFTS, mGiftModels);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            public void onPostExecute(Context context, Object o) {
                if (mOnAnalysisStateListener != null) {
                    mOnAnalysisStateListener.onEnd();
                }
                long s = (System.currentTimeMillis() - startTime) / 1000;
                LogCat.i(AppConstants.TAG, "总共耗时" + s + "秒");
                LogCat.i(AppConstants.TAG, "解析出" + getGiftModels().size() + "道题目");
            }
        });
        //读取到数据
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 解析进度
     */
    public interface OnAnalysisStateListener {
        void onStart();

        void onEnd();
    }

    /**
     * 解析进度
     */
    public interface OnReadListener {
        void onSuccess(Object list);
    }
}
