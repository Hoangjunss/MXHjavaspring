package com.baconbao.mxh.Repository.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.Status;
import com.baconbao.mxh.Models.User.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByIsActiveAndStatusOrderByCreateAtDesc(boolean active, Status status);

    List<Post> findByIsActiveAndStatus(boolean active, Status status);

    List<Post> findByUserOrderByCreateAtDesc(User user);

    List<Comment> findByCommentsOrderByCreateAtDesc(Post post);

    @Query("SELECT p, COUNT(c) " +
            "FROM Post p " +
            "LEFT JOIN p.comments c " +
            "WHERE p = :post AND p.isActive = :active AND p.status = :status " +
            "GROUP BY p ")
    List<Object[]> findPostAndCommentAndReplyCount(
            @Param("post") Post post,
            @Param("active") boolean active,
            @Param("status") Status status);


        @Query("SELECT count(i) FROM Post p LEFT JOIN interactions i WHERE p = :post ")
        Long countInteraction(@Param("post") Post post);
}