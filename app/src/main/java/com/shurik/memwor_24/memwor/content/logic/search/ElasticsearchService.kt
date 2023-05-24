//package com.shurik.memwor_24.memwor.content.logic.search
//
//import org.elasticsearch.action.search.SearchRequest
//import org.elasticsearch.client.RequestOptions
//import org.elasticsearch.client.RestHighLevelClient
//import org.elasticsearch.client.indices.CreateIndexRequest
//import org.elasticsearch.client.indices.GetIndexRequest
//import org.elasticsearch.common.settings.Settings
//import org.elasticsearch.index.query.QueryBuilders
//import org.elasticsearch.index.reindex.ScrollableHitSource
//import org.elasticsearch.search.SearchHit
//import org.elasticsearch.search.builder.SearchSourceBuilder
//import org.elasticsearch.xcontent.XContentFactory.jsonBuilder
//
//class ElasticsearchService(private val client: RestHighLevelClient) {
//
//    fun createIndexIfNotExists(indexName: String) {
//        val getIndexRequest = GetIndexRequest(indexName)
//        val indexExists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT)
//
//        if (!indexExists) {
//            val request = CreateIndexRequest(indexName)
//                .settings(
//                    Settings.builder()
//                        .put("index.number_of_shards", 3)
//                        .put("index.number_of_replicas", 2)
//                )
//                .mapping(
//                    jsonBuilder()
//                        .startObject()
//                        .startObject("properties")
//                        .startObject("title")
//                        .field("type", "text")
//                        .endObject()
//                        .startObject("content")
//                        .field("type", "text")
//                        .endObject()
//                        .startObject("author")
//                        .field("type", "text")
//                        .endObject()
//                        .endObject()
//                        .endObject()
//                )
//
//            client.indices().create(request, RequestOptions.DEFAULT)
//        }
//    }
//
//    // Функции для индексации и поиска контента
//    fun searchContent(query: String, indexName: String): List<SearchHit> {
//        val searchSourceBuilder = SearchSourceBuilder()
//            .query(
//                QueryBuilders.boolQuery()
//                    .should(QueryBuilders.matchQuery("title", query))
//                    .should(QueryBuilders.matchQuery("content", query))
//                    .should(QueryBuilders.matchQuery("author", query))
//            )
//
//        val searchRequest = SearchRequest(indexName)
//            .source(searchSourceBuilder)
//
//        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
//        return searchResponse.hits.hits.toList()
//    }
//}
