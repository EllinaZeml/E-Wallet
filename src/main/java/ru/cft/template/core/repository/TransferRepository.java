package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.core.entity.TransferEntity;
import java.util.List;


public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    List<TransferEntity> findBySender(Long senderId);
    List<TransferEntity> findByReceiver(Long receiverId);
    List<TransferEntity> findBySenderAndReceiver(Long senderId, Long receiverId);




}
