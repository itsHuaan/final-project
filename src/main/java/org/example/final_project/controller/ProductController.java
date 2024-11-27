package org.example.final_project.controller;


import jakarta.servlet.annotation.MultipartConfig;
import org.example.final_project.model.ProductModel;
import org.example.final_project.service.impl.ProductService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@MultipartConfig
@RequestMapping(Const.API_PREFIX + "/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/getAll")
    ResponseEntity getAllByPage(@RequestParam(required = false) Integer pageSize,
                                @RequestParam(required = false) Integer pageIndex) {
        Pageable pageable = Pageable.unpaged();
        if (pageSize != null && pageIndex != null) {
            if (pageSize > 0 && pageIndex >= 0){
                pageable = PageRequest.of(pageIndex, pageSize);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pageable error");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllByPages(pageable));
    }

    @PostMapping("/addNew")
    ResponseEntity addNewProduct(@ModelAttribute ProductModel model) {
        if (productService.save(model) == 1) {
            return ResponseEntity.ok("Add Product Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Adding New Product");
        }
    }

    @PostMapping("/update/{id}")
    ResponseEntity updateProduct(@PathVariable("id") long id,
                                 @RequestBody ProductModel model) {
        if (productService.update(id, model) == 1) {
            return ResponseEntity.ok("Update Product Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Updating Product with Id= " + id);
        }
    }

    @PostMapping("/delete/{id}")
    ResponseEntity deleteProduct(@PathVariable("id") long id) {
        if (productService.delete(id) == 1) {
            return ResponseEntity.ok("Delete Product Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Deleting Product with Id= " + id);
        }
    }

    @PostMapping("/inactivate/{id}")
    ResponseEntity inactivateProduct(@PathVariable("id") long id) {
        if (productService.inActivateProduct(id) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("Inactivate Product Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When inactivating Product with Id= " + id);
        }
    }
}
