package com.qiguliuxing.dts.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiguliuxing.dts.admin.util.AuthSupport;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.domain.DtsBrand;
import com.qiguliuxing.dts.db.service.DtsBrandService;
import com.qiguliuxing.dts.db.service.DtsRoleService;

@Service
public class AdminDataAuthService {

	@Autowired
	private DtsRoleService roleService;
	
	@Autowired
	private DtsBrandService brandService;
	
	/**
	 * 是否属于运营商管理员，超级管理员除外
	 * @return
	 */
	public boolean isBrandManager() {
		Integer[] roleIds = null;
		DtsAdmin currentUser = AuthSupport.currentUser();
		if (currentUser != null) {
			roleIds = currentUser.getRoleIds();
			Set<String> roles = roleService.queryByIds(roleIds);
			//仅仅只是品牌管理员且不属于超级管理员
			if (roles.contains(AuthSupport.BRAND_ROLE_NAME) && !roles.contains(AuthSupport.SUPER_ROLE_NAME)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前用户的管理的品牌商铺
	 * @return
	 */
	public List<Integer> getBrandIds() {
		List<Integer> brandIds = null;
		DtsAdmin currentUser = AuthSupport.currentUser();
		List<DtsBrand> brands = brandService.getAdminBrands(currentUser.getId());
		if (brands != null && brands.size() > 0) {
			brandIds = new ArrayList<Integer>();
			for (DtsBrand brand:brands) {
				brandIds.add(brand.getId());
			}
		}
		return brandIds;
	}
}
