package com.bridgelabz.fundooappbackend;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.bridgelabz.fundooappbackend.note.controller.NoteController;
import com.bridgelabz.fundooappbackend.note.dto.NoteDto;
import com.bridgelabz.fundooappbackend.note.model.Note;
import com.bridgelabz.fundooappbackend.user.response.Response;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FundooAppBackendApplicationTests {

	/*private MockMvc mockMvc;
	
	//NoteController nc=null;
		//@Mock
    //NoteService notesServiceImplementation;
	
	@Autowired
	private WebApplicationContext context;
	
	ObjectMapper mapper = new ObjectMapper();
	/*@Test
	void contextLoads() {
	}
	
	@BeforeEach
	public void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		//nc = new NoteController(notesServiceImplementation);
	}
	
	@Test
	public void addNoteTest() throws Exception
	{
	Note note = new Note();
	note.setTitle("Note One");
	note.setDescription("Note Desc");
	note.setArchieve(true);
	note.setPin(true);
	note.setTrash(true);
	
	String jsonRequest = mapper.writeValueAsString(note);
	
	
	MvcResult result=mockMvc.perform(post("note/addnote").content(jsonRequest).
				content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	
	String resultContent = result.getResponse().getContentAsString();
	
	Response response = mapper.readValue(resultContent,Response.class);
	
	//assertEquals(true,response.getStatus());*/
	//Assertions.assertEquals(true,response.getStatus());
//	}

/*
 * @Test public void createNoteTest() throws Exception { Note note = new Note();
 * note.setTitle("Note One"); note.setDescription("Note Desc");
 * note.setArchieve(true); note.setPin(true); note.setTrash(true);
 * 
 * nc.addNote(note);
 * 
 * ((NoteService) verify(note, times(1))).addNote(note); }
 */

/*
 * @Test public void addNoteTest() throws Exception { Note note = new Note();
 * note.setTitle("Note One"); note.setDescription("Note Desc");
 * note.setArchieve(true); note.setPin(true); note.setTrash(true);
 * 
 * String jsonRequest = mapper.writeValueAsString(note);
 * 
 * 
 * MvcResult result=mockMvc.perform(post("note/addnote").content(jsonRequest).
 * content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).
 * andReturn();
 * 
 * String resultContent = result.getResponse().getContentAsString();
 * 
 * Response response = mapper.readValue(resultContent,Response.class);
 * //assertEquals(true,response.getStatus());
 * Assertions.assertEquals(true,response.getStatus()); }
 */
	
	
	@InjectMocks
    NoteController nController;
     
    @Mock
    NoteDto noteDto;
     
    @Test
    public void testAddEmployee() throws Exception 
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
         
        //when(employeeDAO.addEmployee(any(Note.class))).thenReturn(true);
         
        Note employee = new Note( 0, "Lokesh", "Gupta", null, null, null, null, false, false, false);
        Response responseEntity = nController.addNote(employee);
        assertEquals(true,responseEntity.getStatus());
       // assertEquals(true,responseEntity.getStatus()).isEqualTo(200);
    //    assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");
    }

}