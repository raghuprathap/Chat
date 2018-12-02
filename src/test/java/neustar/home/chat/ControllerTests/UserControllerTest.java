package neustar.home.chat.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import neustar.home.chat.controller.UserController;
import neustar.home.chat.model.CustomResponse;
import neustar.home.chat.model.User;
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
import org.springframework.test.web.servlet.MockMvcBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    private User user;

    private ResponseEntity<CustomResponse> responseResponseEntity;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController = new UserController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CustomResponse response = new CustomResponse();
        response.setMessage("User has been created.");
        response.setStatus("SUCCESS");
        new ResponseEntity<CustomResponse>(response, HttpStatus.CREATED);
    }

    @Test
    public void createTest() throws Exception {
        user = new User(0, "Raghu");

        when(userRepository.save(user)).thenReturn(user);
        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(jsonToString(user)))
                .andExpect(status().isCreated()).andDo(print());


    }

    @Test
    public void allTest() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isOk());
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
