package com.franky.movie.entity.common;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 前端用户实体
 * @author frank
 *
 */
@Entity
@Table(name="movie_account")
@EntityListeners(AuditingEntityListener.class)
public class Account extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int account_sex_man = 1; 
	
	public final static int account_sex_woman = 2; 
	
	public final static int account_status_able = 1; 
	
	public final static int account_status_unable = 0; 
	
	@Column(name="mobile",nullable=false,unique=true,length=11)
	private String mobile;//前端用户手机号码
	
	@Column(name="password",nullable=false,length=32)
	private String password;//前端用户登录密码
	
	@Column(name="nickname",length=32)
	private String nickname;//前端用户昵称
	
	@Column(name="head_pic",nullable=false,length=128)
	private String headPic;//前端用户头像
	
	@Column(name="sex",nullable=false,length=1)
	private Integer sex = account_sex_man;//前端用户性别
	
	@Column(name="status",nullable=false,length=1)
	private Integer status = account_status_able;//前端用户状态
	
	@Column(name="balance",nullable=false,length=12)
	private BigDecimal balance = new BigDecimal(0);//前端用户余额

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public static int getAccountSexWoman() {
		return account_sex_woman;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Account [mobile=" + mobile + ", password=" + password
				+ ", nickname=" + nickname + ", headPic=" + headPic + ", sex="
				+ sex + ", balance=" + balance + "]";
	}

	
	
	
	
	
}
