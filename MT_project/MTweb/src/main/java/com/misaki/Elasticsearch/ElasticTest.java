package com.misaki.Elasticsearch;


import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @title: com.misaki.ElasticTest
 * @Author  misaki
 * @Date: 2021/12/15 16:09
 * @Version 1.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void test() throws IOException {

        // 检索索引
        final GetIndexRequest indexRequest = new GetIndexRequest("es");
        final boolean exists = restHighLevelClient.indices().exists(indexRequest, RequestOptions.DEFAULT);
        System.out.println("-------------------------");
        System.out.println(exists);
        System.out.println("-------------------------");
    }

    @Test
    public void test01() throws Exception {
        final GetRequest request = new GetRequest("es", "1");
        final GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        final String string = response.toString();
        System.out.println("-------------------------");
        System.out.println(string);
        System.out.println("-------------------------");
    }

    @Test
    public void test02() throws Exception {
        SearchResponse search = restHighLevelClient.search(new SearchRequest("es"), RequestOptions.DEFAULT);
        System.out.println("-------------------------");
        System.out.println(search);
        System.out.println("-------------------------");
    }

    @Test
    public void test03() throws IOException {
        GetRequest getRequest=new GetRequest("es","servlet.MT","128376");
        RequestOptions option=RequestOptions.DEFAULT;
        GetResponse getResponse = restHighLevelClient.get(getRequest, option);
        System.out.println("-------------------------");
        System.out.println("_index: " + getResponse.getIndex());
        System.out.println(getResponse);
        System.out.println("-------------------------");
    }

    @Test
    public void test04_search() throws IOException {
        SearchRequest searchRequest = new SearchRequest("es"); //将请求限制为一个索引
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.types("servlet.MT"); //将请求限制为一个类型。
        searchRequest.source(sourceBuilder); //将SearchSourceBuilder添加到SeachRequest

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("-------------------------");
        System.out.println(searchResponse);
        System.out.println("-------------------------");
    }

    // 中文match匹配
    @Test
    public void Chinese_keywords_search2() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("Chinese.keyword", "机器翻译"));
        searchRequest2(searchSourceBuilder);
    }

    // 英文match匹配
    @Test
    public void English_keywords_search2() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("English.keyword", "engine"));
        searchRequest2(searchSourceBuilder);
    }

    private void searchRequest2(SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest("es");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println("-------------------------");
        System.out.println(searchResponse);
        System.out.println("-------------------------");
    }
}

