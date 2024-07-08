package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Repository.Post.InteractionRepository;
import com.baconbao.mxh.Services.Service.Post.InteractionService;

@Service
public class InteractionServiceImpl  implements InteractionService{
    @Autowired
    private InteractionRepository interactionRepository;

    @Override
    public Interaction findById(Long id) {
        Optional<Interaction> interaction = interactionRepository.findById(id);
        if(interaction.isPresent()) {
            return interaction.get();
        }else{
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void saveInteraction(Interaction interaction) {
        try {
            if(interaction.getId() == null){
                interaction.setId(getGenerationId());
            }
            interactionRepository.save(interaction);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }

    
}
