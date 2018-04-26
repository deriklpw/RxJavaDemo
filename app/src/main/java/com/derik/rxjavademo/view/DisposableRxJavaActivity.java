package com.derik.rxjavademo.view;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.derik.rxjavademo.R;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class DisposableRxJavaActivity extends AppCompatActivity {
    private static final String TAG = DisposableRxJavaActivity.class.getSimpleName();

    @BindView(R.id.tv_content)
    TextView mContent;
    private Unbinder mUnbiner;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mUnbiner = ButterKnife.bind(this);
        Log.d(TAG, "onCreate ThreadId=" + Thread.currentThread().getId());
    }

    /**
     * Disposable
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: DisposableRxJava");
        mDisposable.add(getObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext: " + s);
                        mContent.append(s);
                        mContent.append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
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
        Log.d(TAG, "onDestroy: ");
        mDisposable.clear();
        mUnbiner.unbind();
    }

    private Observable<String> getObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                Log.d(TAG, "call: ThreadId=" + Thread.currentThread().getId());
                SystemClock.sleep(5000);
                return Observable.just("item1", "item2", "item3", "item4", "item5");
            }
        });
    }

}
