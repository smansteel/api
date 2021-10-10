package com.iseplife.api.controllers;

import com.iseplife.api.conf.jwt.TokenPayload;
import com.iseplife.api.constants.Roles;
import com.iseplife.api.dao.club.ClubFactory;
import com.iseplife.api.dao.club.ClubMemberFactory;
import com.iseplife.api.dao.club.projection.ClubMemberProjection;
import com.iseplife.api.dto.club.ClubAdminDTO;
import com.iseplife.api.dto.club.ClubDTO;
import com.iseplife.api.dto.club.ClubMemberCreationDTO;
import com.iseplife.api.dto.club.ClubMemberDTO;
import com.iseplife.api.dto.club.view.ClubPreview;
import com.iseplife.api.dto.club.view.ClubView;
import com.iseplife.api.dto.gallery.view.GalleryPreview;
import com.iseplife.api.dto.student.view.StudentPreview;
import com.iseplife.api.dto.post.view.PostView;
import com.iseplife.api.services.ClubService;
import com.iseplife.api.services.PostService;
import com.iseplife.api.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Guillaume on 30/07/2017.
 * back
 */
@RestController
@RequestMapping("/club")
public class ClubController {

  @Autowired
  ClubService clubService;

  @Autowired
  PostService postService;

  @Autowired
  JsonUtils jsonUtils;

  @GetMapping
  @RolesAllowed({Roles.STUDENT})
  public List<ClubPreview> getAllClubs() {
    return clubService.getAll().stream()
      .map(ClubFactory::toPreview)
      .collect(Collectors.toList());
  }

  @PostMapping
  @RolesAllowed({Roles.ADMIN})
  public ClubView createClub(@RequestBody ClubAdminDTO dto) {
    return clubService.createClub(dto);
  }

  @GetMapping("/{id}")
  @RolesAllowed({Roles.STUDENT})
  public ClubView getClub(@PathVariable Long id) {
    return clubService.getClubView(id);
  }

  @PutMapping("/{id}")
  @RolesAllowed({Roles.STUDENT})
  public ClubView updateClub(@PathVariable Long id, @RequestBody ClubDTO dto) {
    return clubService.updateClub(id, dto);
  }

  @PutMapping("/{id}/admin")
  @RolesAllowed({Roles.ADMIN})
  public ClubView updateAdminClub(@PathVariable Long id, @RequestBody ClubAdminDTO dto) {
    return clubService.updateClubAdmin(id, dto);
  }

  @PostMapping("/{id}/logo")
  @RolesAllowed({Roles.STUDENT})
  public String updateLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    return clubService.updateLogo(id, file);
  }

  @PostMapping("/{id}/cover")
  @RolesAllowed({Roles.STUDENT})
  public String updateCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    return clubService.updateCover(id, file);
  }

  @PutMapping("/{id}/archive")
  @RolesAllowed({Roles.ADMIN})
  public Boolean toggleArchiveStatus(@PathVariable Long id) {
    return clubService.toggleArchiveStatus(id);
  }

  @DeleteMapping("/{id}")
  @RolesAllowed({Roles.ADMIN})
  public void deleteClub(@PathVariable Long id) {
    clubService.deleteClub(id);
  }

  @GetMapping("/{id}/galleries")
  @RolesAllowed({Roles.STUDENT})
  public Page<GalleryPreview> getGalleries(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
    return clubService.getClubGalleries(id, page);
  }

  @GetMapping("/{id}/admins")
  @RolesAllowed({Roles.STUDENT})
  public Set<StudentPreview> getAdmins(@PathVariable Long id) {
    return clubService.getAdmins(id);
  }

  @PostMapping("/{id}/member")
  @RolesAllowed({Roles.STUDENT})
  public ClubMemberProjection addMember(@PathVariable Long id, @RequestBody ClubMemberCreationDTO dto) {
    return ClubMemberFactory.toView(clubService.addMember(id, dto));
  }

  @PutMapping("/member/{member}")
  @RolesAllowed({Roles.STUDENT})
  public ClubMemberProjection updateMember(@PathVariable Long member, @RequestBody ClubMemberDTO dto) {
    return ClubMemberFactory.toView(clubService.updateMember(member, dto));
  }

  @GetMapping("/{id}/member")
  @RolesAllowed({Roles.STUDENT})
  public List<ClubMemberProjection> getYearlyMembers(@PathVariable Long id, @RequestParam(name = "y", required = false) Integer year) {
    return clubService.getYearlyMembers(id, year);
  }

  @GetMapping("/{id}/school-sessions")
  @RolesAllowed({Roles.STUDENT})
  public Set<Integer> getClubSchoolSessions(@PathVariable Long id) {
    return clubService.getClubAllSchoolSessions(id);
  }


  @DeleteMapping("/member/{member}")
  @RolesAllowed({Roles.STUDENT})
  public void deleteMember(@PathVariable Long member,
                           @AuthenticationPrincipal TokenPayload payload) {
    clubService.removeMember(member, payload);
  }

  @GetMapping("/{id}/post")
  @RolesAllowed({Roles.STUDENT})
  public Page<PostView> getPosts(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @AuthenticationPrincipal TokenPayload token) {
    return postService.getAuthorPosts(id, page, token);
  }
}
