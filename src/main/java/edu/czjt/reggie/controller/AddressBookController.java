package edu.czjt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import edu.czjt.reggie.common.BaseContext;
import edu.czjt.reggie.common.R;
import edu.czjt.reggie.entity.AddressBook;
import edu.czjt.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());//BaseContext.getCurrentId()是一个获取当前用户 ID 的方法调用
        //针对 addressBook 对象的方法调用，通过传递当前用户的 ID 作为参数来设置 userId 属性的值
        log.info("addressBook:{}", addressBook);  //打印日志信息
        addressBookService.save(addressBook);
        return R.success(addressBook); //返回信息
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default") //更新--设置为默认地址
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {  //接收一个包含地址信息的addressBook对象
        log.info("addressBook:{}", addressBook); //打印日志
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();//创建一个LambdaUpdateWrapper对象，用于构建更新条件和设置更新值的条件包装器。
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId()); //设置更新条件，要求user_id等于当前用户的ID。这样可以确保只更新属于当前用户的地址簿信息。
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper); //将所有与当前用户关联的地址簿的is_default标志重置为非默认状态。

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook); //更新数据库中对应的字段值
        return R.success(addressBook); //返回
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象的地址信息");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper); //从数据库中查询一条满足条件的地址信息

        if (null == addressBook) {
            return R.error("没有找到该对象的地址信息");
        } else {
            return R.success(addressBook); //返回
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) { //接收一个参数addressBooK
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook); //日志信息

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime); //按照地址簿的更新时间降序排序

        //SQL:select * from address_book where user_id = ? order by update_time desc(降序)
        return R.success(addressBookService.list(queryWrapper));
    }
}
