package com.alessiogrumiro.udacity.popularmovies.services.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Alessio Grumiro on 02/02/17.
 */

public class ConfigResponseContainer {

    @SerializedName("change_keys")
    private List<String> changeKeys;

    @SerializedName("images")
    private ImageParams imageParams;

    public ImageParams getImageParams() {
        return imageParams;
    }

    public void setImageParams(ImageParams imageParams) {
        this.imageParams = imageParams;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }

    public class ImageParams {

        @SerializedName("base_url")
        private String baseUrl;

        @SerializedName("secure_base_url")
        private String baseUrlSecure;

        @SerializedName("backdrop_sizes")
        private List<String> backdropSizes;

        @SerializedName("logo_sizes")
        private List<String> logoSizes;

        @SerializedName("poster_sizes")
        private List<String> posterSizes;

        @SerializedName("profile_sizes")
        private List<String> profileSizes;

        @SerializedName("still_sizes")
        private List<String> stillSizes;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getBaseUrlSecure() {
            return baseUrlSecure;
        }

        public void setBaseUrlSecure(String baseUrlSecure) {
            this.baseUrlSecure = baseUrlSecure;
        }

        public List<String> getBackdropSizes() {
            return backdropSizes;
        }

        public void setBackdropSizes(List<String> backdropSizes) {
            this.backdropSizes = backdropSizes;
        }

        public List<String> getLogoSizes() {
            return logoSizes;
        }

        public void setLogoSizes(List<String> logoSizes) {
            this.logoSizes = logoSizes;
        }

        public List<String> getPosterSizes() {
            return posterSizes;
        }

        public void setPosterSizes(List<String> posterSizes) {
            this.posterSizes = posterSizes;
        }

        public List<String> getProfileSizes() {
            return profileSizes;
        }

        public void setProfileSizes(List<String> profileSizes) {
            this.profileSizes = profileSizes;
        }

        public List<String> getStillSizes() {
            return stillSizes;
        }

        public void setStillSizes(List<String> stillSizes) {
            this.stillSizes = stillSizes;
        }
    }
}
