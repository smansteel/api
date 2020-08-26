package com.iseplife.api.dto.group.view;

import com.iseplife.api.dto.student.view.StudentPreview;

import java.util.List;

public class GroupView {
  private Long id;
  private String name;
  private Boolean restricted;
  private Boolean archived;
  private Boolean locked;
  private String cover;
  private Long feed;
  private List<StudentPreview> admins;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<StudentPreview> getAdmins() {
    return admins;
  }

  public void setAdmins(List<StudentPreview> admins) {
    this.admins = admins;
  }

  public Boolean getRestricted() {
    return restricted;
  }

  public void setRestricted(Boolean restricted) {
    this.restricted = restricted;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public Long getFeed() {
    return feed;
  }

  public void setFeed(Long feed) {
    this.feed = feed;
  }
}
