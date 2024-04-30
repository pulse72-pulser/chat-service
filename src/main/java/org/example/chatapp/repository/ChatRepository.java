package org.example.chatapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.chatapp.models.Chat;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CassandraRepository<Chat, UUID> {
    List<Chat> findByUserId(UUID userId);

    Optional<Chat> findByChatIdAndUserId(UUID chatId, UUID userId);

    Optional<Chat> findByChatId(UUID chatId);
}