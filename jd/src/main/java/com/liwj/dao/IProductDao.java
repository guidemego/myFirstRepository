package com.liwj.dao;

import com.liwj.pojo.ResultModel;
import org.apache.solr.client.solrj.SolrQuery;

public interface IProductDao {
    ResultModel queryIndex(SolrQuery query);
}
