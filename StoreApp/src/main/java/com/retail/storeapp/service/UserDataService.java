package com.retail.storeapp.service;

import com.retail.storeapp.entity.User;
import com.retail.storeapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userService")
public class UserDataService {
    private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("addUser")
    public String addUser(@RequestBody User user){
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("user.tostring:"+user.toString());
            logger.error("Exception occurred while adding user object", e);
            return "Error occurred while adding User";
        }
        return "User added successfully!";
    }

    @RequestMapping("verifyUser")
    public User verifyUser(@RequestBody User user){
        try {
            User fetchedUserDetails = userRepository.findByUserName(user.getUserName());
            if(!fetchedUserDetails.getPassword().equals(user.getPassword()))
                return null;
            return fetchedUserDetails;
        }catch (Exception e){
            logger.error("user.tostring:"+user.toString());
            logger.error("Exception occurred while verifying user ", e);
        }
        return null;
    }
}
