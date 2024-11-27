package org.example.final_project.service.impl;

import org.example.final_project.dto.PromotionDto;
import org.example.final_project.mapper.PromotionMapper;
import org.example.final_project.model.PromotionModel;
import org.example.final_project.repository.IPromotionRepository;
import org.example.final_project.service.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService implements IPromotionService {
    @Autowired
    IPromotionRepository iPromotionRepository;
    @Autowired
    PromotionMapper promotionMapper;
    @Override
    public List<PromotionDto> getAll() {
        return null;
    }

    @Override
    public PromotionDto getById(Long id) {
        return null;
    }

    @Override
    public int save(PromotionModel model) {
        return 0;
    }

    @Override
    public int update(Long aLong, PromotionModel model) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
