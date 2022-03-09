package com.dsl.tvplayer;

import com.dsl.tvplayer.entity.VideoBean;
import com.dsl.tvplayer.http.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        List<VideoBean> videoBeans=new ArrayList<>();
        VideoBean videoBean=new VideoBean();
        videoBean.setId(0);
        videoBean.setImage("https://gitee.com/cnkker/video/raw/master/list/hdmovie.png");
        videoBean.setUrl("http://aldirect.hls.huya.com/huyalive/94525224-2460685313-10568562945082523648-2789274524-10057-A-0-1_1200.m3u8");
        videoBean.setReleaseDate("2019-03-05");
        videoBean.setTitle("周星驰电影");
        videoBean.setType("动作 / 冒险 / 奇幻");
        videoBeans.add(videoBean);
        Gson gson=new Gson();
        BaseResponse<List<VideoBean>> baseResponse=new BaseResponse<>();
        baseResponse.setCode(0);
        baseResponse.setData(videoBeans);
        baseResponse.setMsg("OK");
        String json= gson.toJson(baseResponse);

        System.out.printf(json);
    }
    @Test
    public void testJson(){
        String json="[{\"id\":0,\"title\":\"周星驰电影\",\"image\":\"https://gitee.com/cnkker/video/raw/master/list/hdmovie.png\",\"releaseDate\":\"2019-03-05\",\"url\":\"http://aldirect.hls.huya.com/huyalive/94525224-2460685313-10568562945082523648-2789274524-10057-A-0-1_1200.m3u8\",\"type\":\"动作 / 冒险 / 奇幻\"}]";
        Gson gson=new Gson();
        List<VideoBean> videoBeans= gson.fromJson(json, new TypeToken<List<VideoBean>>(){}.getType());
        System.out.printf(videoBeans.size()+"");
    }
}