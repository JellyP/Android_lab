package com.example.administrator.meetingtaste.model;

import com.example.administrator.meetingtaste.model.Items;
import com.example.administrator.meetingtaste.model.ShoppingCar;

/**
 * Created by Administrator on 2018/1/4.
 */

public class ShoppingcarAndItem
{
    final public static String SHOPPINGCAR_ITEM_TABLE = "shoppingcar_item";

    public ShoppingCar shoppingCar;
    public Items items;

    public ShoppingCar getShoppingCar() {
        return shoppingCar;
    }

    public void setShoppingCar(ShoppingCar shoppingCar) {
        this.shoppingCar = shoppingCar;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

}
