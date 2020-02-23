package com.github.test.mongodb.service;

import com.github.test.mongodb.common.page.PageQuery;
import com.github.test.mongodb.common.page.PageResult;
import com.github.test.mongodb.dto.UserDTO;
import com.github.test.mongodb.model.User;

import java.util.List;

public interface UserService {

  UserDTO saveUser(UserDTO userDTO);

  boolean delUserById(Long id);

  List<User> listUsers(UserDTO userDTO);

  PageResult<User> pageUsers(PageQuery<UserDTO> pageQuery);

}
