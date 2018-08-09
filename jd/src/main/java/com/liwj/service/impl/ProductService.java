package com.liwj.service.impl;

import com.liwj.dao.IProductDao;
import com.liwj.pojo.ResultModel;
import com.liwj.service.IProductService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductDao productDao;

    @Override
    public ResultModel queryIndex(String queryString,String catalog_name,String price,String sort,Integer pageNum,Integer pageSize) {
        SolrQuery solrQuery=new SolrQuery();
        if(queryString!=null&&!"".equals(queryString)){
            solrQuery.setQuery(queryString);
        }else{
            solrQuery.setQuery("*:*");
        }
        //默认搜索域
        solrQuery.set("df","product_keywords");
        //种类过滤
        if(!StringUtils.isEmpty(catalog_name)){
                solrQuery.addFilterQuery("product_catalog_name:"+catalog_name);
        }
        //价格过滤
        if(!StringUtils.isEmpty(price)){
            String[] prices = price.split("-");
            solrQuery.addFilterQuery("product_price:["+prices[0]+" TO "+prices[1]+"]");
        }
        //排序
        solrQuery.setSort("product_price","1".equals(sort)? SolrQuery.ORDER.asc: SolrQuery.ORDER.desc);
        //分页
        if(pageNum<0){
            pageNum=1;
        }
        int startNo=(pageNum-1)*pageSize;
        solrQuery.setStart(startNo);
        solrQuery.setRows(pageSize);
        //高亮显示
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("product_name");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        ResultModel resultModel = productDao.queryIndex(solrQuery);
        resultModel.setPageNum(pageNum);
        resultModel.setPageSize(pageSize);
        Integer totalCount = resultModel.getTotalCount();
        resultModel.setTotalPage(totalCount%pageSize>0?totalCount/pageSize:totalCount/pageSize+1);
        return resultModel;
    }
}
