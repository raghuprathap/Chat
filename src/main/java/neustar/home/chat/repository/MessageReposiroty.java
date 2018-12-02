package neustar.home.chat.repository;

import neustar.home.chat.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageReposiroty extends JpaRepository<Messages, Integer> {
    @Query(value = "SELECT * from messages where user_id= :user_id order by timestamp DESC limit :count",nativeQuery = true)
    public List<Messages> findByUserId( @Param(value = "user_id") Integer user_id,
                                            @Param(value = "count") Integer count);



}
