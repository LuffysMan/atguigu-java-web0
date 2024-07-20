package com.atguigu.fruit.dao;
import com.atguigu.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //获取指定页面上的库存列表信息, 每页显示5条
    List<Fruit> getFruitList(Integer pageNo);

    //获取指定关键字指定页面上的库存列表信息, 每页显示5条
    List<Fruit> getFruitList(String keyword, Integer pageNo);

    // 根据fid获取特定的水果库存信息
    Fruit getFruitByFid(Integer fid);

    //修改指定的库存记录
    void updateFruit(Fruit fruit);

    //根据fid删除指定的库存记录
    void delFruit(Integer fid);

    //添加新库存记录
    void addFruit(Fruit fruit);

    //获取库存总数量
    int getFruitCount();

    //获取符合关键字的水果的库存总数量
    int getFruitCount(String keyword);
}
