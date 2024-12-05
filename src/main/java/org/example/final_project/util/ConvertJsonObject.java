package org.example.final_project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.final_project.model.ProductOptionsModel;

import java.util.ArrayList;
import java.util.List;

public class ConvertJsonObject {
    public static List<ProductOptionsModel> convertJsonToOption(List<String> jsonObject) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        List<ProductOptionsModel> options=new ArrayList<>();
        for (String item:jsonObject){
            options.add(objectMapper.readValue(item, ProductOptionsModel.class));
        }
        return options;
    }
}
