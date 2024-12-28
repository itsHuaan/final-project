package org.example.final_project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.final_project.model.ProductOptionsModel;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

public class ConvertJsonObject {
    public static List<ProductOptionsModel> convertJsonToOption(String jsonObject) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        List<ProductOptionsModel> options = objectMapper.readValue(jsonObject, new TypeReference<List<ProductOptionsModel>>() {});
        return options;
    }
}
