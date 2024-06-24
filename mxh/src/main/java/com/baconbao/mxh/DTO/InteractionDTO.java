package com.baconbao.mxh.DTO;

public class InteractionDTO {
    private Long reactionId;
    private Long postId;

    public InteractionDTO() {
    }

    public InteractionDTO(Long reactionId, Long postId) {
        this.reactionId = reactionId;
        this.postId = postId;
    }

    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    
}
