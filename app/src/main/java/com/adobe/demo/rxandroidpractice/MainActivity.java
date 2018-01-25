package com.adobe.demo.rxandroidpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //doSomeWork();

        // flatMap_zipWith_operator();
        zip_filter_flatMapIterable_operator();
        // merge_filter_flatmapIterable();

    }

    void merge_filter_flatmapIterable() {
        Observable.merge(getResult1Observable(), getResult2Observable())
                //.filter(item -> Integer.parseInt(item) % 2 == 0)
                .subscribe(onNext -> {
                    System.out.println(onNext);
                }, e -> {

                });

    }

    void concat_filter_flatmapIterable() {
        Observable.concat(getResult1Observable(), getResult2Observable())
                .filter(item -> Integer.parseInt(item) % 2 == 0)
                .subscribe(onNext -> {
                    System.out.println(onNext);
                }, e -> {

                });

    }

    void zip_filter_flatMapIterable_operator() {
        Single.zip(getResult1Observable().toList(), getResult2Observable().toList(), (List<String> r1, List<String> r2) -> {
            r1.addAll(r2);
            return r1;
        }).toObservable()
                .subscribeOn(Schedulers.io())
                .compose(iterate())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(item -> Integer.parseInt(item.first) % 2 == 0)

                .subscribe(onNext -> {
                    Toast.makeText(MainActivity.this, onNext.first ,Toast.LENGTH_SHORT).show();
                    System.out.println(onNext);
                }, e -> {
                    e.printStackTrace();
                });
    }

    ObservableTransformer<List<String>, Pair<String, Integer>> iterate() {
        return upstream -> upstream.flatMap(list -> {
            return Observable.<String>create(it -> {
                for (String i : list){
                    it.onNext(i);
                }
                it.onComplete();
            }).map(item -> new Pair<String, Integer>(item, new Random().nextInt()));
            //--
            //-


            // --
        });
    }

    void flatMap_zipWith_operator() {
        Observable.fromArray("janishar ali anwar")
                .flatMap(word -> Observable.fromArray(word.split(" ")))
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(
                        val -> System.out.print(val + " "),
                        err -> {
                            System.out.println("nerror ");
                        },
                        () -> System.out.println("completed"));

    }

    Observable<String> getResult1Observable() {
        return Observable.<List<String>>create(it -> {
            it.onNext(getResult1());
            it.onComplete();
        }).flatMapIterable(list -> list)
                .flatMap(item -> Observable.timer(1, TimeUnit.SECONDS).map(__ -> item));
    }

    Observable<String> getResult2Observable() {
        return Observable.<List<String>>create(it -> {
            it.onNext(getResult2());
            it.onComplete();
        }).flatMapIterable(list -> list)
                .flatMap(item -> Observable.timer(1, TimeUnit.SECONDS).map(__ -> item));
    }


    List<String> getResult1() throws InterruptedException {
        List<String> list = new ArrayList<String>(Arrays.asList(new String[]{"1", "2", "3", "4"}));
        return list;
    }

    List<String> getResult2() throws InterruptedException {
        List<String> list = new ArrayList<String>(Arrays.asList(new String[]{"5", "6", "7", "8"}));
        return list;
    }

    // Observable just is a factory method that emmits the values.
    private Observable<String> getObservable() {
        return Observable.just("Cricket", "Football");
    }

    private SingleObserver<Integer> getSingleObserver() {

        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    private MaybeObserver<Integer> getMaybeObserver() {
        return new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private CompletableObserver getCompletableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }


    private Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
    }

    private void doSomeWork() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }
}
