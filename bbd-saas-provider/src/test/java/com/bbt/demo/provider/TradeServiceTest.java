package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.TradeService;
import com.bbd.saas.enums.TradeStatus;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.OrderSnap;
import com.bbd.saas.mongoModels.Trade;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.Goods;
import com.bbd.saas.vo.Reciever;
import com.bbd.saas.vo.Sender;
import com.bbd.saas.vo.TradeQueryVO;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TradeServiceTest {
	@Autowired
	private TradeService tradeService;
	@Autowired
	private OrderService orderService;
	private ObjectId uId = new ObjectId("573c5f421e06c8275c08183c");
	//junit.framework.TestCase时用
	public void setUp() throws Exception{
        System.out.println("set up");
        // 生成成员变量的实例
    }
	//junit.framework.TestCase时用
    public void tearDown() throws Exception{
        System.out.println("tear down");
    }

	@Test
	public void testSave() throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		TradeStatus [] status = new TradeStatus[]{
			TradeStatus.WAITCATCH,TradeStatus.WAITCATCH,TradeStatus.WAITGET,
			TradeStatus.WAITPAY,TradeStatus.WAITPAY,TradeStatus.WAITGET,
			TradeStatus.CANCELED,TradeStatus.CANCELED,TradeStatus.GETED,
			TradeStatus.GETED,TradeStatus.GETED,TradeStatus.RETURNED,
			TradeStatus.RETURNED,TradeStatus.WAITGET
		};
		for(int j = 0; j<status.length; j++){
			Trade trade = createTrade(j,status[j],format);
			tradeService.save(trade);
		}
		Assert.isTrue(true);//无用
	}
	private Trade createTrade(int i, TradeStatus status,SimpleDateFormat format){
		Trade trade = new Trade();
		trade.setTradeNo("T" + format.format(new Date())+i);
		trade.setTradeStatus(status);
		trade.setuId(new ObjectId("573c5f421e06c8275c08183c"));
		if(status == TradeStatus.WAITPAY){
			trade.setAmountMay(25000);
		}else {
			trade.setEmbraceId(new ObjectId("572bfbdb7f4a2019e4ed81ce"));
			trade.setAmountMay(25000);
			trade.setAmountReal(23000);
			trade.setAmountReturn(1500);
		}
		trade.setDateAdd(new Date());
		trade.setDateUpd(new Date());
		trade.setOrderSnaps(getOrderSnapList(format, trade.getTradeNo()));
		return trade;
	}

	private List<OrderSnap > getOrderSnapList(SimpleDateFormat format, String tradeNo){
		List<OrderSnap > orderSnapList = new ArrayList<OrderSnap>();
		//商品
		String[] pro = new String[]{"红富士","花生","红薯","桃子","梨"};
		//收件人姓名
		String[] names = new String[]{"朱德","彭德怀","粟裕","陈毅","陈赓"};
		//收件人省
		String[] pros = new String[]{"北京","山东","山西","陕西","江苏"};
		//收件人市
		String[] citys = new String[]{"北京","潍坊","大同","西安","南京"};
		//收件人区
		String[] areas = new String[]{"朝阳","奎文区","海淀","崇文区","丰台区"};
		//收件人详细地址
		String[] address = new String[]{"双井","胜利东街","五路居","北京站","汽车客运站"};
		int rand = 0;
		for(int i = 0; i<1; i++){
			OrderSnap orderSnap = new OrderSnap();
			orderSnap.setMailNum("BBD" + tradeNo + i);
			orderSnap.setOrderNo("O" + format.format(new Date()) + i);

			List<Goods> goodsList = new ArrayList<Goods>();
			rand = (int)(Math.random() * 5);
			for(int j = 0; j<rand; j++){
				Goods goods = new Goods();
				goods.setCode("NF000"+i+j);
				goods.setTitle(pro[(int)(Math.random() * 5)]);
				goods.setNum((int)(Math.random() * 10));
				goodsList.add(goods);
			}
			orderSnap.setGoods(goodsList);
			//收件人
			Reciever reciever = new Reciever();
			reciever.setName(names[(int)(Math.random() * 5)]);
			if(i<10){
				reciever.setPhone("1500106880"+i);
			}else{
				reciever.setPhone("150010688"+i);
			}
			rand = (int)(Math.random() * 5);
			reciever.setProvince(pros[rand]);
			reciever.setCity(citys[rand]);
			reciever.setArea(areas[rand]);
			reciever.setAddress(address[rand]);
			orderSnap.setReciever(reciever);
			orderSnapList.add(orderSnap);
			saveOrderBySnap(orderSnap, tradeNo);
		}
		return orderSnapList;
	}
	private  void saveOrderBySnap(OrderSnap orderSnap, String tradeNo){
		Order order = new Order();
		order.setTradeNo(tradeNo);
		order.setuId(new ObjectId("573c5f421e06c8275c08183c"));
		order.setOrderNo(orderSnap.getOrderNo());
		order.setMailNum(orderSnap.getMailNum());
		Sender sender = new Sender();
		sender.setName("安迪");
		sender.setPhone("13255555555");
		sender.setProvince("北京");
		sender.setCity("北京市");
		sender.setArea("朝阳区");
		sender.setAddress("欢乐颂小区");
		order.setSender(sender);
		order.setReciever(orderSnap.getReciever());
		order.setSrc(orderSnap.getSrc());
		order.setGoods(orderSnap.getGoods());
		order.setOrderCreate(orderSnap.getOrderCreate());
		order.setOrderPay(orderSnap.getOrderPay());
		order.setErrorRemark(orderSnap.getErrorRemark());
		order.setDateAdd(orderSnap.getDateAdd());
		order.setDateUpd(orderSnap.getDateUpd());
		orderService.save(order);
	}

	@Test
	public void testFindPages() throws Exception{
		//设置查询条件
		TradeQueryVO tradeQueryVO = new TradeQueryVO();
		tradeQueryVO.uId = uId;
		tradeQueryVO.tradeStatus = -1;
		tradeQueryVO.noLike = null;
		tradeQueryVO.rcvKeyword = "奎文";
		tradeQueryVO.dateAddStart = "2016-06-21";
		tradeQueryVO.dateAddEnd = "2016-06-21";
		//若此方法超时，则把设置快件数量、揽件员、订单状态提到controller中
		PageModel<Trade> tradePage = tradeService.findTradePage(0, tradeQueryVO);
		Assert.isTrue(true);//无用
	}

}
