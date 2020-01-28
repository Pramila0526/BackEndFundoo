package com.bridgelabz.fundooappbackend.elastic.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.bridgelabz.fundooappbackend.note.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
/*********************************************************************************************************
 * @author 	:Pramila Mangesh Tawari
 * Purpose	:Elastic Service Implementation to perform fast operations
 *
 ***********************************************************************************************************/
@Service
public class ElasticServiceImplementation implements ElasticService {

	private RestHighLevelClient client;

	private ObjectMapper objectMapper;

	@Autowired
	public ElasticServiceImplementation(RestHighLevelClient client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;
	}

	static String INDEX = "notedata"; // database
	static String TYPE = "note"; // table

	public String createNote(Note note) throws Exception {

        System.out.println("in elastic");
        @SuppressWarnings("unchecked")
		Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE,
        				String.valueOf(note.getId()))
        .source(documentMapper); //.index(INDEX).type(TYPE);

        System.out.println("****"+indexRequest);
        System.out.println("after request");
        IndexResponse indexResponse = client.index(indexRequest,
        					RequestOptions.DEFAULT);

        System.out.println("****"+indexResponse);
        System.out.println("note is :"+indexResponse.getResult().name());
        return indexResponse.getResult().name();
	}
	
	public Note searchById(String noteId) throws Exception {

		GetRequest getRequest = new GetRequest(INDEX, TYPE, noteId);

		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		Map<String, Object> resultMap = getResponse.getSource();

		return objectMapper.convertValue(resultMap, Note.class);

	}

	public String updateNote(Note note) throws Exception {

		Note resultDocument = searchById(String.valueOf(note.getId()));
		Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);

		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(resultDocument.getId()));

		updateRequest.doc(documentMapper);

		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

		return updateResponse.getResult().name();
	}
	
	public String deleteNote(Note noteId) throws Exception {

		System.out.println("delete elastic");
		
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(noteId));// .index(INDEX).type(TYPE);
		
		DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);

		return response.getResult().name();
	}

	private List<Note> getSearchResult(SearchResponse response) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Note> note = new ArrayList<>();

		if (searchHit.length > 0) {

			Arrays.stream(searchHit)
					.forEach(hit -> note.add(objectMapper.convertValue(hit.getSourceAsMap(), Note.class)));
		}

		return note;
	}

	public List<Note> searchByTitle(String title) throws Exception {

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery("title", "*" + title + "*"));

		searchSourceBuilder.query(queryBuilder);

		searchRequest.source(searchSourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

		return getSearchResult(response);

	}
	

	public List<Note> searchByWord(String word) throws Exception {

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("word", "*" + word + "*"));

		searchSourceBuilder.query(queryBuilder);

		searchRequest.source(searchSourceBuilder);

		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

		return getSearchResult(response);
	}
	
	public String deleteNote(int noteId) throws Exception {

		System.out.println("delete elastic");
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(noteId));// .index(INDEX).type(TYPE);
		DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);

		return response.getResult().name();
	}
	
	public List<Note> autocomplete(String prefixString) {
	     SearchRequest searchRequest = new SearchRequest(INDEX);
	     CompletionSuggestionBuilder suggestBuilder = new CompletionSuggestionBuilder("xsV"); // Note 1

	    /* suggestBuilder.size(size)
	                   .prefix(prefixString, Fuzziness.ONE) // Note 2
	                   .skipDuplicates(true)
	                   .analyzer("standard");*/
	 
	     SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); // _search
	     sourceBuilder.suggest(new SuggestBuilder().addSuggestion("sAVf", suggestBuilder));
	     searchRequest.source(sourceBuilder);

	     SearchResponse response;
	     try {
	          response = client.search(searchRequest);
	          return getSearchResult(response); // Note 3
	     } catch (IOException ex) {
	          throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in ES search");
	     }
	}
  }
