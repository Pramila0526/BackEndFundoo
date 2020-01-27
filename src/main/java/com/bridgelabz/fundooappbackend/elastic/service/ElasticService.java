package com.bridgelabz.fundooappbackend.elastic.service;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundooappbackend.note.model.Note;
/*********************************************************************************************************
 * @author 	:Pramila Mangesh Tawari
 * Purpose	:Elastic Service Interface
 *
 ***********************************************************************************************************/
@Service
public interface ElasticService {

	public String createNote(Note note) throws Exception;
	public String updateNote(Note note) throws Exception;
	public Note findById(String noteId) throws Exception;
	public String deleteNote(Note noteId) throws Exception; 
   }
