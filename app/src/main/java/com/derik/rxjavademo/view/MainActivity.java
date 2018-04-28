package com.derik.rxjavademo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derik.rxjavademo.R;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    @BindView(R.id.recycleview_list)
    public RecyclerView recyclerView;

    @BindArray(R.array.list_items)
    public String[] mDatas;

    private MyAdapter myAdapter = new MyAdapter();
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        unbinder = ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new OnItemClick() {
            @Override
            public void onItemClick(int position) {
                handleAction(position);
            }
        });
    }

    private void handleAction(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getBaseContext(), SimpleRxJavaActivity.class));
                break;
            case 1:
                startActivity(new Intent(getBaseContext(), MapRxJavaActivity.class));
                break;
            case 2:
                startActivity(new Intent(getBaseContext(), FlatMapRxJavaActivity.class));
                break;
            case 3:
                startActivity(new Intent(getBaseContext(), TakeRxJavaActivity.class));
                break;
            case 4:
                startActivity(new Intent(getBaseContext(), DisposableRxJavaActivity.class));
                break;
            case 5:
                //zip
                break;
            case 6:
                startActivity(new Intent(getBaseContext(), TimerRxJavaActivity.class));
                break;
            case 7:
                startActivity(new Intent(getBaseContext(), DelayRxJavaActivity.class));
                break;
            case 8:
                startActivity(new Intent(getBaseContext(), IntervalRxJavaActivity.class));
                break;
            case 9:
                startActivity(new Intent(getBaseContext(), SingleRxJavaActivity.class));
                break;
            case 10:
                startActivity(new Intent(getBaseContext(), CompletableRxJavaActivity.class));
                break;
            case 11:
                startActivity(new Intent(getBaseContext(), FlowableRxJavaActivity.class));
                break;
            case 12:
                startActivity(new Intent(getBaseContext(), MaybeRxJavaActivity.class));
                break;
            case 13:
                startActivity(new Intent(getBaseContext(), BufferRxJavaActivity.class));
                break;
            case 14:
                startActivity(new Intent(getBaseContext(), FilterRxJavaActivity.class));
                break;
            case 15:
                startActivity(new Intent(getBaseContext(), SkipRxJavaActivity.class));
                break;
            case 16:
                startActivity(new Intent(getBaseContext(), ScanRxJavaActivity.class));
                break;
            case 17:
                startActivity(new Intent(getBaseContext(), ReplayRxJavaActivity.class));
                break;
            case 18:
                break;
            case 19:
                break;
            case 20:
                break;
            case 21:
                break;
            case 22:
                break;
            case 23:
                break;
            case 24:
                break;
            case 25:
                break;
            case 26:
                break;
            case 27:
                break;
            case 28:
                break;
            case 29:
                break;
            case 30:
                break;
            default:
                break;
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private OnItemClick onItemClick;

        public void setOnItemClickListener(OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(position);
                    }
                }
            });

            holder.textView.setText("" + position);
            holder.content.setText(mDatas[position]);

        }

        @Override
        public int getItemCount() {
            return mDatas.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_index)
            TextView textView;
            @BindView(R.id.tv_content)
            TextView content;

            MyHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public void onViewRecycled(@NonNull MyHolder holder) {
            super.onViewRecycled(holder);
        }
    }

    @FunctionalInterface
    public interface OnItemClick {
        void onItemClick(int position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
