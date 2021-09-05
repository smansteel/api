package com.iseplife.api.dao.post;

import com.iseplife.api.dto.post.PostCreationDTO;
import com.iseplife.api.dto.post.view.PostFormView;
import com.iseplife.api.dto.post.view.PostView;
import com.iseplife.api.entity.post.Post;
import com.iseplife.api.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFactory {

  @Autowired
  ThreadService threadService;

  @Autowired
  ModelMapper mapper;


  public Post dtoToEntity(PostCreationDTO post) {
    Post p = new Post();
    p.setDescription(post.getDescription());
    p.setPrivate(post.getPrivate());
    return p;
  }

  public PostFormView toPostFormView(Post post) {
    PostFormView view = mapper.map(post, PostFormView.class);

    view.setEmbed(EmbedFactory.toView(post.getEmbed()));
    view.setThread(post.getThread().getId());
    return view;
  }

  public PostView toView(PostProjection post) {
    PostView view = mapper.map(post, PostView.class);

    view.setEmbed(EmbedFactory.toView(post.getEmbed()));
    view.setLiked(threadService.isLiked(post.getThread()));
    view.setTrendingComment(threadService.getTrendingComment(post.getThread()));
    view.setHasWriteAccess(SecurityService.hasRightOn(post));

    return view;
  }
}
