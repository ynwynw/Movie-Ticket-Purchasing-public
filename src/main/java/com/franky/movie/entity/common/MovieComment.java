package com.franky.movie.entity.common;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 电影评价实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_movie_comment")
@EntityListeners(AuditingEntityListener.class)
public class MovieComment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="movie_id",nullable=false)
	private Movie movie;//所属电影
	
	@ManyToOne
	@JoinColumn(name="account_id",nullable=false)
	private Account account;//所属用户
	
	@Column(name="rate",nullable=false)
	private BigDecimal rate;//评分
	
	@Column(name="content",nullable=false,length=512)
	private String content;//电影评价内容

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MovieComment [movie=" + movie + ", account=" + account
				+ ", rate=" + rate + ", content=" + content + "]";
	}
	
	

	
	
}
