package neustar.home.chat.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import neustar.home.chat.controller.MessageController;
import neustar.home.chat.controller.UserController;
import neustar.home.chat.model.CustomResponse;
import neustar.home.chat.model.Messages;
import neustar.home.chat.model.User;
import neustar.home.chat.repository.MessageReposiroty;
import neustar.home.chat.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class MessageControllerTest {
    private MockMvc mockMvc;

    private Messages messages;

    private ResponseEntity<CustomResponse> responseResponseEntity;

    @MockBean
    private MessageReposiroty messageReposiroty;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController = new UserController();

    @InjectMocks
    private MessageController messageController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        CustomResponse response = new CustomResponse();
        response.setMessage("User has been created.");
        response.setStatus("SUCCESS");
        new ResponseEntity<CustomResponse>(response, HttpStatus.CREATED);
    }

    @Test
    public void createMessageTest() throws Exception {
        Messages messages = new Messages();
       // messages.setTimestamp(LocalDateTime.now());
        messages.setMessage("HI this is for testing");
        Optional<User> user1= Optional.of(new User());
        when(messageReposiroty.save(messages)).thenReturn(messages);
        when(userRepository.findById(anyInt())).thenReturn(user1);
        mockMvc.perform(post("/message/{user_id}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(messages)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void getAllMessagesByUserIdTest() throws Exception {
        List<Messages> list1 = new ArrayList<Messages>();
        when(messageReposiroty.findByUserId(anyInt(), anyInt())).thenReturn(list1);
        mockMvc.perform(get("/messages?count=3&user_id=7")).andExpect(status().isOk());
        // .param("count","3").param("user_id","7")
    }

    // Parsing String format data into JSON format
    private static String jsonToString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
