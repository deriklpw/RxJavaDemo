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
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class CompletableRxJavaActivity extends AppCompatActivity {
    private static final String TAG = CompletableRxJavaActivity.class.getSimpleName();

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
     * 没有items，没有onNext，只有onComplete， onError
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: CompletableRxJava");
        getCompletable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        mContent.append("It is Completable, no items and no onNext");
                        mContent.append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }

    private Completable getCompletable() {
        return Completable.timer(1000, TimeUnit.MILLISECONDS);
    }
}
