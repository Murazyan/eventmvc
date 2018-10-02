package com.example.eventmvc.repository;


import com.example.eventmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findAllByUsername(String userUame);
    List<User> findTop3ByNickname(String nickname);
    User findAllById(int id);
    List<User> findAllByUsernameContaining(String username);
    User findAllByPicUrl(String picUrl);

    List<User> findAllByNicknameContaining(String keyNickname);

//    @Query("Select c from Registration c where c.place like:place")
//    List<User> findByPlaceContaining(@Param("place")String place);

//    @Override
//    void delete(User user);
}
