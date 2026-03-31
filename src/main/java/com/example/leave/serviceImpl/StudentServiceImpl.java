package com.example.leave.serviceImpl;

import com.example.leave.dto.LeaveRequestDTO;
import com.example.leave.model.Leave;
import com.example.leave.model.Student;
import com.example.leave.model.User;
import com.example.leave.repository.LeaveRepository;
import com.example.leave.repository.StudentRepository;
import com.example.leave.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final LeaveRepository leaveRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              LeaveRepository leaveRepository) {
        this.studentRepository = studentRepository;
        this.leaveRepository = leaveRepository;
    }

    @Override
    public LeaveRequestDTO.StudentResponse createStudent(LeaveRequestDTO.StudentRequest request,
                                                         Long userId) {
        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new IllegalArgumentException("Roll number already exists: " + request.getRollNumber());
        }
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setRollNumber(request.getRollNumber());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        if (userId != null) {
            User userRef = new User();
            userRef.setId(userId);
            student.setUser(userRef);
        }
        return toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestDTO.StudentResponse getStudentById(Long id) {
        return toResponse(getStudentEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestDTO.StudentResponse getStudentByRollNumber(String rollNumber) {
        Optional<Student> opt = studentRepository.findByRollNumber(rollNumber);
        if (!opt.isPresent()) {
            throw new NoSuchElementException("Student not found: " + rollNumber);
        }
        return toResponse(opt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestDTO.StudentResponse getStudentByUserId(Long userId) {
        Optional<Student> opt = studentRepository.findByUserId(userId);
        if (!opt.isPresent()) {
            throw new NoSuchElementException("Student not found for userId: " + userId);
        }
        return toResponse(opt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<LeaveRequestDTO.StudentResponse> result = new ArrayList<LeaveRequestDTO.StudentResponse>();
        for (Student s : students) {
            result.add(toResponse(s));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.StudentResponse> getStudentsByDepartment(String department) {
        List<Student> students = studentRepository.findByDepartment(department);
        List<LeaveRequestDTO.StudentResponse> result = new ArrayList<LeaveRequestDTO.StudentResponse>();
        for (Student s : students) {
            result.add(toResponse(s));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDTO.StudentResponse> searchStudentsByName(String name) {
        List<Student> students = studentRepository.searchByName(name);
        List<LeaveRequestDTO.StudentResponse> result = new ArrayList<LeaveRequestDTO.StudentResponse>();
        for (Student s : students) {
            result.add(toResponse(s));
        }
        return result;
    }

    @Override
    public LeaveRequestDTO.StudentResponse updateStudent(Long id,
                                                          LeaveRequestDTO.StudentRequest request) {
        Student student = getStudentEntityById(id);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        return toResponse(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.delete(getStudentEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentEntityById(Long id) {
        Optional<Student> opt = studentRepository.findById(id);
        if (!opt.isPresent()) {
            throw new NoSuchElementException("Student not found with id: " + id);
        }
        return opt.get();
    }

    private LeaveRequestDTO.StudentResponse toResponse(Student s) {
        long total    = leaveRepository.findByStudentId(s.getId()).size();
        long pending  = leaveRepository.countByStudentIdAndStatus(s.getId(), Leave.LeaveStatus.PENDING);
        long approved = leaveRepository.countByStudentIdAndStatus(s.getId(), Leave.LeaveStatus.APPROVED);

        LeaveRequestDTO.StudentResponse r = new LeaveRequestDTO.StudentResponse();
        r.setId(s.getId());
        r.setFirstName(s.getFirstName());
        r.setLastName(s.getLastName());
        r.setFullName(s.getFirstName() + " " + s.getLastName());
        r.setRollNumber(s.getRollNumber());
        r.setEmail(s.getEmail());
        r.setPhone(s.getPhone());
        r.setDepartment(s.getDepartment());
        r.setYear(s.getYear());
        r.setTotalLeaves((int) total);
        r.setPendingLeaves((int) pending);
        r.setApprovedLeaves((int) approved);
        return r;
    }
}