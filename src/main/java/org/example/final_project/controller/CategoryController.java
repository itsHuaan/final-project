package org.example.final_project.controller;

import org.example.final_project.model.CategoryModel;
import org.example.final_project.service.impl.CategoryService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.API_PREFIX+"/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/getAll")
    ResponseEntity getAllCategory(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAll());
    }
    @GetMapping("/getAllByPage")
    ResponseEntity getAllByPage(@RequestParam("pageSize")int pageSize,
                                @RequestParam("pageIndex")int pageIndex){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAllByPage(PageRequest.of(pageIndex,pageSize)));
    }
    @PostMapping("/addNew")
    ResponseEntity addNewCategory(@RequestBody CategoryModel model){
        if(categoryService.save(model)==1){
            return ResponseEntity.ok("Add Category Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Adding New Category");
        }
    }
    @PostMapping("/update/{id}")
    ResponseEntity updateCategory(@PathVariable("id")long id,
                                  @RequestBody CategoryModel model){
        if(categoryService.update(id,model)==1){
            return ResponseEntity.ok("Update Category Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Updating Category with Id= "+id);
        }
    }
    @PostMapping("/delete/{id}")
    ResponseEntity deleteCategory(@PathVariable("id")long id){
        if(categoryService.delete(id)==1){
            return ResponseEntity.ok("Delete Category Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When Deleting Category with Id= "+id);
        }
    }
    @PostMapping("/inactivate/{id}")
    ResponseEntity inactivateCategory(@PathVariable("id")long id){
        if(categoryService.inActivateCategory(id)==1){
            return ResponseEntity.status(HttpStatus.OK).body("Inactivate Category Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error When inactivating Category with Id= "+id);
        }
    }
}
