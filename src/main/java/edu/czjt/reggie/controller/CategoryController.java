package edu.czjt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.czjt.reggie.common.R;
import edu.czjt.reggie.entity.Category;
import edu.czjt.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


/**
 * 分类管理 xujiakai
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
       
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);

        
        /**条件构造器
         *这是一个定义了Lambda表达式的查询条件封装类 
         * LambdaQueryWrapper是Mybatis-Plus的一个查询条件封装类，在使用Lambda表达式时，
         * 可以通过LambdaQueryWrapper来构建查询条件，
         *  这种方式更加简便和安全，同时也避免了SQL注入的风险。
         */
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    
    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);

        categoryService.removeById(id);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }

    
    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        
        //条件构造器
    /**
     * 这是一个基于Mybatis-Plus的Lambda查询构造器，用于构建查询条件。
     * LambdaQueryWrapper的泛型类为Category，表示我们对Category表进行查询。
     * LambdaQueryWrapper的使用可以让我们更加方便地构造查询条件，并且可以避免手写SQL语句带来的风险。
     */
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        
        //添加条件
    /**
     * 这是一个Lambda表达式用于构造查询条件，通过Mybatis-Plus的LambdaQueryWrapper类来使用。
     * Lambda表达式的作用是构建查询条件
     * 即查询指定Category对象的type属性是否与数据库中的记录的type属性相同。
     * 查询数据库中type属性与该Category对象的type属性相等的记录。
     */
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());

        
        //添加排序条件
    /**
     * 这是一个Lambda表达式用于构造排序条件，通过Mybatis-Plus的LambdaQueryWrapper类来使用。
     * 该Lambda表达式的作用是构建排序条件，即按照sort升序和updateTime降序的方式排列结果。
     * 首先按照sort升序进行排序，然后在sort升序的基础上按照updateTime降序进行排序。
     * Lambda表达式可以避免由于拼接错误而导致的查询失败或安全问题。
     */
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}

