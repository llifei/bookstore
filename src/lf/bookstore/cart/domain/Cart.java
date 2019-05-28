package lf.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * @author lifei
 */
public class Cart {
    private Map<String,CartItem>map=new LinkedHashMap<String,CartItem>();

    /**
     * 计算合计
     * @return
     */
    public double getTotal(){
        //合计=所有条目小计之和
        BigDecimal total=new BigDecimal("0");
        for(CartItem cartItem:map.values()){
            BigDecimal subtotal=new BigDecimal(cartItem.getSubtotal()+"");
            total=total.add(subtotal);
        }
        return total.doubleValue();
    }

    /**
     * 添加条目
     * @param cartItem
     */
    public  void add(CartItem cartItem){

        String key=cartItem.getBook().getBid();
        if(null!=map&&map.containsKey(key)){
            CartItem item=map.get(key);
            item.setCount(item.getCount()+cartItem.getCount());
            map.put(key,item);
        }else{
            map.put(key,cartItem);
        }
    }

    /**
     * 清空所有条目
     */
    public void clear(){
        map.clear();
    }

    /**
     * 删除指定条目
     * @param bid
     */
    public void delete(String bid){
        map.remove(bid);
    }

    /**
     * 获取所有条目
     * @return
     */
    public Collection<CartItem> getCartItems(){
        return map.values();
    }

}
