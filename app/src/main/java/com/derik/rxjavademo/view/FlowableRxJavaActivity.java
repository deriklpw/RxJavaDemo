package com.derik.rxjavademo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.derik.rxjavademo.R;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class FlowableRxJavaActivity extends AppCompatActivity {
    private static final String TAG = FlowableRxJavaActivity.class.getSimpleName();

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
     * 支持背压，即可避免发送快，接收处理慢，而引发的问题
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: FlowableRxJava");
        getFlowable()
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        return integer % 3 == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getFlowableSubscriber());

        getFlowable()
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer t1, Integer t2) {

                        return t1 + t2;  //归纳求和
                    }
                })
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {

                        }
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d(TAG, "reduce, onSuccess: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }

    private Flowable<Integer> getFlowable() {
        return Flowable.range(1, 18);
    }

    private FlowableSubscriber getFlowableSubscriber() {
        return new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(3);
                mContent.append("request = " + 3);
                mContent.append("\n");
                Log.d(TAG, "onSubscribe: " + s);

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
                mContent.append(integer + "");
                mContent.append("\n");

            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: " + t);

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }
}
