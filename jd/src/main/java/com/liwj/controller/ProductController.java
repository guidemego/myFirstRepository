package com.liwj.controller;
import com.liwj.pojo.ResultModel;
import com.liwj.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;
    /**
     * 客户端传过来的有：String queryString,string catalog_name,string price,Integer sort,Integer page,Integer rows
     */
    @RequestMapping("/list")
    public String queryIndex(String queryString,
                             String catalog_name,
                             String price,
                             @RequestParam(value = "sort",defaultValue = "1") String sort,
                             @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "30") Integer pageSize,
                             Model model){
        //调用productService方法
        ResultModel result = productService.queryIndex(queryString, catalog_name, price, sort, pageNum, pageSize);
        model.addAttribute("result",result);
        //页码回显
        model.addAttribute("queryString",queryString);
        model.addAttribute("catalog_name",catalog_name);
        model.addAttribute("price",price);
        model.addAttribute("sort",sort);
        return "product_list";
    }
}
