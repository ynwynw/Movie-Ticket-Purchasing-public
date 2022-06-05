package com.franky.movie.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 地域实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_area")
@EntityListeners(AuditingEntityListener.class)
public class Area extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="province_id")
	private Long provinceId;//省份id
	
	@Column(name="city_id")
	private Long cityId;//城市id
	
	@Column(name="name",nullable=false,length=32)
	private String name;//地域名称
	
	@Column(name="is_show")
	private Boolean isShow = true;//是否显示

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	@Override
	public String toString() {
		return "Area [provinceId=" + provinceId + ", cityId=" + cityId
				+ ", name=" + name + ", isShow=" + isShow + "]";
	}

	
	
}
