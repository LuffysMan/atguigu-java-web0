package com.atguigu.fruit.dao.impl;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.basedao.BaseDAO;

import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {

    @Override
    public List<Fruit> getFruitList(Integer pageNo) {
        return super.executeQuery("select * from t_fruit limit ?, 5;", (pageNo - 1) * 5);
    }

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return super.executeQuery("select * from t_fruit where fname like ? or remark like ? limit ?, 5;", "%" + keyword + "%", "%" + keyword + "%", (pageNo - 1) * 5);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {

        return super.load("select * from t_fruit where fid = ? ", fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        String sql = "update t_fruit set fname = ? , price = ? , fcount = ? , remark = ? where fid = ? ";
        super.executeUpdate(sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark(), fruit.getFid());
    }

    @Override
    public void delFruit(Integer fid) {
        super.executeUpdate("delete from t_fruit where fid = ?", fid);
    }

    @Override
    public void addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(0, ?, ?, ?, ?)";
        super.executeUpdate(sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark());
    }

    @Override
    public int getFruitCount() {
        Long fruitCount = (Long) super.executeComplexQuery("select count(*) from t_fruit")[0];
        if (fruitCount >= Integer.MIN_VALUE && fruitCount <= Integer.MAX_VALUE) {
            return fruitCount.intValue();
        }
        throw new IllegalArgumentException("fruitCount value " + fruitCount + " is out of Integer range.");
    }

    @Override
    public int getFruitCount(String keyword) {
        Long fruitCount = (Long) super.executeComplexQuery("select count(*) from t_fruit where fname like ? or remark like ?", "%" + keyword + "%", "%" + keyword + "%")[0];
        if (fruitCount >= Integer.MIN_VALUE && fruitCount <= Integer.MAX_VALUE) {
            return fruitCount.intValue();
        }
        throw new IllegalArgumentException("fruitCount value " + fruitCount + " is out of Integer range.");
    }
}
