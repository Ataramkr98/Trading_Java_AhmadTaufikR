package com.ahmadtrportofolio.pulse.academy;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/academy")
public class AcademyController {

    private final AcademyService academyService;

    public AcademyController(AcademyService academyService) {
        this.academyService = academyService;
    }

    @GetMapping("/courses")
    public List<AcademyService.CourseDto> courses() {
        return academyService.courses();
    }

    @PostMapping("/lessons/{courseId}/complete")
    public Map<String, Object> complete(@PathVariable UUID courseId, @RequestBody(required = false) Map<String, Integer> body) {
        int lessonIndex = body == null || body.get("lessonIndex") == null ? 0 : body.get("lessonIndex");
        academyService.completeLesson(courseId, lessonIndex);
        return Map.of("ok", true);
    }
}
