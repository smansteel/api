package com.iseplife.api.dao.post;

import com.iseplife.api.dao.post.projection.CommentProjection;
import com.iseplife.api.dto.thread.view.CommentFormView;
import com.iseplife.api.dto.thread.view.CommentView;
import com.iseplife.api.entity.post.Comment;
import com.iseplife.api.services.SecurityService;
import com.iseplife.api.services.ThreadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommentFactory {
  final private ModelMapper mapper;

  public CommentView toView(CommentProjection comment, Boolean isLiked) {
    CommentView view = mapper.map(comment, CommentView.class);
    view.setLiked(isLiked);
    view.setHasWriteAccess(SecurityService.hasRightOn(comment));

    return view;
  }

  public CommentFormView toView(Comment comment) {
    mapper.typeMap(Comment.class, CommentFormView.class).addMappings(mapper -> {
      mapper.map(src -> src.getThread().getId(), CommentFormView::setThread);
      mapper.skip(CommentFormView::setAuthor);
    });
    CommentFormView view = mapper.map(comment, CommentFormView.class);
    view.setAuthor(comment.getAsClub() != null ?
      AuthorFactory.toView(comment.getAsClub()) :
      AuthorFactory.toView(comment.getStudent())
    );

    return view;
  }
}
