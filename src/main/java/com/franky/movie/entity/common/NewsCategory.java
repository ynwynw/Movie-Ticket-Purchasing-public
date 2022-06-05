package com.franky.movie.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 新闻分类实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_news_category")
@EntityListeners(AuditingEntityListener.class)
public class NewsCategory extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="name",nullable=false,length=32)
	private String name;//新闻分类名称
	
	@Column(name="remark")
	private String remark ;//备注

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "NewsCategory [name=" + name + ", remark=" + remark + "]";
	}

	

	
	
}
