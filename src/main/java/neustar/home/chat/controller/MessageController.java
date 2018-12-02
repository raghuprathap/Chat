package neustar.home.chat.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ExampleProperty;
import neustar.home.chat.model.CustomResponse;
import neustar.home.chat.model.Messages;
import neustar.home.chat.model.User;
import neustar.home.chat.repository.MessageReposiroty;
import neustar.home.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    private MessageReposiroty messageRepository;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "create messages posted by userId", notes = "$MessageController.createMessage")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "contents",
                    dataType = "Integer",
                    examples = @io.swagger.annotations.Example(
                            value = {
                                    @ExampleProperty(value = "{'property': 'user_id'}", mediaType = "Integer"),
                                    @ExampleProperty(value = "{'property': 'messages'}", mediaType = "application/json")
                            }))
    })
    @PostMapping(path = "/message/{user_id}")
    public ResponseEntity<CustomResponse> createMessage(@PathVariable Integer user_id, @RequestBody Messages messages) {
        messages.setUserId(user_id);
        Optional<User> user = userRepository.findById(user_id);
        user.ifPresent(user1 -> {System.out.println("testing the data "+user1.getUserName());
        messages.setUserName(user1.getUserName());
        });
        messages.setTimestamp(LocalDateTime.now());
        messageRepository.save(messages);
        CustomResponse response = new CustomResponse();
        response.setMessage("message has been created");
        response.setStatus("SUCCESS");
        return new ResponseEntity<CustomResponse>(response, HttpStatus.CREATED);
    }
//    @GetMapping("/messages")
//    public ResponseEntity<List<Messages>> getAllMessage(){
//        List<Messages> list= messageRepository.findAll();
//        return new ResponseEntity<List<Messages>>(list, HttpStatus.OK);
//    }

    @ApiOperation(value = "Get messages by userId", notes = "$MessageController.getAllMessagesByUserId")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "contents",
                    dataType = "Integer",
                    examples = @io.swagger.annotations.Example(
                            value = {
                                    @ExampleProperty(value = "{'property': 'count'}", mediaType = "Integer"),
                                    @ExampleProperty(value = "{'property': 'user_id'}", mediaType = "Integer")
                            }))
    })
    @GetMapping("/messages")
    public ResponseEntity<List<Messages>> getAllMessagesByUserId(@RequestParam(required = false) Integer count,
                                                                    @RequestParam(required = false) Integer user_id) {

        if(count == null && user_id == null) {
            List<Messages> list= messageRepository.findAll();

            return new ResponseEntity<List<Messages>>(list, HttpStatus.OK);
        } else {
            List<Messages> list = messageRepository.findByUserId(user_id, count);
            for (Messages m: list) {
                System.out.println("message data is "+m);
            }
            return new ResponseEntity<List<Messages>>(list, HttpStatus.OK);
        }

    }
}
