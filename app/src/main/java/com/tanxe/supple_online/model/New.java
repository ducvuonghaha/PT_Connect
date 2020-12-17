package com.tanxe.supple_online.model;

public class New {
    int image;
    String title;
    String url;

    public New(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public New(int image, String title, String url) {
            this.image = image;
            this.title = title;
            this.url = url;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

