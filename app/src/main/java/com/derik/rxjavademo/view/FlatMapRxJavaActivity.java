package com.derik.rxjavademo.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.derik.rxjavademo.R;
import com.derik.rxjavademo.data.Course;
import com.derik.rxjavademo.data.Student;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by derik on 18-4-3.
 * Email: weilai0314@163.com
 */

public class FlatMapRxJavaActivity extends AppCompatActivity {
    private static final String TAG = FlatMapRxJavaActivity.class.getSimpleName();
    @BindView(R.id.tv_content)
    TextView mContent;
    Unbinder mUnbiner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mUnbiner = ButterKnife.bind(this);
    }

    /**
     * FlatMap，事件对象一对多转换
     * <p>
     * 依据Student对象，返回一个新的Observable，
     */
    @OnClick(R.id.bt_action)
    public void action(View view) {
        Log.d(TAG, "action: FlatMapRxJava");
        Observable.fromIterable(getStudents())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Student, ObservableSource<Course>>() {
                    @Override
                    public ObservableSource<Course> apply(Student student) throws Exception {
                        Log.d(TAG, "apply: " +student);
                        return Observable.fromIterable(student.getCourses());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Course>() {
                    @Override
                    public void accept(Course course) throws Exception {
                        String msg = "onNext, " + course.getName() + ":" + course.getScore();
                        Log.d(TAG, "onNext: " + msg);
                        mContent.append(msg);
                        mContent.append("\n");
                    }
                });
    }

    @NonNull
    private ArrayList<Student> getStudents() {
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
        return students;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbiner.unbind();
    }
}
