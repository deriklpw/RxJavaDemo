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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class IntervalRxJavaActivity extends AppCompatActivity {
    private static final String TAG = IntervalRxJavaActivity.class.getSimpleName();

    @BindView(R.id.tv_content)
    TextView mContent;
    private Unbinder mUnbiner;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mUnbiner = ButterKnife.bind(this);
    }

    /**
     * 指定间隔时间内，发送数字(0,1,2...)
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: IntervalRxJava");
        disposables.add(getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long l) {
                        Log.d(TAG, "onNext: " + l);
                        mContent.append("" + l);
                        mContent.append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        mUnbiner.unbind();
    }

    private Observable<? extends Long> getObservable() {
        return Observable.interval(1, TimeUnit.SECONDS);
    }
}
