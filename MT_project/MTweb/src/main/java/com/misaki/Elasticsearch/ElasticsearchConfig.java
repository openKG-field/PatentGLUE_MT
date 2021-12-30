package com.misaki.Elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;


/**
 * @title: com.misaki.ElasticsearchConfig
 * @Author  misaki
 * @Date: 2021/12/15 15:58
 * @Version 1.0
 */
@Configuration
public class ElasticsearchConfig {
    @Configuration
    public static class RestClientConfig extends AbstractElasticsearchConfiguration {

        @Override
        public RestHighLevelClient elasticsearchClient() {

            final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                    .connectedTo("180.76.156.218:9200")
                    .build();

            return RestClients.create(clientConfiguration).rest();
        }
    }

}
