package com.franky.movie.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.franky.movie.annotion.ValidateEntity;

/**
 * 新闻实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_news")
@EntityListeners(AuditingEntityListener.class)
public class News extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="news_category_id")
	private NewsCategory newsCategory;//所属分类
	
	@ValidateEntity(required=true,maxLength=126,errorMaxLengthMsg="新闻标题字数不能大于126个字符")
	@Column(name="title",nullable=false,length=128)
	private String title;//新闻标题
	
	@ValidateEntity(required=true,maxLength=252,errorMaxLengthMsg="新闻摘要字数不能大于252个字符")
	@Column(name="abs",nullable=false,length=256)
	private String abs ;//新闻摘要
	
	@ValidateEntity(required=true)
	@Column(name="picture",nullable=false,length=128)
	private String picture ;//新闻封面图片

	@ValidateEntity(required=true,maxLength=2500,errorMaxLengthMsg="新闻内容字数不能大于2500个字符")
	@Column(name="content",nullable=false,length=2560)
	private String content ;//新闻内容
	
	@Column(name="view_num")
	private int viewNumber ;//浏览量

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getViewNumber() {
		return viewNumber;
	}

	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}

	public NewsCategory getNewsCategory() {
		return newsCategory;
	}

	public void setNewsCategory(NewsCategory newsCategory) {
		this.newsCategory = newsCategory;
	}

	@Override
	public String toString() {
		return "News [title=" + title + ", abs=" + abs + ", picture=" + picture
				+ ", content=" + content + ", viewNumber=" + viewNumber + "]";
	}
	
	
	
	
}
