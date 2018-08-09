package com.liwj.service;

import com.liwj.pojo.ResultModel;

public interface IProductService {
    ResultModel queryIndex(String queryString,String catalog_name,String price,String sort,Integer pageNum,Integer pageSize);
}
