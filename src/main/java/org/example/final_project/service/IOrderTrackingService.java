package org.example.final_project.service;

import org.example.final_project.dto.StatusMessageDto;

public interface IOrderTrackingService {
    int updateStatusShipping(StatusMessageDto messageDto);
}
