package com.derik.rxjavademo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.derik.rxjavademo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class TimerRxJavaActivity extends AppCompatActivity {
    private static final String TAG = TimerRxJavaActivity.class.getSimpleName();

    @BindView(R.id.tv_content)
    TextView mContent;
    private Unbinder mUnbiner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mUnbiner = ButterKnife.bind(this);
    }

    /**
     * Timer, 延迟一定时间后，开始发送item（0），并结束
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: TimerRxJava");
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long s) throws Exception {
                        Log.d(TAG, "onNext: " + s);
                        mContent.append("" + s);
                        mContent.append("\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "onError: " + throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }

    private Observable<? extends Long> getObservable() {
        return Observable.timer(2000, TimeUnit.MILLISECONDS);
    }
}
