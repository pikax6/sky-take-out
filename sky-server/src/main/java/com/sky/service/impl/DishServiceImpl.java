package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品及其口味数据
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        // 对象拷贝
        BeanUtils.copyProperties(dishDTO, dish);

        // 1. 向菜品表插入1条数据
        dishMapper.insert(dish);

        // 2. 获取插入后的菜品id（关键！MyBatis会自动回显id）
        Long dishId = dish.getId();

        // 3. 给口味集合赋值dishId
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId); // 绑定菜品id
            });
            // 4. 批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

}
