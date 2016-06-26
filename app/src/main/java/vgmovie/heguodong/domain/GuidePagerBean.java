package vgmovie.heguodong.domain;

import java.util.List;

/**
 * Created by heGuoDong_start  on 2016/6/22 21:13.
 *
 * 在这里是 GuidePager  的bean。
 */
public class GuidePagerBean {


    /**
     * id : 888
     * title : 6.23 海底启动图
     * start : 1466611200
     * times : 15
     * pic : http://p1.meituan.net/movie/1a7f9dd0d7a3949bf7ae4ba6a28eda77137128.jpg
     * type : 2
     * isShowLogo : 0
     * end : 1466697540
     * url :
     */

    private List<PostersBean> posters;

    public List<PostersBean> getPosters() {
        return posters;
    }

    public void setPosters(List<PostersBean> posters) {
        this.posters = posters;
    }

    public static class PostersBean {
        private int id;
        private String title;
        private int start;
        private int times;
        private String pic;
        private int type;
        private int isShowLogo;
        private int end;
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

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsShowLogo() {
            return isShowLogo;
        }

        public void setIsShowLogo(int isShowLogo) {
            this.isShowLogo = isShowLogo;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
