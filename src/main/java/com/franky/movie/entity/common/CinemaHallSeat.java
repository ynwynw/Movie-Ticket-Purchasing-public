package com.franky.movie.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 影厅座位实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_cinema_hall_seat")
@EntityListeners(AuditingEntityListener.class)
public class CinemaHallSeat extends BaseEntity {

	public static final int CINEMAL_HALL_SEAT_STATUS_ENABLE = 1;//状态可用
	public static final int CINEMAL_HALL_SEAT_STATUS_UNABLE = 0;//状态不可用
	
	public static final int CINEMAL_HALL_SEAT_TYPE_NORMAL = 1;//普通座位
	public static final int CINEMAL_HALL_SEAT_TYPE_LOVE = 2;//情侣座
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="cinema_hall_id")
	private CinemaHall cinemaHall;//所属电影院影厅
	
	@Column(name="x",nullable=false,length=4)
	private Integer x;//横坐标，即第几座（列）的座
	
	@Column(name="y",nullable=false,length=4)
	private Integer y;//纵坐标，即第几排的排
	
	
	@Column(name="status",nullable=false,length=1)
	private int status = CINEMAL_HALL_SEAT_STATUS_ENABLE;//状态
	
	@Column(name="type",nullable=false,length=1)
	private int type = CINEMAL_HALL_SEAT_TYPE_NORMAL;//类型

	public CinemaHall getCinemaHall() {
		return cinemaHall;
	}

	public void setCinemaHall(CinemaHall cinemaHall) {
		this.cinemaHall = cinemaHall;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CinemaHallSeat [cinemaHall=" + cinemaHall + ", x=" + x + ", y="
				+ y + ", status=" + status + ", type=" + type + "]";
	}

	
	
	

	

	
	
}
