package com.derik.rxjavademo.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class MapRxJavaActivity extends AppCompatActivity {
    private static final String TAG = MapRxJavaActivity.class.getSimpleName();

    @BindView(R.id.tv_content)
    TextView mContent;
    private Unbinder mUnbiner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mUnbiner = ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: MapRxJava");
        Observable.just("ic_launcher", "ic_launcher_round")
                .subscribeOn(Schedulers.io())
                .map(res -> {
                    int id = getResources().getIdentifier(res, "mipmap", getPackageName());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
                    return bitmap;

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    Log.d(TAG, "onNext, " + bitmap.getByteCount());
                    mContent.append(""+bitmap.getByteCount());
                    mContent.append("\n");
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }
}
