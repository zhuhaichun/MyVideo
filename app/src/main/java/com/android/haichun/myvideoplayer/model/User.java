package com.android.haichun.myvideoplayer.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @Override
    public String toString() {
        return "User{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    /**
     * code : 200
     * message : 成功!
     * result : {"name":"peakchao","nikeName":"飞翔的水牛","headerImg":"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png","phone":"13888888888","email":"123456@qq.com","vipGrade":"6","autograph":"没有个性，没有签名","remarks":"这是一个备注，啦啦啦啦~"}
     */

    private int code;
    private String message;
    private ResultBean result;
    private String password;
    private List<Video.ResultBean.DataBeanX.ContentBean.DataBean> collection;
    private List<Video.ResultBean.DataBeanX.ContentBean.DataBean> hearts;

    public List<Video.ResultBean.DataBeanX.ContentBean.DataBean> getCollection() {
        return collection;
    }

    public void setCollection(List<Video.ResultBean.DataBeanX.ContentBean.DataBean> collection) {
        this.collection = collection;
    }

    public List<Video.ResultBean.DataBeanX.ContentBean.DataBean> getHearts() {
        return hearts;
    }

    public void setHearts(List<Video.ResultBean.DataBeanX.ContentBean.DataBean> hearts) {
        this.hearts = hearts;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class ResultBean implements Serializable{
        /**
         * name : peakchao
         * nikeName : 飞翔的水牛
         * headerImg : https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png
         * phone : 13888888888
         * email : 123456@qq.com
         * vipGrade : 6
         * autograph : 没有个性，没有签名
         * remarks : 这是一个备注，啦啦啦啦~
         */

        private String name;
        private String nikeName;
        private String headerImg;
        private String phone;
        private String email;
        private String vipGrade;
        private String autograph;
        private String remarks;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNikeName() {
            return nikeName;
        }

        public void setNikeName(String nikeName) {
            this.nikeName = nikeName;
        }

        public String getHeaderImg() {
            return headerImg;
        }

        public void setHeaderImg(String headerImg) {
            this.headerImg = headerImg;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getVipGrade() {
            return vipGrade;
        }

        public void setVipGrade(String vipGrade) {
            this.vipGrade = vipGrade;
        }

        public String getAutograph() {
            return autograph;
        }

        public void setAutograph(String autograph) {
            this.autograph = autograph;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "name='" + name + '\'' +
                    ", nikeName='" + nikeName + '\'' +
                    ", headerImg='" + headerImg + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", vipGrade='" + vipGrade + '\'' +
                    ", autograph='" + autograph + '\'' +
                    ", remarks='" + remarks + '\'' +
                    '}';
        }
    }
}
