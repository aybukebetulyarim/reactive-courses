package com.reactive.courses;

import com.reactive.courses.entity.Course;
import com.reactive.courses.entity.Student;
import com.reactive.courses.metadata.SpringCourseMetadata;
import com.reactive.courses.repository.CourseRepository;
import com.reactive.courses.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class CoursesApplication implements CommandLineRunner {

    private final CourseRepository courseRepository;

    private final StudentRepository studentRepository;

    public CoursesApplication(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoursesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Webflux")
                .description("Spring Webflux")
                .duration(10)
                .teacher("test")
                .courseMetadata(
                        SpringCourseMetadata.builder()
                                .type("spring")
                                .language("Java")
                                .github("aaa")
                                .prerequisites(List.of("Java", "Spring"))
                                .build())
                .isUpdated(false)
                .build();

        courseRepository.save(course).block();

        Student student = Student.builder()
                .id(UUID.randomUUID())
                .name("bb")
                .email("b@b.com")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .courses(Set.of(course.getId().toString()))
                .isUpdated(false)
                .build();

        studentRepository.save(student).block();

    }

}
