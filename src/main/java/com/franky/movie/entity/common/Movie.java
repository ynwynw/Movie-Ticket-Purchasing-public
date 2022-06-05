package com.franky.movie.entity.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.franky.movie.bean.MovieArea;
import com.franky.movie.bean.MovieLang;
import com.franky.movie.bean.MovieType;

/**
 * 电影实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_movie")
@EntityListeners(AuditingEntityListener.class)
public class Movie extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="name",nullable=false,length=64)
	private String name;//电影名称
	
	@Column(name="abs",length=128)
	private String abs;//电影摘要
	
	@Column(name="type",length=128)
	private String type;//电影类型
	
	@Column(name="area",length=12)
	private MovieArea area;//电影地区
	
	@Column(name="directed_by",nullable=false,length=64)
	private String directedBy;//导演
	
	@Column(name="actor",nullable=false,length=512)
	private String actor;//演员
	
	@Column(name="language",nullable=false,length=32)
	private MovieLang language;//语言
	
	@Column(name="time")
	private Integer time;//片长
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name="show_time")
	private Date showTime;//上映时间
	
	@Column(name="info",length=1280)
	private String info;//剧情介绍
	
	@Column(name="picture",length=512)
	private String picture;//照片
	
	@Column(name="video",length=512)
	private String video;//视频
	
	@Column(name="total_money",length=12)
	private BigDecimal totalMoney = new BigDecimal(0);//累计票房
	
	@Column(name="rate",length=12)
	private BigDecimal rate = new BigDecimal(0);//评分
	
	@Column(name="rate_count")
	private Integer rateCount = 0;//评论数量
	
	@Column(name="is_show")
	private Boolean isShow = true;//是否正在热映

	@Transient
	private List<MovieType> typeList;//用于直接显示在页面的电影类型.不需要保存到数据库
	
	@Transient
	private List<String> pictureList;//用于直接显示在页面的电影剧照.不需要保存到数据库
	
	@Transient
	private String mainPic;//主图
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public String getDirectedBy() {
		return directedBy;
	}

	public void setDirectedBy(String directedBy) {
		this.directedBy = directedBy;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	

	public MovieLang getLanguage() {
		return language;
	}

	public void setLanguage(MovieLang language) {
		this.language = language;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public MovieArea getArea() {
		return area;
	}

	public void setArea(MovieArea area) {
		this.area = area;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public List<MovieType> getTypeList() {
		if(this.type != null){
			typeList = new ArrayList<MovieType>();
			String[] split = this.type.split(",");
			for(String t : split){
				MovieType movieType = MovieType.valueOf(t);
				if(movieType != null){
					typeList.add(movieType);
				}
			}
		}
		return typeList;
	}

	public void setTypeList(List<MovieType> typeList) {
		this.typeList = typeList;
	}

	
	
	public List<String> getPictureList() {
		if(!StringUtils.isEmpty(this.picture)){
			pictureList = new ArrayList<String>();
			String[] split = this.picture.split(";");
			for(String p : split){
				pictureList.add(p);
			}
		}
		return pictureList;
	}

	public void setPictureList(List<String> pictureList) {
		this.pictureList = pictureList;
	}

	public String getMainPic() {
		if(this.getPictureList() != null){
			return this.getPictureList().get(0);
		}
		return mainPic;
	}

	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	
	public Integer getRateCount() {
		return rateCount;
	}

	public void setRateCount(Integer rateCount) {
		this.rateCount = rateCount;
	}

	@Override
	public String toString() {
		return "Movie [name=" + name + ", abs=" + abs + ", type=" + type
				+ ", area=" + area + ", directedBy=" + directedBy + ", actor="
				+ actor + ", language=" + language + ", time=" + time
				+ ", showTime=" + showTime + ", info=" + info + ", picture="
				+ picture + ", video=" + video + ", totalMoney=" + totalMoney
				+ ", rate=" + rate + "]";
	}

	

	
	
}
