package org.example.final_project.controller;


import org.example.final_project.model.ProductModel;
import org.example.final_project.service.impl.ProductService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Const.API_PREFIX+"/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/getAll")
    ResponseEntity getAllProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll());
    }
    @GetMapping("/getAllByPage")
    ResponseEntity getAllByPage(@RequestParam("pageSize")int pageSize,
                                @RequestParam("pageIndex")int pageIndex){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllByPage(PageRequest.of(pageIndex,pageSize)));
    }
    @PostMapping("/addNew")
    ResponseEntity addNewProduct(@RequestBody ProductModel model,
                                 @RequestParam("files")MultipartFile[] files){
        if(productService.save(model)==1){
            return ResponseEntity.ok("Add Product Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Adding New Product");
        }
    }
    @PostMapping("/update/{id}")
    ResponseEntity updateProduct(@PathVariable("id")long id,
                                  @RequestBody ProductModel model){
        if(productService.update(id,model)==1){
            return ResponseEntity.ok("Update Product Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Updating Product with Id= "+id);
        }
    }
    @PostMapping("/delete/{id}")
    ResponseEntity deleteProduct(@PathVariable("id")long id){
        if(productService.delete(id)==1){
            return ResponseEntity.ok("Delete Product Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Deleting Product with Id= "+id);
        }
    }
    @PostMapping("/inactivate/{id}")
    ResponseEntity inactivateProduct(@PathVariable("id")long id){
        if(productService.inActivateProduct(id)==1){
            return ResponseEntity.status(HttpStatus.OK).body("Inactivate Product Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When inactivating Product with Id= "+id);
        }
    }
}
