package com.ahmadtrportofolio.pulse.academy;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, UUID> {
    List<LessonCompletion> findByUserId(UUID userId);
    boolean existsByUserIdAndCourseIdAndLessonIndex(UUID userId, UUID courseId, Integer lessonIndex);
}
