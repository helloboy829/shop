package com.qiguliuxing.dts.admin.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.admin.dao.CategorySellVo;
import com.qiguliuxing.dts.admin.dao.OrderAmtsVo;
import com.qiguliuxing.dts.admin.dao.UserOrderCntVo;
import com.qiguliuxing.dts.admin.util.AuthSupport;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.bean.CategorySellAmts;
import com.qiguliuxing.dts.db.bean.DayStatis;
import com.qiguliuxing.dts.db.service.DtsGoodsProductService;
import com.qiguliuxing.dts.db.service.DtsGoodsService;
import com.qiguliuxing.dts.db.service.DtsOrderService;
import com.qiguliuxing.dts.db.service.DtsUserService;

@RestController
@RequestMapping("/admin/dashboard")
@Validated
public class AdminDashbordController {
	private static final Logger logger = LoggerFactory.getLogger(AdminDashbordController.class);

	private static final int STATIS_DAYS_RANG = 30;// 统计的天数范围，一个月数据

	@Autowired
	private DtsUserService userService;
	@Autowired
	private DtsGoodsService goodsService;
	@Autowired
	private DtsGoodsProductService productService;
	@Autowired
	private DtsOrderService orderService;

	@GetMapping("")
	public Object info() {
		logger.info("【请求开始】操作人:[" + AuthSupport.userName() + "] 系统管理->首页仪表盘查询");

		int userTotal = userService.count();
		int goodsTotal = goodsService.count();
		int productTotal = productService.count();
		int orderTotal = orderService.count();
		Map<String, Integer> data = new HashMap<>();
		data.put("userTotal", userTotal);
		data.put("goodsTotal", goodsTotal);
		data.put("productTotal", productTotal);
		data.put("orderTotal", orderTotal);

		logger.info("【请求结束】系统管理->首页仪表盘查询:响应结果:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}

	@GetMapping("/chart")
	public Object chart() {
		logger.info("【请求开始】操作人:[" + AuthSupport.userName() + "] 系统管理->首页图表查询");

		// 近期用户，订单增长量查询
		UserOrderCntVo userOrderCnt = new UserOrderCntVo();
		List<DayStatis> userCnts = userService.recentCount(STATIS_DAYS_RANG);
		List<DayStatis> orderCnts = orderService.recentCount(STATIS_DAYS_RANG);

		String[] dayData = unionDayData(userCnts, orderCnts);
		userOrderCnt.setDayData(dayData);
		userOrderCnt.setUserCnt(fetchArrCnt(dayData, userCnts));
		userOrderCnt.setOrderCnt(fetchArrCnt(dayData, orderCnts));

		// 订单请款统计，订单笔数与订单金额
		OrderAmtsVo orderAmts = fetchOrderAmtsVo(orderCnts);

		// 大类销售统计情况
		List<CategorySellAmts> categorySellStatis = orderService.categorySell();// 统计总量
		CategorySellVo categorySell = fetchCategorySell(categorySellStatis);

		Map<String, Object> data = new HashMap<>();
		data.put("userOrderCnt", userOrderCnt);
		data.put("orderAmts", orderAmts);
		data.put("categorySell", categorySell);

		logger.info("【请求结束】系统管理->首页图表查询:响应结果:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}

	/**
	 * 获取日期数据并排序
	 * 
	 * @param userCnts
	 * @param orderCnts
	 * @return
	 */
	private String[] unionDayData(List<DayStatis> userCnts, List<DayStatis> orderCnts) {
		Set<String> days = new HashSet<>();
		for (DayStatis userCnt : userCnts) {
			days.add(userCnt.getDayStr());
		}
		for (DayStatis orderCnt : orderCnts) {
			days.add(orderCnt.getDayStr());
		}

		/*days.stream().sorted(Comparator.reverseOrder());// 排序
		return days.toArray(new String[days.size()]);*/
		
		List<String> list = new ArrayList<String>(days);
		Collections.sort(list);
		return list.toArray(new String[0]);
	}

	/**
	 * 从统计集合中获取数量 不存在则设置 0
	 * 
	 * @param dayData
	 * @param dayStatisCnts
	 * @return
	 */
	private int[] fetchArrCnt(String[] dayData, List<DayStatis> dayStatisCnts) {
		int[] arrCnts = new int[dayData.length];
		for (int i = 0; i < dayData.length; i++) {
			int dayCnt = 0;
			String dayStr = dayData[i];
			for (DayStatis ds : dayStatisCnts) {
				if (dayStr.equals(ds.getDayStr())) {
					dayCnt = ds.getCnts();
					break;
				}
			}
			arrCnts[i] = dayCnt;
		}
		return arrCnts;
	}

	/**
	 * 获取订单统计数据
	 * 
	 * @param orderCnts
	 * @return
	 */
	private OrderAmtsVo fetchOrderAmtsVo(List<DayStatis> orderCnts) {
		OrderAmtsVo orderAmts = new OrderAmtsVo();
		int size = 0;
		if (orderCnts != null && orderCnts.size() > 0) {
			size = orderCnts.size();
		}
		String[] dayData = new String[size];
		int[] orderCntData = new int[size];
		BigDecimal[] orderAmtData = new BigDecimal[size];
		for (int i = 0; i < size; i++) {
			dayData[i] = orderCnts.get(i).getDayStr();
			orderCntData[i] = orderCnts.get(i).getCnts();
			orderAmtData[i] = orderCnts.get(i).getAmts();
		}
		orderAmts.setDayData(dayData);
		orderAmts.setOrderAmtData(orderAmtData);
		orderAmts.setOrderCntData(orderCntData);
		return orderAmts;
	}

	/**
	 * 获取大类的销售统计数据
	 * 
	 * @param categorySellStatis
	 * @return
	 */
	private CategorySellVo fetchCategorySell(List<CategorySellAmts> categorySellData) {
		CategorySellVo categorySell = new CategorySellVo();
		int size = 0;
		if (categorySellData != null && categorySellData.size() > 0) {
			size = categorySellData.size();
		}
		String[] categoryNames = new String[size];
		for (int i = 0; i < size; i++) {
			categoryNames[i] = categorySellData.get(i).getName();
		}
		categorySell.setCategoryNames(categoryNames);
		categorySell.setCategorySellData(categorySellData);
		return categorySell;
	}

}
