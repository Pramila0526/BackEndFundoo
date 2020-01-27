package com.bridgelabz.fundooappbackend.elastic.service;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public Note findById(String noteId) throws Exception {

		GetRequest getRequest = new GetRequest(INDEX, TYPE, noteId);

		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		Map<String, Object> resultMap = getResponse.getSource();

		return objectMapper.convertValue(resultMap, Note.class);

	}

	public String updateNote(Note note) throws Exception {

		Note resultDocument = findById(String.valueOf(note.getId()));
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
  }
