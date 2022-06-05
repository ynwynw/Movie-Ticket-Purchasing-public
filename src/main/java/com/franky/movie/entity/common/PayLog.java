package com.franky.movie.entity.common;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.franky.movie.bean.PaymentType;

/**
 * 支付记录实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_pay_log")
@EntityListeners(AuditingEntityListener.class)
public class PayLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int status_unpay = 0;//待支付 
	
	public final static int status_paid = 1;//已支付 
	
	@Column(name="sn",nullable=false,unique=true,length=32)
	private String sn;//支付编号，唯一
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;//所属用户
	
	@Column(name="status",nullable=false,length=1)
	private Integer status = status_unpay;//支付记录状态
	
	@Column(name="money",nullable=false,length=12)
	private BigDecimal money;//支付金额
	
	@Column(name="payment_type",nullable=false,length=5)
	private PaymentType paymentType;//支付方式

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return "PayLog [sn=" + sn + ", account=" + account + ", status="
				+ status + ", money=" + money + ", paymentType=" + paymentType
				+ "]";
	}

	
	
}
