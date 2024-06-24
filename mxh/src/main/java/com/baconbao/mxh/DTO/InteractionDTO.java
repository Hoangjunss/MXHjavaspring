package com.baconbao.mxh.DTO;

public class InteractionDTO {
    private Long idInteraction;
    private String interactType;
    private Long idPost;

    public InteractionDTO() {
    }

    public InteractionDTO(Long idInteraction, String interactType, Long idPost) {
        this.idInteraction = idInteraction;
        this.interactType = interactType;
        this.idPost = idPost;
    }

    public Long getIdInteraction() {
        return idInteraction;
    }

    public void setIdInteraction(Long idInteraction) {
        this.idInteraction = idInteraction;
    }

    public String getInteractType() {
        return interactType;
    }

    public void setInteractType(String interactType) {
        this.interactType = interactType;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

}
