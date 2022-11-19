package com.example.demo.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    public List<Student> getStudent(){
        return studentRepository.findAll();
//                List.of(
//                new Student("mariam1","Tom","ykn@",16)
//        );
    }

    public void saveStudent(Student student) {
        Optional<Student> studentOptional=studentRepository.findStudentsByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }


    public void deleteStudent(Long id) {
        boolean exists=studentRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException(
                    "student with Id "+id+" does not exist"
            );
        }
        studentRepository.deleteById(id);
    }
    @Transactional
    public void updateStudent(Long id, String name, String email) {
        Student student=studentRepository.findById(id).orElseThrow(
                ()->new IllegalStateException(
                        "student with id "+id+"does not exist."
                )
        );
        if(name !=null && name.length()>0 && !Objects.equals(student.getLastName(), name)){
            student.setFirstName(name);
        }
        if(email !=null && email.length()>0 && !Objects.equals(student.getEmail(), email)){
            Optional<Student>studentOptional=studentRepository.findStudentsByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }
}
