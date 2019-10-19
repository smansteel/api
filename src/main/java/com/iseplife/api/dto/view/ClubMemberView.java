package com.iseplife.api.dto.view;

import com.iseplife.api.entity.club.Club;
import com.iseplife.api.entity.user.Student;
import com.iseplife.api.constants.ClubRole;

/**
 * Created by Guillaume on 03/12/2017.
 * back
 */
public class ClubMemberView {
  private Club club;
  private ClubRole role;
  private Student member;

  public ClubRole getRole() {
    return role;
  }

  public void setRole(ClubRole role) {
    this.role = role;
  }

  public Student getMember() {
    return member;
  }

  public void setMember(Student member) {
    this.member = member;
  }

  public Club getClub() {
    return club;
  }

  public void setClub(Club club) {
    this.club = club;
  }
}