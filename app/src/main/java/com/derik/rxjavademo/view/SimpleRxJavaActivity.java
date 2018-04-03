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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: SimpleRxJava");
        Observable<String> observable = getObservable();
        // 此种方式，内部会先转换成Subscriber对象
        Observer<String> observer = getStringObserver();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        // 不完整定义的回调，RxJava会依据定义，自动创建出Subscriber
        observable.subscribe(o -> {
                    Log.d(TAG, "onNext, s=" + o);
                    mContent.append(o);
                    mContent.append("\n");
                },
                throwable -> Log.d(TAG, "onError, " + throwable),
                () -> Log.d(TAG, "completed."));

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
        return Observable.create(e -> {
            if (!e.isDisposed()) {
                e.onNext("Hello");
                e.onNext("Hi");
                e.onNext("Aloha");
                e.onComplete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }
}
