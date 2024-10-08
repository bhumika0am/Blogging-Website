package com.blog.api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.Exceptions.NoUsersFound;
import com.blog.api.Exceptions.ResourceNotFound;
import com.blog.api.dto.UserDto;
import com.blog.api.entities.User;
import com.blog.api.repos.UserRepo;
@Service
class UserServiceImp implements UserService{
	@Autowired
	private UserRepo ur;
	@Autowired
	private ModelMapper mapper;
	//it is done for testing purpose only
	/*
	 * @Override public UserDto findUserByEmail(String email) { User
	 * user=ur.findByEmail(email).orElseThrow(()->new
	 * ResourceNotFound("user","email",email)); return convertToDto(user); }
	 */

	@Override
	public UserDto createUser(UserDto user) {
	   return userToDto( ur.save(dtoToUser(user)));
	   
	}

	@Override
	public UserDto updateUser(UserDto user, Long userid) {
		User dbuser=ur.findById(userid).orElseThrow(()->new ResourceNotFound("User","Id",userid));
		dbuser.setName(user.getName());
		dbuser.setEmail(user.getEmail());
		dbuser.setPassword(user.getPassword());
		dbuser.setAbout(user.getAbout());
		return(userToDto(ur.save(dbuser)));
	}
	@Override
	public List<UserDto> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> users=ur.findAll();
		if(users.isEmpty()) {
			throw new NoUsersFound("There are no users to be listed");
		}
		return users.stream().map(user->userToDto(user)).collect(Collectors.toList());
	
	}

	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub
		User user=ur.findById(id).orElseThrow(()->new ResourceNotFound("User","Id",id));
		System.out.println(user);
		if(user!=null) {
			ur.delete(user);
		}
		
		
	}

	@Override
	public UserDto getUserById(Long id) {
		User user=ur.findById(id).orElseThrow(()->new ResourceNotFound("User","Id",id));
		return mapper.map(user,UserDto.class);
	}
	public User dtoToUser(UserDto u) {
		User user=this.mapper.map(u, User.class);
		return user;
	}
	public UserDto userToDto(User u) {
		UserDto user=this.mapper.map(u, UserDto.class);
		return user;
	}
}
