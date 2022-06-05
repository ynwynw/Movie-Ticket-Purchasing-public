package com.franky.movie.entity.common;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.franky.movie.bean.CinemaSessionType;

/**
 * 影厅排片场次实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_cinema_hall_session")
@EntityListeners(AuditingEntityListener.class)
public class CinemaHallSession extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="cinema_id")
	private Cinema cinema;//所属电影院
	
	@ManyToOne
	@JoinColumn(name="movie_id")
	private Movie movie;//所属电影
	
	@ManyToOne
	@JoinColumn(name="cinema_hall_id")
	private CinemaHall cinemaHall;//所属影厅
	
	@Column(name="show_date",nullable=false,length=12)
	private String showDate;//放映日期
	
	@Column(name="show_time",nullable=false,length=12)
	private String showTime;//放映时间
	
	@Column(name="start_time",nullable=false,length=18)
	private String startTime;//开场时间
	
	@Column(name="end_time",nullable=false,length=18)
	private String endTime;//散场时间
	
	@Column(name="cinema_session_type",nullable=false,length=5)
	private CinemaSessionType cinemaSessionType;//放映类型
	
	@Column(name="old_price",nullable=false,length=6)
	private BigDecimal oldPrice;//原价
	
	@Column(name="new_price",nullable=false,length=6)
	private BigDecimal newPrice;//现价

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public CinemaHall getCinemaHall() {
		return cinemaHall;
	}

	public void setCinemaHall(CinemaHall cinemaHall) {
		this.cinemaHall = cinemaHall;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public CinemaSessionType getCinemaSessionType() {
		return cinemaSessionType;
	}

	public void setCinemaSessionType(CinemaSessionType cinemaSessionType) {
		this.cinemaSessionType = cinemaSessionType;
	}

	public BigDecimal getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(BigDecimal oldPrice) {
		this.oldPrice = oldPrice;
	}

	public BigDecimal getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(BigDecimal newPrice) {
		this.newPrice = newPrice;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "CinemaHallSession [cinema=" + cinema + ", movie=" + movie
				+ ", cinemaHall=" + cinemaHall + ", showDate=" + showDate
				+ ", showTime=" + showTime + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", cinemaSessionType="
				+ cinemaSessionType + ", oldPrice=" + oldPrice + ", newPrice="
				+ newPrice + "]";
	}

	

	
	
	
	

	

	
	
}
