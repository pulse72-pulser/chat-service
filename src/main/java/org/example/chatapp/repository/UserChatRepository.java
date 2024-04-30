package org.example.chatapp.repository;

import java.util.List;
import java.util.UUID;

import org.example.chatapp.models.UserChat;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRepository extends CassandraRepository<UserChat, UUID> {


    List<UserChat> findByChatId(UUID chatId);

    @Query("SELECT * FROM default_keyspace.user_chat WHERE cosine_similarity(text_embedding, :embedding) > :threshold")
    List<UserChat> findBySimilarEmbedding(@Param("embedding") List<Double> embedding, @Param("threshold") double threshold);

    @Query("SELECT * FROM default_keyspace.user_chat WHERE user_id = :userId ORDER BY cosine_similarity(text_embedding, :embedding) DESC LIMIT 1")
    UserChat findMostSimilarByUserIdAndEmbedding(@Param("userId") UUID userId, @Param("embedding") List<Double> embedding);
}