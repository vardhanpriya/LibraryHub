package com.libraryhub.notification.repository;

import com.libraryhub.notification.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long> {
}
