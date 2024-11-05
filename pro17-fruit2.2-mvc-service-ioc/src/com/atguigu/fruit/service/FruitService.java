package com.atguigu.fruit.service;
import com.atguigu.fruit.pojo.Fruit;

import java.util.List;


public interface FruitService {
    // 增
    public void addFruit(Fruit fruit);

    // 删
    public void delFruit(Integer fid);

    // 改
    public void updateFruit(Fruit fruit);

    // 查
    public Fruit getFruitByFid(Integer fid);

    public List<Fruit> getFruitList(String keyword, Integer pageNo);

    public Integer getPageCount(String keyword);
}
