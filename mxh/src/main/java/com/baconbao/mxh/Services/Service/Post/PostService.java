package com.baconbao.mxh.Services.Service.Post;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.User.User;

@Service
public interface PostService {
    List<Post> findByActive(boolean active);
    List<Post> findByUserPosts(User user);
    Post findById(Long id);
    void save(Post post);
    void delete(Long id);
    Long getGenerationId();
    List<Comment> findByCommentsOrderByCreateAtDesc(Post post);
    List<Object[]> findPostAndCommentAndReplyCount(Post post, boolean active);
    Long countInteraction(Post post);
}
