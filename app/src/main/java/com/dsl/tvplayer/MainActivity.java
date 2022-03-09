package com.dsl.tvplayer;


import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dsl.tvplayer.common.ApiService;
import com.dsl.tvplayer.entity.VideoBean;
import com.dsl.tvplayer.http.BaseObserver;
import com.dsl.tvplayer.http.RetrofitServiceManager;
import com.dsl.tvplayer.http.RxUtils;
import com.dsl.tvplayer.http.listener.RetrofitDownloadListener;
import com.dsl.tvplayer.video.OnViewPagerListener;
import com.dsl.tvplayer.video.VideoAdapter;
import com.dsl.tvplayer.video.VideoController;
import com.dsl.tvplayer.video.ViewPagerLayoutManager;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity implements RetrofitDownloadListener {

    private String  TAG="MainActivity";
    private CompositeDisposable compositeDisposable;
    private Context mContext;
    private IjkVideoView mIjkVideoView;
    private VideoController mVideoController;
    private int mCurrentPosition;
    private RecyclerView mRecyclerView;
    private List<VideoBean> mVideoList;
    private PlayerConfig config;

    @Override
    protected void initView() {

        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.hide();
        }
        setStatusBarTransparent();
        mContext = this;
        mIjkVideoView = new IjkVideoView(this);
        config = new PlayerConfig.Builder().autoRotate().enableMediaCodec().build();
        mIjkVideoView.setPlayerConfig(config);
        mVideoController = new VideoController(this);
        mIjkVideoView.setVideoController(mVideoController);
        mRecyclerView = findViewById(R.id.rv);


        getData();

    }


    private void startPlay(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        FrameLayout frameLayout = itemView.findViewById(R.id.container);
        Glide.with(this)
                .load(mVideoList.get(position).getImage())
                .placeholder(android.R.color.white)
                .into(mVideoController.getThumb());
        ViewParent parent = mIjkVideoView.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(mIjkVideoView);
        }
        frameLayout.addView(mIjkVideoView);
        mIjkVideoView.setUrl(mVideoList.get(position).getUrl());
        mIjkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_CENTER_CROP);
        mIjkVideoView.start();
    }

    /**
     * 把状态栏设成透明
     */
    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = this.getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                    return defaultInsets.replaceSystemWindowInsets(
                            defaultInsets.getSystemWindowInsetLeft(),
                            0,
                            defaultInsets.getSystemWindowInsetRight(),
                            defaultInsets.getSystemWindowInsetBottom());
                }
            });
            ViewCompat.requestApplyInsets(decorView);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public void getData() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .getBanner()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<VideoBean>>(this) {
                    @Override
                    protected void onSuccess(List<VideoBean> videoBeans) {
                        Log.d("print", "--->addSubscribe" + videoBeans.size());
                        mVideoList = videoBeans;

                        VideoAdapter videoAdapter = new VideoAdapter(mVideoList, mContext);
                        ViewPagerLayoutManager layoutManager = new ViewPagerLayoutManager(mContext, OrientationHelper.VERTICAL);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setAdapter(videoAdapter);

                        layoutManager.setOnViewPagerListener(new OnViewPagerListener() {
                            @Override
                            public void onInitComplete() {
                                //自动播放第一条
                                startPlay(0);
                                VideoBean videoBean = mVideoList.get(0);
                                Toasty.info(mContext, videoBean.getTitle(), Toast.LENGTH_LONG, true).show();
                            }

                            @Override
                            public void onPageRelease(boolean isNext, int position) {
                                if (mCurrentPosition == position) {
                                    mIjkVideoView.release();
                                }
                            }

                            @Override
                            public void onPageSelected(int position, boolean isBottom) {
                                if (mCurrentPosition == position) return;
                                startPlay(position);
                                mCurrentPosition = position;
                                VideoBean videoBean = mVideoList.get(position);
                                Toasty.info(mContext, videoBean.getTitle(), Toast.LENGTH_LONG, true).show();
                            }
                        });
                    }
                }));


    }

    /**
     * 封装到base里面效果最佳
     *
     * @param disposable disposable
     */
    private void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        mIjkVideoView.release();
        System.exit(0);
    }


    @Override
    public void onStartDownload() {

    }

    @Override
    public void onProgress(int progress) {
//        runOnUiThread(() -> {
//            mNumberProgressBar.setVisibility(View.VISIBLE);
//            mNumberProgressBar.setProgress(progress);
//            if(progress == 100){
//                mNumberProgressBar.setVisibility(View.GONE);
//                Toast.makeText(baseActivity, "下载完成", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onFinishDownload() {

    }

    @Override
    public void onFail(String errorInfo) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        mIjkVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIjkVideoView.resume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {


            case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键
                /*    实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
                 *    exp:KeyEvent.ACTION_UP
                 */
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "down--->");
                    if (mCurrentPosition < (mVideoList.size() - 1)) {
                        mCurrentPosition = mCurrentPosition + 1;
                        mIjkVideoView.release();
                        startPlay(mCurrentPosition);
                        VideoBean videoBean = mVideoList.get(mCurrentPosition);
                        Toasty.info(mContext, videoBean.getTitle(), Toast.LENGTH_LONG, true).show();

                    } else {
                        Toasty.info(mContext, "我是有底线的", Toast.LENGTH_LONG, true).show();
                    }
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:   //向上键
                if (mCurrentPosition > 0) {
                    mCurrentPosition = mCurrentPosition - 1;
                    mIjkVideoView.release();
                    startPlay(mCurrentPosition);
                    VideoBean videoBean = mVideoList.get(mCurrentPosition);
                    Toasty.info(mContext, videoBean.getTitle(), Toast.LENGTH_LONG, true).show();
                } else {
                    Toasty.info(mContext, "已经到顶了", Toast.LENGTH_LONG, true).show();
                }
                break;

            default:
                break;
        }

        return super.onKeyDown(keyCode, event);

    }
}
