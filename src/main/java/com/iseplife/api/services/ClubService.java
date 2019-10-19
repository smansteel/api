package com.iseplife.api.services;

import com.google.common.collect.Sets;
import com.iseplife.api.conf.jwt.TokenPayload;
import com.iseplife.api.dto.ClubDTO;
import com.iseplife.api.dto.view.ClubMemberView;
import com.iseplife.api.entity.Feed;
import com.iseplife.api.entity.club.Club;
import com.iseplife.api.entity.club.ClubMember;
import com.iseplife.api.entity.user.Student;
import com.iseplife.api.conf.jwt.TokenPayload;
import com.iseplife.api.constants.ClubRole;
import com.iseplife.api.constants.Roles;
import com.iseplife.api.dao.club.ClubFactory;
import com.iseplife.api.dao.club.ClubMemberRepository;
import com.iseplife.api.dao.club.ClubRepository;
import com.iseplife.api.dto.ClubDTO;
import com.iseplife.api.dto.view.ClubMemberView;
import com.iseplife.api.entity.Feed;
import com.iseplife.api.entity.club.Club;
import com.iseplife.api.entity.club.ClubMember;
import com.iseplife.api.entity.user.Student;
import com.iseplife.api.exceptions.AuthException;
import com.iseplife.api.exceptions.IllegalArgumentException;
import com.iseplife.api.utils.MediaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Guillaume on 30/07/2017.
 * back
 */
@Service
public class ClubService {

  @Autowired
  ClubRepository clubRepository;

  @Autowired
  ClubMemberRepository clubMemberRepository;

  @Autowired
  ClubFactory clubFactory;

  @Autowired
  StudentService studentService;

  @Autowired
  MediaUtils imageUtils;

  @Value("${storage.club.url}")
  public String clubLogoStorage;

  private static final int WIDTH_LOGO_CLUB = 256;

  public Club createClub(ClubDTO dto, MultipartFile logo) {
    Club club = clubFactory.dtoToEntity(dto);
    if (dto.getAdminId() == null) {
      throw new IllegalArgumentException("The id of the admin cannot be null");
    }
    Student admin = studentService.getStudent(dto.getAdminId());
    if (admin == null) {
      throw new IllegalArgumentException("this student id doesn't exist");
    }

    ClubMember clubMember = new ClubMember();
    clubMember.setStudent(admin);
    clubMember.setRole(ClubRole.SUPER_ADMIN);
    clubMember.setClub(club);

    club.setMembers(Collections.singletonList(clubMember));
    setClubLogo(club, logo);

    Feed clubFeed = new Feed();
    //TODO: slugify name
    clubFeed.setName(club.getName());
    club.setFeed(clubFeed);
    return clubRepository.save(club);
  }

  public List<Club> searchClubs(String name) {
    return clubRepository.findAllByNameContainingIgnoringCase(name);
  }

  private void setClubLogo(Club club, MultipartFile file) {
    String path = imageUtils.resolvePath(clubLogoStorage, club.getName(), false);
    imageUtils.removeIfExistJPEG(path);
    imageUtils.saveJPG(file, WIDTH_LOGO_CLUB, path);
    club.setLogoUrl(imageUtils.getPublicUrlImage(path));
  }

  public ClubMember addMember(Long clubId, Long studentId) {
    clubMemberRepository.findByClubId(clubId).forEach(member -> {
      if (member.getStudent().getId().equals(studentId)) {
        throw new IllegalArgumentException("this student is already part of this club");
      }
    });

    ClubMember clubMember = new ClubMember();
    clubMember.setClub(getClub(clubId));
    clubMember.setStudent(studentService.getStudent(studentId));
    clubMember.setRole(ClubRole.MEMBER);

    return clubMemberRepository.save(clubMember);
  }

  public List<Club> getAll() {
    return clubRepository.findAllByOrderByName();
  }

  public Club getClub(Long id) {
    Club club = clubRepository.findOne(id);
    if (club == null) throw new IllegalArgumentException("could not find club with id: " + id);
    return club;
  }

  /**
   * Retrieve the list of clubs where the student is admin
   *
   * @param student student id
   * @return a club list
   */
  List<Club> getClubAuthors(Student student) {
    return clubRepository.findByRoleWithInheritance(student, ClubRole.PUBLISHER);
  }

  public List<ClubMember> getPublisherClubs(Student student){
    return clubMemberRepository.findByRoleWithInheritance(student, ClubRole.PUBLISHER);
  }

  public void deleteClub(Long id) {
    clubRepository.delete(id);
  }

  public List<ClubMember> getMembers(Long id) {
    return clubMemberRepository.findByClubId(id);
  }

  public Set<Student> getAdmins(Long clubId) {
    Club club = getClub(clubId);
    return club.getMembers()
    .stream()
    .filter(s -> s.getRole().is(ClubRole.ADMIN))
    .map(ClubMember::getStudent)
    .collect(Collectors.toSet());
  }

  public Set<Student> getAdmins(Club club){
    return club.getMembers()
      .stream()
      .filter(s -> s.getRole().is(ClubRole.ADMIN))
      .map(ClubMember::getStudent)
      .collect(Collectors.toSet());
  }

  public void addAdmin(Long clubId, Long studId) {
    ClubMember member = clubMemberRepository.findOneByStudentIdAndClubId(studId, clubId);
    if(member == null) throw new IllegalArgumentException("the student needs to be part of the club to be an admin");

    member.setRole(ClubRole.ADMIN);
    clubRepository.save(member.getClub());
  }


  public void removeAdmin(Long clubId, Long studId) {
    ClubMember member = clubMemberRepository.findOneByStudentIdAndClubId(clubId, studId);
    member.setRole(ClubRole.MEMBER);

    clubMemberRepository.save(member);
  }

  public Club updateClub(Long id, ClubDTO clubDTO, MultipartFile logo) {
    Club club = getClub(id);

    club.setName(clubDTO.getName());
    club.setCreatedAt(clubDTO.getCreation());
    club.setDescription(clubDTO.getDescription());
    club.setWebsite(clubDTO.getWebsite());

    if (logo != null) {
      setClubLogo(club, logo);
    }
    return clubRepository.save(club);
  }

  public ClubMember updateMemberRole(Long member, ClubRole role, TokenPayload payload) {
    ClubMember clubMember = getMember(member);
    if (!payload.getRoles().contains(Roles.ADMIN) && payload.getRoles().contains(Roles.CLUB_MANAGER)) {
      if (!payload.getClubsAdmin().contains(clubMember.getClub().getId())) {
        throw new AuthException("no rights to modify this club");
      }
    }
    clubMember.setRole(role);
    return clubMemberRepository.save(clubMember);
  }

  private ClubMember getMember(Long member) {
    ClubMember clubMember = clubMemberRepository.findOne(member);
    if (clubMember == null) {
      throw new IllegalArgumentException("member could not be found");
    }
    return clubMember;
  }

  public void removeMember(Long member, TokenPayload payload) {
    ClubMember clubMember = getMember(member);
    Club club = clubMember.getClub();
    if (!payload.getRoles().contains(Roles.ADMIN) && payload.getRoles().contains(Roles.CLUB_MANAGER)) {
      if (!payload.getClubsAdmin().contains(club.getId())) {
        throw new AuthException("no rights to modify this club");
      }
    }
    clubMemberRepository.delete(clubMember);
    clubRepository.save(club);
  }

  public List<ClubMemberView> getStudentClubs(Long id) {
    List<ClubMember> clubMembers = clubMemberRepository.findByStudentId(id);
    return clubMembers.stream().map(cm -> {
      ClubMemberView clubMemberView = new ClubMemberView();
      clubMemberView.setClub(cm.getClub());
      clubMemberView.setMember(cm.getStudent());
      clubMemberView.setRole(cm.getRole());
      return clubMemberView;
    }).collect(Collectors.toList());
  }

  public Club getIsepLive() {
    return clubRepository.findByIsAdmin(true);
  }

}