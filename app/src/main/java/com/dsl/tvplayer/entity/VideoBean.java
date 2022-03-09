package com.dsl.tvplayer.entity;

import java.util.Date;

public class VideoBean {

        private int id;
        private String title;
        private String image;
        private String releaseDate;
        private String type;

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        private String url;
        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getImage() {
                return image;
        }

        public void setImage(String image) {
                this.image = image;
        }

        public String getReleaseDate() {
                return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
                this.releaseDate = releaseDate;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }
}
