package com.jason.loaddatademo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by madong on 2016/10/10.
 */
public class GifBean implements Serializable{

    List<Data> lsitText;

    public GifBean(List<Data> lsitText) {
        this.lsitText = lsitText;
    }

    public List<Data> getLsitText() {
        return lsitText;
    }

    public void setLsitText(List<Data> lsitText) {
        this.lsitText = lsitText;
    }

    @Override
    public String toString() {
        return "GifBean{" +
                "lsitText=" + lsitText +
                '}';
    }

    public static  class Data implements Serializable{
        public Data(String text, String url) {
            this.text = text;
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        String text;
        String url;

        @Override
        public String toString() {
            return "Data{" +
                    "text='" + text + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
