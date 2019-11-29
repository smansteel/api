package com.iseplife.api.dao.media;

import com.iseplife.api.entity.media.Media;
import com.iseplife.api.constants.PublishStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {
  Page<Media> findAllByMediaTypeInAndPost_isPrivateAndPost_PublishStateOrderByCreationDesc(
    Collection<String> mediaType, Boolean post_isPrivate, PublishStateEnum post_publishState, Pageable pageable);

  Page<Media> findAllByMediaTypeInAndPost_PublishStateOrderByCreationDesc(
    Collection<String> mediaType, PublishStateEnum post_publishState, Pageable pageable);

  Page<Media> findAllByMediaTypeInOrderByCreationDesc(
    Collection<String> mediaType, Pageable pageable);
}
