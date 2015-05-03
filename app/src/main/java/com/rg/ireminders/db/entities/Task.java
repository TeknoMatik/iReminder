package com.rg.ireminders.db.entities;

/**
 * Created by rustamgaifullin on 2/7/15.
 */
public class Task {
  private Integer id;
  private String title;
  private Long created;
  private Long due;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public Long getDue() {
    return due;
  }

  public void setDue(Long due) {
    this.due = due;
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("Task{");
    sb.append("id=").append(id);
    sb.append(", title='").append(title).append('\'');
    sb.append(", created=").append(created);
    sb.append(", due=").append(due);
    sb.append('}');
    return sb.toString();
  }
}
