package com.jifenke.lepluslive.s_movie.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 电影院和POS终端对应表(OneToMany) Created by zhangwen on 2017/4/25.
 */
@Entity
@Table(name = "S_MOVIE_TERMINAL")
public class SMovieTerminal {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreate = new Date();

  private String movieName;  //影院名称

  @Column(nullable = false, unique = true)
  private String terminalNo;   //终端ID  该字段数据库唯一，不可重复

  private Integer state = 1;  //该终端是否可使用（如果=0,不可核销订单）  1=可使用|0=暂停使用

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(Date dateCreate) {
    this.dateCreate = dateCreate;
  }

  public String getMovieName() {
    return movieName;
  }

  public void setMovieName(String movieName) {
    this.movieName = movieName;
  }

  public String getTerminalNo() {
    return terminalNo;
  }

  public void setTerminalNo(String terminalNo) {
    this.terminalNo = terminalNo;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
