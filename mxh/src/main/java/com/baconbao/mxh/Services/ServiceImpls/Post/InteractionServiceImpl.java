package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.Post.InteractionRepository;
import com.baconbao.mxh.Services.Service.Post.InteractionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InteractionServiceImpl implements InteractionService {
    @Autowired
    private InteractionRepository interactionRepository;

    @Override
    public Interaction findById(Long id) {
        log.info("Finding interaction by id: {}", id);
        Optional<Interaction> interaction = interactionRepository.findById(id);
        if (interaction.isPresent()) {
            return interaction.get();
        } else {
            log.error("Interaction is not found with id: {}", id);
            throw new CustomException(ErrorCode.INTERACT_NOT_FOUND);
        }
    }

    @Override
    public void saveInteraction(Interaction interaction) {
        try {
            log.info("Saving interaction: {}", interaction);
            if (interaction.getId() == null) {
                interaction.setId(getGenerationId());
            }
            interactionRepository.save(interaction);
        } catch (DataIntegrityViolationException e) {
            log.error("Unable to save interaction");
            throw new CustomException(ErrorCode.INTERACT_UNABLE_TO_SAVE);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    @Override
    public Interaction findByPostAndUser(Post post, User user) {
        log.info("Finding interaction by post and user: {}, {}", post.getId(), user.getId());
        return interactionRepository.findByPostAndUser(post, user);
    }

}
