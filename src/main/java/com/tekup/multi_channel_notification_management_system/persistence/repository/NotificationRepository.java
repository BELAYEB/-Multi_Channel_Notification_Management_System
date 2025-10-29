package com.tekup.multi_channel_notification_management_system.persistence.repository;

import com.tekup.multi_channel_notification_management_system.common.enums.Channel;
import com.tekup.multi_channel_notification_management_system.common.enums.NotificationStatus;
import com.tekup.multi_channel_notification_management_system.common.enums.Priority;
import com.tekup.multi_channel_notification_management_system.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByStatus(NotificationStatus status);

    List<NotificationEntity> findByChannel(Channel channel);

    List<NotificationEntity> findByPriority(Priority priority);

    List<NotificationEntity> findByRecipient(String recipient);

    @Query("SELECT n FROM NotificationEntity n WHERE n.status = :status " +
            "AND n.priority = :priority ORDER BY n.createdAt ASC")
    List<NotificationEntity> findByStatusAndPriority(
            @Param("status") NotificationStatus status,
            @Param("priority") Priority priority);

    @Query("SELECT n FROM NotificationEntity n WHERE n.status = 'FAILED' " +
            "AND n.retryCount < :maxRetries")
    List<NotificationEntity> findFailedNotificationsForRetry(
            @Param("maxRetries") int maxRetries);

    @Query("SELECT n FROM NotificationEntity n WHERE n.createdAt BETWEEN :start AND :end")
    List<NotificationEntity> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    long countByStatus(NotificationStatus status);

    long countByChannel(Channel channel);
}
