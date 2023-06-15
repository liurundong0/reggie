package edu.czjt.reggie.dto;

import edu.czjt.reggie.entity.Dish;
import edu.czjt.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

}
