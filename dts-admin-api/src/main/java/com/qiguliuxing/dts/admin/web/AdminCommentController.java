package com.qiguliuxing.dts.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.admin.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.admin.service.AdminDataAuthService;
import com.qiguliuxing.dts.admin.util.AuthSupport;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.core.validator.Order;
import com.qiguliuxing.dts.core.validator.Sort;
import com.qiguliuxing.dts.db.domain.DtsComment;
import com.qiguliuxing.dts.db.service.DtsCommentService;

@RestController
@RequestMapping("/admin/comment")
@Validated
public class AdminCommentController {
	private static final Logger logger = LoggerFactory.getLogger(AdminCommentController.class);

	@Autowired
	private DtsCommentService commentService;
	
	@Autowired
	private AdminDataAuthService adminDataAuthService;

	@RequiresPermissions("admin:comment:list")
	@RequiresPermissionsDesc(menu = { "商品管理", "评论管理" }, button = "查询")
	@GetMapping("/list")
	public Object list(String userId, String valueId, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer limit,
			@Sort @RequestParam(defaultValue = "add_time") String sort,
			@Order @RequestParam(defaultValue = "desc") String order) {
		logger.info("【请求开始】操作人:[" + AuthSupport.userName()+ "] 商品管理->评论管理->查询,请求参数:userId:{},page:{}", userId, page);

		// 需要区分数据权限，如果属于品牌商管理员，则需要获取当前用户管理品牌店铺
		List<Integer> brandIds = null;
		if (adminDataAuthService.isBrandManager()) {
			brandIds = adminDataAuthService.getBrandIds();
			logger.info("运营商管理角色操作，需控制数据权限，brandIds:{}", JSONObject.toJSONString(brandIds));

			if (brandIds == null || brandIds.size() == 0) {// 如果尚未管理任何入驻店铺，则返回空数据
				Map<String, Object> data = new HashMap<>();
				data.put("total", 0L);
				data.put("items", null);

				logger.info("【请求结束】商品管理->评论管理->查询:{}", JSONObject.toJSONString(data));
				return ResponseUtil.ok(data);
			}
		}
		List<DtsComment> commentList = null;
		long total = 0L;
		if (brandIds == null || brandIds.size() == 0) {
			commentList = commentService.querySelective(userId, valueId, page, limit, sort, order);
			total = PageInfo.of(commentList).getTotal();
		} else {
			commentList = commentService.queryBrandCommentSelective(brandIds,userId, valueId, page, limit, sort, order);
			total = PageInfo.of(commentList).getTotal();
		}
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", commentList);

		logger.info("【请求结束】商品管理->评论管理->查询:total:{}", total);
		return ResponseUtil.ok(data);
	}

	@RequiresPermissions("admin:comment:delete")
	@RequiresPermissionsDesc(menu = { "商品管理", "评论管理" }, button = "删除")
	@PostMapping("/delete")
	public Object delete(@RequestBody DtsComment comment) {
		logger.info("【请求开始】操作人:[" + AuthSupport.userName()+ "] 商品管理->评论管理->删除,请求参数:{}", JSONObject.toJSONString(comment));

		Integer id = comment.getId();
		if (id == null) {
			return ResponseUtil.badArgument();
		}
		commentService.deleteById(id);

		logger.info("【请求结束】商品管理->评论管理->删除:响应结果:{}", "成功!");
		return ResponseUtil.ok();
	}

}
