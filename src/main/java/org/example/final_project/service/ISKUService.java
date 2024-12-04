package org.example.final_project.service;

import org.example.final_project.dto.SKUDto;
import org.example.final_project.model.SKUModel;
import java.util.List;

public interface ISKUService extends IBaseService<SKUDto, SKUModel,Long> {
    List<SKUDto> getAllByProduct(long productId);
}
