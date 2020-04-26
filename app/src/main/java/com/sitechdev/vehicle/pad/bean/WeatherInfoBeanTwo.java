package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;
import java.util.List;

public class WeatherInfoBeanTwo implements Serializable {

    /**
     * code : 200
     * message :
     * data : {"city":"北京","cityid":101010100,"citycode":null,"date":"2018-10-10","week":"星期三","weather":"多云","temp":"12","temphigh":"17","templow":"4","img":"01","humidity":"27%","pressure":null,"windspeed":null,"winddirect":"东北风","windpower":"1级","updatetime":"2018-10-10 19:30","aqi":null,"index":[{"iname":"洗车指数","ivalue":"较适宜","detail":"无雨且风力较小，易保持清洁度"}],"daily":[{"date":"2018-10-11","week":"明天","sunrise":null,"sunset":null,"night":{"weather":null,"templow":"5","img":null,"winddirect":null,"windpower":null},"day":{"weather":"晴","img":"00","winddirect":"无持续风向","windpower":"0-3级 <5.4m/s","temphigh":"18"}},{"date":"2018-10-12","week":"后天","sunrise":null,"sunset":null,"night":{"weather":null,"templow":"7","img":null,"winddirect":null,"windpower":null},"day":{"weather":"多云","img":"01","winddirect":"南风","windpower":"0-3级 <5.4m/s","temphigh":"20"}},{"date":"2018-10-13","week":"星期六","sunrise":null,"sunset":null,"night":{"weather":null,"templow":"8","img":null,"winddirect":null,"windpower":null},"day":{"weather":"多云","img":"01","winddirect":"南风","windpower":"0-3级 <5.4m/s","temphigh":"20"}}],"hourly":null}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * city : 北京
         * cityid : 101010100
         * citycode : null
         * date : 2018-10-10
         * week : 星期三
         * weather : 多云
         * temp : 12
         * temphigh : 17
         * templow : 4
         * img : 01
         * humidity : 27%
         * pressure : null
         * windspeed : null
         * winddirect : 东北风
         * windpower : 1级
         * updatetime : 2018-10-10 19:30
         * alarm: "大风预警”
         * aqi : null
         * index : [{"iname":"洗车指数","ivalue":"较适宜","detail":"无雨且风力较小，易保持清洁度"}]
         * daily : [{"date":"2018-10-11","week":"明天","sunrise":null,"sunset":null,"night":{"weather":null,"templow":"5","img":null,"winddirect":null,"windpower":null},"day":{"weather":"晴","img":"00","winddirect":"无持续风向","windpower":"0-3级 <5.4m/s","temphigh":"18"}},{"date":"2018-10-12","week":"后天","sunrise":null,"sunset":null,"night":{"weather":null,"templow":"7","img":null,"winddirect":null,"windpower":null},"day":{"weather":"多云","img":"01","winddirect":"南风","windpower":"0-3级 <5.4m/s","temphigh":"20"}},{"date":"2018-10-13","week":"星期六","sunrise":null,"sunset":null,"night":{"weather":null,"templow":"8","img":null,"winddirect":null,"windpower":null},"day":{"weather":"多云","img":"01","winddirect":"南风","windpower":"0-3级 <5.4m/s","temphigh":"20"}}]
         * hourly : null
         */

        private String city;
        private int cityid;
        private Object citycode;
        private String date;
        private String week;
        private String weather;
        private String temp;
        private String temphigh;
        private String templow;
        private String img;
        private String humidity;
        private Object pressure;
        private Object windspeed;
        private String winddirect;
        private String windpower;
        private String updatetime;
        private List<String> alarm = null;
        private AqiBean aqi;
        private Object hourly;
        private List<IndexBean> index;
        private List<DailyBean> daily;

        public List<String> getAlarm() {
            return alarm;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCityid() {
            return cityid;
        }

        public void setCityid(int cityid) {
            this.cityid = cityid;
        }

        public Object getCitycode() {
            return citycode;
        }

        public void setCitycode(Object citycode) {
            this.citycode = citycode;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTemphigh() {
            return temphigh;
        }

        public void setTemphigh(String temphigh) {
            this.temphigh = temphigh;
        }

        public String getTemplow() {
            return templow;
        }

        public void setTemplow(String templow) {
            this.templow = templow;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public Object getPressure() {
            return pressure;
        }

        public void setPressure(Object pressure) {
            this.pressure = pressure;
        }

        public Object getWindspeed() {
            return windspeed;
        }

        public void setWindspeed(Object windspeed) {
            this.windspeed = windspeed;
        }

        public String getWinddirect() {
            return winddirect;
        }

        public void setWinddirect(String winddirect) {
            this.winddirect = winddirect;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public AqiBean getAqi() {
            return aqi;
        }

        public void setAqi(AqiBean aqi) {
            this.aqi = aqi;
        }

        public Object getHourly() {
            return hourly;
        }

        public void setHourly(Object hourly) {
            this.hourly = hourly;
        }

        public List<IndexBean> getIndex() {
            return index;
        }

        public void setIndex(List<IndexBean> index) {
            this.index = index;
        }

        public List<DailyBean> getDaily() {
            return daily;
        }

        public void setDaily(List<DailyBean> daily) {
            this.daily = daily;
        }

        public static class AqiBean implements Serializable {

            /**
             * "so2": null,
             * "so224": null,
             * "no2": null,
             * "no224": null,
             * "co": null,
             * "co24": null,
             * "o3": null,
             * "o38": null,
             * "o324": null,
             * "pm10": null,
             * "pm1024": null,
             * "pm25": null,
             * "pm2524": null,
             * "iso2": null,
             * "ino2": null,
             * "ico": null,
             * "io3": null,
             * "io38": null,
             * "ipm10": null,
             * "ipm25": null,
             * "aqi": "35",
             * "primarypollutant": null,
             * "quality": "优质",
             * "timepoint": null,
             * "aqiinfo": null
             */

            private String so2;
            private String so224;
            private String no2;
            private String no224;
            private String co;
            private String co24;
            private String o3;
            private String o38;
            private String o324;
            private String pm10;
            private String pm1024;
            private String pm2_5;
            private String pm2_524;
            private String iso2;
            private String ino2;
            private String io3;
            private String io38;
            private String ipm10;
            private String ipm2_5;
            private String aqi;
            private String primarypollutant;
            private String quality;
            private String timepoint;
            private Object aqiinfo;

            public String getQuality() {
                return quality;
            }

            public String getAqi() {
                return aqi;
            }
        }

        public static class IndexBean implements Serializable {
            /**
             * iname : 洗车指数
             * ivalue : 较适宜
             * detail : 无雨且风力较小，易保持清洁度
             */

            private String iname;
            private String ivalue;
            private String detail;

            public String getIname() {
                return iname;
            }

            public void setIname(String iname) {
                this.iname = iname;
            }

            public String getIvalue() {
                return ivalue;
            }

            public void setIvalue(String ivalue) {
                this.ivalue = ivalue;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }
        }

        public static class DailyBean implements Serializable {
            /**
             * date : 2018-10-11
             * week : 明天
             * sunrise : null
             * sunset : null
             * night : {"weather":null,"templow":"5","img":null,"winddirect":null,"windpower":null}
             * day : {"weather":"晴","img":"00","winddirect":"无持续风向","windpower":"0-3级 <5.4m/s","temphigh":"18"}
             * quality : 优质
             */

            private String date;
            private String week;
            private Object sunrise;
            private Object sunset;
            private NightBean night;
            private DayBean day;
            private String quality;

            public String getQuality() {
                return quality;
            }

            public void setQuality(String quality) {
                this.quality = quality;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public Object getSunrise() {
                return sunrise;
            }

            public void setSunrise(Object sunrise) {
                this.sunrise = sunrise;
            }

            public Object getSunset() {
                return sunset;
            }

            public void setSunset(Object sunset) {
                this.sunset = sunset;
            }

            public NightBean getNight() {
                return night;
            }

            public void setNight(NightBean night) {
                this.night = night;
            }

            public DayBean getDay() {
                return day;
            }

            public void setDay(DayBean day) {
                this.day = day;
            }

            public static class NightBean implements Serializable {
                /**
                 * weather : null
                 * templow : 5
                 * img : null
                 * winddirect : null
                 * windpower : null
                 */

                private Object weather;
                private String templow;
                private Object img;
                private Object winddirect;
                private Object windpower;

                public Object getWeather() {
                    return weather;
                }

                public void setWeather(Object weather) {
                    this.weather = weather;
                }

                public String getTemplow() {
                    return templow;
                }

                public void setTemplow(String templow) {
                    this.templow = templow;
                }

                public Object getImg() {
                    return img;
                }

                public void setImg(Object img) {
                    this.img = img;
                }

                public Object getWinddirect() {
                    return winddirect;
                }

                public void setWinddirect(Object winddirect) {
                    this.winddirect = winddirect;
                }

                public Object getWindpower() {
                    return windpower;
                }

                public void setWindpower(Object windpower) {
                    this.windpower = windpower;
                }
            }

            public static class DayBean implements Serializable {
                /**
                 * weather : 晴
                 * img : 00
                 * winddirect : 无持续风向
                 * windpower : 0-3级 <5.4m/s
                 * temphigh : 18
                 */

                private String weather;
                private String img;
                private String winddirect;
                private String windpower;
                private String temphigh;

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getWinddirect() {
                    return winddirect;
                }

                public void setWinddirect(String winddirect) {
                    this.winddirect = winddirect;
                }

                public String getWindpower() {
                    return windpower;
                }

                public void setWindpower(String windpower) {
                    this.windpower = windpower;
                }

                public String getTemphigh() {
                    return temphigh;
                }

                public void setTemphigh(String temphigh) {
                    this.temphigh = temphigh;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "WeatherInfoBeanTwo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
