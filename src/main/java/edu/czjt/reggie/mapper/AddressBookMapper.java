package edu.czjt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.czjt.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
    //通过继承BaseMapper接口，AddressBookMapper接口获得了通用的数据库操作方法
    //可以对AddressBook实体类进行增删改查等操作

}
