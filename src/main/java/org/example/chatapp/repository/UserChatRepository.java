package org.example.chatapp.repository;


import java.util.UUID;

import org.example.chatapp.models.UserChat;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRepository extends MongoRepository<UserChat, UUID> {

}