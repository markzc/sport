package com.itheima.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.dao.product.ColorDao;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;
import com.itheima.core.pojo.product.Product;
import com.itheima.core.pojo.product.Sku;
import com.itheima.core.pojo.product.SkuQuery;

/**
 * 商品详情页面
 * @author lx
 *
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService{

	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	
	//通过商品ID 查询商品对象
	public Product selectProductById(Long id){
		return productDao.selectByPrimaryKey(id);
	}
	//通过商品ID 查询SKu对象结果集（里面有颜色对象）   库存大于0的
	public List<Sku> selectSkuListByProductId(Long id){
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id).andStockGreaterThan(0);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	
}
