package com.reactive.courses.service;

import com.reactive.courses.dto.CourseDto;
import com.reactive.courses.dto.StudentDto;
import com.reactive.courses.dto.StudentListDto;
import com.reactive.courses.entity.Student;
import com.reactive.courses.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final CourseService courseService;

    public StudentService(StudentRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    public Flux<Student> findAll() {
        return repository.findAll();
    }

    public Mono<StudentListDto> findAllWithCourses() {
        return repository.findAll()
                .flatMap(
                        student -> {
                            List<Mono<CourseDto>> courseDtoList = student.getCourses()
                                    .stream()
                                    .map(courseId -> courseService.findById(UUID.fromString(courseId)))
                                    .collect(Collectors.toList());

                            return Flux.combineLatest(courseDtoList, objects -> {
                                List<CourseDto> courses = Arrays.stream(objects)
                                        .map(obj -> (CourseDto) obj)
                                        .collect(Collectors.toList());

                                return new StudentDto(student.getName(), student.getEmail(), courses);
                            });
                        })
                .collectList()
                .map(StudentListDto::new);
    }
}
