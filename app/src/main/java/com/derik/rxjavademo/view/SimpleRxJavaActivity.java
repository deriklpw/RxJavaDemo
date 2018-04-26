package com.derik.rxjavademo.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.derik.rxjavademo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class SimpleRxJavaActivity extends AppCompatActivity {

    private static final String TAG = SimpleRxJavaActivity.class.getSimpleName();
    @BindView(R.id.tv_content)
    TextView mContent;
    Unbinder mUnbiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mUnbiner = ButterKnife.bind(this);
    }

    /**
     * 基础用法
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: SimpleRxJava");
        Observable<String> observable = getObservable();
        Observer<String> observer = getStringObserver();

        //specify run the background thread
        observable.subscribeOn(Schedulers.io())
                //specify be notified on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        observable.subscribe(new Consumer<String>() {
                                 @Override
                                 public void accept(String s) throws Exception {
                                     Log.d(TAG, "onNext, s=" + s);
                                     mContent.append(s);
                                     mContent.append("\n");

                                 }
                             },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "onError, " + throwable);
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "completed.");
                    }
                });

    }

    @NonNull
    private Observer<String> getStringObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext, Item: " + s);
                mContent.append(s);
                mContent.append("\n");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete.");
            }
        };
    }

    private Observable<String> getObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext("Hello");
                    e.onNext("Hi");
                    e.onNext("Peter");
                    e.onComplete();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }
}
