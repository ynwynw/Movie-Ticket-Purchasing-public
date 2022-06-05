package com.franky.movie.entity.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.franky.movie.bean.CinemaServices;

/**
 * 电影院实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_cinema")
@EntityListeners(AuditingEntityListener.class)
public class Cinema extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="name",nullable=false,length=64)
	private String name;//电影院名称
	
	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area;//电影院地区
	
	@Column(name="address",nullable=false,length=128)
	private String address;//地址
	
	@Column(name="tel",nullable=false,length=16)
	private String tel;//电话
	
	@Column(name="transport",length=128)
	private String transport;//交通路线
	
	@Column(name="cinema_service",length=128)
	private String cinemaService;//影院服务
	
	@Column(name="info",length=1280)
	private String info;//介绍
	
	@Column(name="picture",length=512)
	private String picture;//照片
	
	@Column(name="rate",length=12)
	private BigDecimal rate = new BigDecimal(0);//评分

	@Column(name="rate_count")
	private Integer rateCount = 0;//评论数量
	
	@Transient
	private List<CinemaServices> cinemaServiceList;//用于直接显示在页面的电影院服务.不需要保存到数据库

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getCinemaService() {
		return cinemaService;
	}

	public void setCinemaService(String cinemaService) {
		this.cinemaService = cinemaService;
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

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getRateCount() {
		return rateCount;
	}

	public void setRateCount(Integer rateCount) {
		this.rateCount = rateCount;
	}

	public List<CinemaServices> getCinemaServiceList() {
		if(this.cinemaService != null){
			cinemaServiceList = new ArrayList<CinemaServices>();
			String[] split = this.cinemaService.split(",");
			for(String t : split){
				CinemaServices cinemaService = CinemaServices.valueOf(t);
				if(cinemaService != null){
					cinemaServiceList.add(cinemaService);
				}
			}
		}
		return cinemaServiceList;
	}

	public void setCinemaServiceList(List<CinemaServices> cinemaServiceList) {
		this.cinemaServiceList = cinemaServiceList;
	}

	@Override
	public String toString() {
		return "Cinema [name=" + name + ", area=" + area + ", address="
				+ address + ", tel=" + tel + ", transport=" + transport
				+ ", cinemaService=" + cinemaService + ", info=" + info
				+ ", picture=" + picture + ", rate=" + rate
				+ ", cinemaServiceList=" + cinemaServiceList + "]";
	}
	
	

	

	
	
}
