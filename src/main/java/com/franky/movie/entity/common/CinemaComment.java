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
 * 影院评价实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_cinema_comment")
@EntityListeners(AuditingEntityListener.class)
public class CinemaComment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="cinema_id",nullable=false)
	private Cinema cinema;//所属影院
	
	@ManyToOne
	@JoinColumn(name="account_id",nullable=false)
	private Account account;//所属用户
	
	@Column(name="rate",nullable=false)
	private BigDecimal rate;//评分
	
	@Column(name="content",nullable=false,length=512)
	private String content;//影院评价内容

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
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
		return "CinemaComment [cinema=" + cinema + ", account=" + account
				+ ", rate=" + rate + ", content=" + content + "]";
	}

	
	
}
