package com.fih.idx.rxjavademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.tv_tittle)
    public TextView title;
    @BindView(R.id.recycleview_list)
    public RecyclerView recyclerView;

    @BindArray(R.array.list_items)
    public String[] mDatas;

    private MyAdapter myAdapter = new MyAdapter();
    private Unbinder unbinder;
    private ArrayList<Bitmap> bitmapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        unbinder = ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(this::handleAction);
    }

    private void handleAction(int position) {
        switch (position) {
            case 0:
                Log.d(TAG, "handleAction: test RxJava0");
                testRxJava0();
                break;
            case 1:
                Log.d(TAG, "handleAction: test RxJava1");
                testRxJava1();

                break;
            case 2:
                testRxJava2();
                break;
            case 3:
                testRxJava3();
                break;
            case 4:
                testRxJava4();
                break;
            case 5:
                testRxJava5(position);
                break;
            case 6:
                testRxJava4();
                break;
            case 7:
                testRxJava4();
                break;
            case 8:
                testRxJava4();
                break;
            case 9:
                testRxJava4();
                break;
            case 10:
                testRxJava4();
                break;

            default:
                break;
        }
    }

    // 与Retrofit2.0+RxJava结合使用
    private void testRxJava5(int position) {
        Log.d(TAG, "testRxJava5: " + position);

    }

    // 事件序列的再变换，flatMap()
    private void testRxJava4() {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("chinese", 80.6f));
        courses.add(new Course("english", 80.0f));
        courses.add(new Course("history", 80.5f));
        Student student1 = new Student.Builder()
                .setAge(25)
                .setName("student1")
                .setGender("male")
                .setCourses(courses).build();

        courses.clear();
        courses.add(new Course("chinese", 81.6f));
        courses.add(new Course("english", 81.0f));
        courses.add(new Course("history", 81.5f));
        Student student2 = new Student.Builder()
                .setAge(26)
                .setName("student2")
                .setGender("male")
                .setCourses(courses).build();

        courses.clear();
        courses.add(new Course("chinese", 82.6f));
        courses.add(new Course("english", 82.0f));
        courses.add(new Course("history", 82.5f));
        Student student3 = new Student.Builder()
                .setAge(22)
                .setName("student3")
                .setGender("female")
                .setCourses(courses).build();

        ArrayList<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);

        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(new Action1<Course>() {
                    @Override
                    public void call(Course course) {
                        Log.d(TAG, "call: " + course.getName() + ":" + course.getScore());
                    }
                });

    }

    // 事件序列的变换，map()
    private void testRxJava3() {
        Observable.just("ic_launcher", "ic_launcher_round")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String res) {
                        int id = getResources().getIdentifier(res, "mipmap", getPackageName());
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
                        return bitmap;

                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        Log.d(TAG, "testRxJava4: " + bitmap.getByteCount());
                    }
                });
    }

    // 为事件产生和消费指定线程
    private void testRxJava2() {
        Observable.just("test1", "test2", "test3")
                .subscribeOn(Schedulers.io()) //指定事件发生的线程，只能调用一次
                .observeOn(AndroidSchedulers.mainThread()) //指定消费所在的线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "testRxJava3: " + s);
                    }
                });
    }

    // 发送bitmap对象
    private void testRxJava1() {
        mDatas[0] = "12";
        int[] resIds = {android.R.drawable.ic_media_next,
                android.R.drawable.ic_media_pause, android.R.drawable.ic_media_play,
                android.R.drawable.ic_media_previous, android.R.drawable.ic_media_rew
        };
        if (bitmapList == null) {
            bitmapList = new ArrayList<>();
        } else {
            bitmapList.clear();
        }
        for (int i = 0; i < resIds.length; i++) {
            bitmapList.add(BitmapFactory.decodeResource(getResources(), resIds[i]));
        }
        myAdapter.notifyDataSetChanged();

        Observable.from(bitmapList).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Log.d(TAG, "testRxJava2: " + bitmap.getByteCount());
            }
        });
    }

    // 基础用法
    private void testRxJava0() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        // 此种方式，内部会先转换成Subscriber对象
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }
        };

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };

        observable.subscribe(mySubscriber);
        // 内部会将Observer先转换为Subscriber对象
        observable.subscribe(observer);
        // 不完整定义的回调，RxJava会依据定义，自动创建出Subscriber
        observable.subscribe(new Action1<String>() {
                                 @Override
                                 public void call(String s) {
                                     Log.d(TAG, "testRxJava: s=" + s);
                                 }
                             },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d(TAG, "testRxJava: error");
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "testRxJava: completed");
                    }
                });

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
            holder.itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onItemClick(position);
                }
            });

            holder.textView.setText(mDatas[position]);
            if (bitmapList != null && position < bitmapList.size()) {
                holder.imageView.setImageBitmap(bitmapList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_content)
            TextView textView;
            @BindView(R.id.iv_icon)
            ImageView imageView;

            MyHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
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
