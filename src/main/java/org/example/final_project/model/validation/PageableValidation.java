package org.example.final_project.model.validation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PageableValidation {
    public static Pageable setDefault(Integer pageSize,Integer pageIndex){
        if(pageSize!=null&&pageIndex!=null){
            if(pageSize>0 && pageIndex>=0){
                return PageRequest.of(pageIndex,pageSize);
            }else{
                return null;
            }
        }else{
            return Pageable.unpaged();
        }
    }
}
