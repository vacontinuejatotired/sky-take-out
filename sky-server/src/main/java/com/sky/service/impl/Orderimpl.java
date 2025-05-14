package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrdersService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Orderimpl implements OrdersService {
    private static final Logger log = LoggerFactory.getLogger(Orderimpl.class);
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        AddressBook addressBookMapperById = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBookMapperById == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart>shoppingCarts=shoppingCartMapper.List(shoppingCart);
        if(shoppingCarts==null||shoppingCarts.size()==0){
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //判断传入数据
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBookMapperById.getPhone());
        orders.setAddress(addressBookMapperById.getProvinceName()+addressBookMapperById.getCityName()+addressBookMapperById.getDistrictName());
        orders.setConsignee(addressBookMapperById.getConsignee());
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        ordersMapper.insert(orders);
        //插入订单表
        List<OrderDetail>list=new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            list.add(orderDetail);
        }
        orderDetailMapper.insertBatch(list);
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        OrderSubmitVO orderSubmitVO=OrderSubmitVO.builder().id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .build();
        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws  Exception{
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code","ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        Orders orders1=ordersMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        Orders orders=Orders.builder().id(orders1.getId()).checkoutTime(LocalDateTime.now()).payStatus(Orders.PAID).status(Orders.TO_BE_CONFIRMED).build();
        ordersMapper.update(orders);
        return vo;
    }

    @Override
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO1) {
        PageHelper.startPage(ordersPageQueryDTO1.getPage(), ordersPageQueryDTO1.getPageSize());
        OrdersPageQueryDTO ordersPageQueryDTO=new OrdersPageQueryDTO();
        ordersPageQueryDTO.setPage(ordersPageQueryDTO1.getPage());
        ordersPageQueryDTO.setPageSize(ordersPageQueryDTO1.getPageSize());
        ordersPageQueryDTO.setStatus(ordersPageQueryDTO1.getStatus());
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        Page<Orders>page=ordersMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO>list=new ArrayList<>();
        if(page!=null&&page.getTotal()>0){
            for (Orders orders : page) {
                Long orderId = orders.getId();
                List<OrderDetail> orderDetails=orderDetailMapper.getByOrderid(orderId);
                OrderVO orders1=new OrderVO();
                BeanUtils.copyProperties(orders,orders1);
                orders1.setOrderDetailList(orderDetails);
                list.add(orders1);
            }
        }
        return new PageResult(page.getTotal(),list);
    }

    @Override
    public OrderVO selectOrderDetail(Long id) {
        OrderVO orderVO=new OrderVO();
        orderVO.setUserId(BaseContext.getCurrentId());
        Orders orders=ordersMapper.getById(id);
        List<OrderDetail>orderDetailList=orderDetailMapper.getByOrderid(orders.getId());
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    @Override
    public void cancelById(Long id) {
        Orders ordersDB = ordersMapper.getById(id);
        if(ordersDB==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(ordersDB.getStatus()>2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if(ordersDB.getPayStatus().equals(Orders.UN_PAID)){
            ordersDB.setPayStatus(Orders.REFUND);
        }
        if(ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            ordersDB.setStatus(Orders.CANCELLED);
        }
        ordersDB.setCancelTime(LocalDateTime.now());
        ordersDB.setCancelReason("用户取消订单");
        ordersMapper.update(ordersDB);
    }

    @Override
    @Transactional
    public void submitAgain(Long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderid(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    @Override
    public OrderStatisticsVO getStatisticsVo() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(ordersMapper.coutStatus(Orders.CONFIRMED));
        orderStatisticsVO.setToBeConfirmed(ordersMapper.coutStatus(Orders.TO_BE_CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(ordersMapper.coutStatus(Orders.DELIVERY_IN_PROGRESS));
        return orderStatisticsVO;
    }

    @Override
    public PageResult searchList(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders>orders=ordersMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO>list=new ArrayList<>();
        if(orders!=null&&orders.getTotal()>0){
            for (Orders order : orders) {
                OrderVO orders1=new OrderVO();
                BeanUtils.copyProperties(order,orders1);
                List<OrderDetail>orderDetailList=orderDetailMapper.getByOrderid(order.getId());
                orders1.setOrderDetailList(orderDetailList);
                String orderDishes=null;
                for(OrderDetail orderDetail:orderDetailList){
                    orderDishes+=orderDishes;
                }
                orders1.setOrderDishes(orderDishes);
                list.add(orders1);
            }
        }
        else{
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        return new PageResult(orders.getTotal(),list);
    }

    @Override
    public void confirm(Long id) {
        Orders ordersDB = ordersMapper.getById(id);
        if(ordersDB==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(ordersDB.getStatus()>2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        ordersDB.setStatus(Orders.CONFIRMED);
        ordersMapper.update(ordersDB);
    }

}
