package com.liwj.dao.impl;

import com.liwj.dao.IProductDao;
import com.liwj.pojo.Product;
import com.liwj.pojo.ResultModel;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDaoImpl implements IProductDao{
    @Autowired
    private SolrServer solrServer;
    @Override
    public ResultModel queryIndex(SolrQuery query) {
        ResultModel model=new ResultModel();
        List<Product> productList=new ArrayList<>();
        try {
            QueryResponse response = solrServer.query(query);
            SolrDocumentList solrDocuments = response.getResults();
            Long numFound = solrDocuments.getNumFound();
            model.setTotalCount(numFound.intValue());
            for(SolrDocument doc:solrDocuments){
                Product product=new Product();
                //产品id
                String id= (String) doc.get("id");
                product.setPid(id);
                //产品名称
                Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
                Map<String, List<String>> listMap = highlighting.get(id);
                List<String> product_name = listMap.get("product_name");
                if(product_name!=null&&product_name.size()>0){
                    product.setName(product_name.get(0));
                }else{
                    product.setName((String) doc.get("product_name"));
                }
                String catalog_name= (String) doc.get("product_catalog_name");
                //产品价格
                Float product_price = (Float) doc.get("product_price");
                product.setPrice(product_price);
                //产品图片
                product.setPicture((String) doc.get("product_picture"));
                productList.add(product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        model.setProductList(productList);
        return model;
    }
}
