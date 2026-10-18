package com.ahmadtrportofolio.pulse.academy;

import com.ahmadtrportofolio.pulse.common.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcademyService {

    public record CourseDto(UUID id, String title, String level, int lessons, int completed, String duration) {}

    private final CourseRepository courseRepository;
    private final LessonCompletionRepository completionRepository;
    private final com.ahmadtrportofolio.pulse.portfolio.PortfolioService portfolioService;

    public AcademyService(CourseRepository courseRepository, LessonCompletionRepository completionRepository,
                          com.ahmadtrportofolio.pulse.portfolio.PortfolioService portfolioService) {
        this.courseRepository = courseRepository;
        this.completionRepository = completionRepository;
        this.portfolioService = portfolioService;
    }

    @Transactional(readOnly = true)
    public List<CourseDto> courses() {
        UUID userId = portfolioService.demoAccount().getUserId();
        return courseRepository.findAll().stream().map(course -> {
            long done = completionRepository.findByUserId(userId).stream()
                    .filter(c -> c.getCourse().getId().equals(course.getId())).count();
            return new CourseDto(course.getId(), course.getTitle(), course.getLevel(),
                    course.getLessonsCount(), (int) done, course.getDuration());
        }).toList();
    }

    @Transactional
    public void completeLesson(UUID courseId, int lessonIndex) {
        UUID userId = portfolioService.demoAccount().getUserId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        if (lessonIndex < 0 || lessonIndex >= course.getLessonsCount()) {
            throw new com.ahmadtrportofolio.pulse.common.OrderRejectedException("Invalid lesson index");
        }
        if (completionRepository.existsByUserIdAndCourseIdAndLessonIndex(userId, courseId, lessonIndex)) {
            return;
        }
        LessonCompletion completion = new LessonCompletion();
        completion.setUserId(userId);
        completion.setCourse(course);
        completion.setLessonIndex(lessonIndex);
        completion.setCompletedAt(java.time.OffsetDateTime.now());
        completionRepository.save(completion);
    }
}
