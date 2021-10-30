package com.iseplife.api.dao.student;

import com.iseplife.api.conf.StorageConfig;
import com.iseplife.api.dto.student.view.*;
import com.iseplife.api.entity.user.Role;
import com.iseplife.api.entity.user.Student;
import com.iseplife.api.utils.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentFactory {
  final private ModelMapper mapper;

  public static StudentPictures toPictures(String picture, Boolean hasDefaultPicture) {
    if (MediaUtils.isOriginalPicture(picture)) {
      return new StudentPictures(
        picture,
        null
      );
    } else {
      return new StudentPictures(
        hasDefaultPicture ?
          StorageConfig.MEDIAS_CONF.get("user_original").path + "/" + MediaUtils.extractFilename(picture) :
          null,
        picture
      );
    }
  }

  public StudentPreview toPreview(Student student) {
    return mapper.map(student, StudentPreview.class);
  }


  public StudentView toView(Student student) {
    mapper
      .typeMap(Student.class, StudentView.class)
      .addMappings(mapper -> {
        mapper.map(
          Student::getPicture,
          (dest, v) -> dest.setPictures(toPictures((String) v, student.getHasDefaultPicture()))
        );
        mapper
          .using(ctx -> ((Set<Role>) ctx.getSource()).stream().map(Role::getRole).collect(Collectors.toList()))
          .map(Student::getRoles, StudentView::setRoles);
      });
    return mapper.map(student, StudentView.class);
  }

  public StudentPreviewAdmin toPreviewAdmin(Student student) {
    mapper
      .typeMap(Student.class, StudentPreviewAdmin.class)
      .addMappings(mapper -> {
        mapper
          .using(ctx -> ((Set<Role>) ctx.getSource()).stream().map(Role::getRole).collect(Collectors.toList()))
          .map(Student::getRoles, StudentPreviewAdmin::setRoles);
      });

    return mapper.map(student, StudentPreviewAdmin.class);
  }

  public StudentAdminView toAdminView(Student student) {
    mapper
      .typeMap(Student.class, StudentAdminView.class)
      .addMappings(mapper -> {
        mapper
          .using(ctx -> ((Set<Role>) ctx.getSource()).stream().map(Role::getRole).collect(Collectors.toList()))
          .map(Student::getRoles, StudentAdminView::setRoles);
      });

    return mapper.map(student, StudentAdminView.class);
  }

  public StudentOverview toOverview(Student student) {
    return mapper.map(student, StudentOverview.class);
  }

}
