package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Interact;
import com.baconbao.mxh.Repository.Post.InteractRepository;
import com.baconbao.mxh.Services.Service.Post.InteractService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InteractServiceImpl implements InteractService {
    @Autowired
    private InteractRepository interactRepository;

    @Override
    public Interact findById(Long id) {
        log.info("Finding interact with id: {}", id);
        Optional<Interact> interact = interactRepository.findById(id);
        if (interact.isPresent()) {
            return interact.get();
        } else {
            log.error("Interact with id {} not found", id);
            throw new CustomException(ErrorCode.INTERACT_NOT_FOUND);
        }
    }

}
