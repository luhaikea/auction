package com.lhk.auction.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.bean.PmsSearchParam;
import com.lhk.auction.bean.PmsSearchProductInfo;
import com.lhk.auction.service.PmsProductService;
import com.lhk.auction.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Reference
    PmsProductService pmsProductService;


    @Override
    public List<PmsSearchProductInfo> list(PmsSearchParam pmsSearchParam) {

        //jest的dsl工具  elasticsearch的客户端
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String valueId[] = pmsSearchParam.getValueId();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(valueId != null){
            for (String s : valueId) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("pmsProductAttrValues.valueId",s);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        if(StringUtils.isNoneBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("productName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        if(StringUtils.isNoneBlank(catalog3Id)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);
        //分页
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.field("productName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //sort 倒序排序
        searchSourceBuilder.sort("id", SortOrder.DESC);
        //aggs聚合函数
        //groupby_attr是一个别名 就是查找每个pmsProductAttrValues.valueId值出现的次数
        //这个聚合的结果主要用于筛选条件的动态刷新  有就是下面的物品集合不一样，上面的筛选条件不一样  就是找到查寻到的物品的所有的不重复的valuedId集合
        // 这是一种方案这里没有用这个方案
        TermsAggregationBuilder groupby_attr = AggregationBuilders.terms("groupby_attr").field("pmsProductAttrValues.valueId");
        searchSourceBuilder.aggregation(groupby_attr);

        String dslStr = searchSourceBuilder.toString();

        //用api执行复杂查询
        List<PmsSearchProductInfo> pmsSearchProductInfos = new ArrayList<>();
        Search search = new Search.Builder(dslStr).addIndex("auction").addType("PmsProductInfo").build();
        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<PmsSearchProductInfo, Void>> hits = null;
        if(execute != null){
            hits = execute.getHits(PmsSearchProductInfo.class);
        }

        for(SearchResult.Hit<PmsSearchProductInfo, Void> hit : hits){
            PmsSearchProductInfo source = hit.source;

            Map<String, List<String>> highlight = hit.highlight;
            if(highlight!=null){
                String productName = highlight.get("productName").get(0);
                source.setProductName(productName);
            }

            pmsSearchProductInfos.add(source);
        }

        return pmsSearchProductInfos;
    }

    @Override
    public void updateSearch(String productId) throws IOException {

        // 查询mysql数据
        PmsProductInfo pmsProductInfo = pmsProductService.getSearchProductById(productId);
        // 转化为es的数据结构
        PmsSearchProductInfo pmsSearchProductInfo = new PmsSearchProductInfo();
        BeanUtils.copyProperties(pmsProductInfo, pmsSearchProductInfo);
        // 导入es  这里指定在elasticsearch中的document的主键为pmsSearchProductInfo.getId()  更新时直接写入会自动覆盖
        Index put = new Index.Builder(pmsSearchProductInfo).index("auction").type("PmsProductInfo").id(pmsSearchProductInfo.getId() + "").build();
        jestClient.execute(put);
    }

}
