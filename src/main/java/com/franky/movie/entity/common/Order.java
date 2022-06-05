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
 * 订单实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_order")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int status_unpay = 0;//待支付 
	
	public final static int status_paid = 1;//已支付 
	
	public final static int status_cancel = -1;//已取消 
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;//所属用户
	
	@Column(name="sn",nullable=false,unique=true,length=32)
	private String sn;//订单编号，唯一
	
	@ManyToOne
	@JoinColumn(name="cinema_hall_session_id")
	private CinemaHallSession cinemaHallSession;//所属放映场次
	
	@Column(name="status",nullable=false,length=1)
	private Integer status = status_unpay;//订单状态
	
	@Column(name="old_money",nullable=false,length=12)
	private BigDecimal oldMoney;//应支付金额
	
	@Column(name="new_money",nullable=false,length=12)
	private BigDecimal newMoney;//实际支付金额
	
	@Column(name="num",nullable=false,length=1)
	private Integer num ;//影票数量

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}


	public CinemaHallSession getCinemaHallSession() {
		return cinemaHallSession;
	}

	public void setCinemaHallSession(CinemaHallSession cinemaHallSession) {
		this.cinemaHallSession = cinemaHallSession;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getOldMoney() {
		return oldMoney;
	}

	public void setOldMoney(BigDecimal oldMoney) {
		this.oldMoney = oldMoney;
	}

	public BigDecimal getNewMoney() {
		return newMoney;
	}

	public void setNewMoney(BigDecimal newMoney) {
		this.newMoney = newMoney;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "Order [account=" + account + ", sn=" + sn + ", " + ", cinemaHallSession=" + cinemaHallSession + ", status="
				+ status + ", oldMoney=" + oldMoney + ", newMoney=" + newMoney
				+ ", num=" + num + "]";
	}
	
	
}
