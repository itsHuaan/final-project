package org.example.final_project.service;

import org.example.final_project.dto.CartDto;
import org.example.final_project.model.CartModel;

public interface ICartService extends IBaseService<CartDto, CartModel, Long>{
    CartDto getUserCart(Long userId);
}
