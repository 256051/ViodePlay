package com.dsl.tvplayer.common;


import com.dsl.tvplayer.entity.BannerEntity;
import com.dsl.tvplayer.entity.VideoBean;
import com.dsl.tvplayer.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/3/23.
 */

public interface ApiService {

    String BASR_URL = "https://gitee.com/cnkker/";



    //缓存一个小时
    @Headers("Cache-Control: public, max-age=3600")
    @GET("video/raw/master/list/movie.json")
    Observable<BaseResponse<List<VideoBean>>> getBanner();



    @Streaming
    @GET
    Observable<ResponseBody> downLoad(@Url String url);

}
