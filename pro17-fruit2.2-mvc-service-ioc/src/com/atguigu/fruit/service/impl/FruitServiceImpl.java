package com.atguigu.fruit.service.impl;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.fruit.service.FruitService;

import java.util.List;

public class FruitServiceImpl implements FruitService {
    private final FruitDAO fruitDAO = null;

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
    }

    @Override
    public void delFruit(Integer fid) {
        fruitDAO.delFruit(fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitDAO.getFruitByFid(fid);
    }

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return fruitDAO.getFruitList(keyword, pageNo);
    }

    @Override
    public Integer getPageCount(String keyword) {
        int fruitCount = fruitDAO.getFruitCount();
        return (fruitCount + 5 - 1) / 5;
    }


}
