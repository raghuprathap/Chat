package neustar.home.chat.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ExampleProperty;
import neustar.home.chat.model.CustomResponse;
import neustar.home.chat.model.User;
import neustar.home.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "Create a new user", notes = "$UserController.create")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "contents",
                    dataType = "User",
                    examples = @io.swagger.annotations.Example(
                            value = {
                                    @ExampleProperty(value = "{'property': 'user_name'}", mediaType = "application/json")
                            }))
    })
    @PostMapping("/user")
    public ResponseEntity<CustomResponse> create(@RequestBody final  User user) {

        userRepository.save(user);
        CustomResponse response = new CustomResponse();
        response.setMessage("User has been created.");
        response.setStatus("SUCCESS");
        return new ResponseEntity<CustomResponse>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get All users",
        notes = "$UserController.all")
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> all() {

        List<User> user = userRepository.findAll();
        return new ResponseEntity<List<User>>(user, HttpStatus.OK);

    }
}
