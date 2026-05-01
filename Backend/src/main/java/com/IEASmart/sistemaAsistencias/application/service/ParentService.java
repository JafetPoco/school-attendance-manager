package com.IEASmart.sistemaAsistencias.application.service;

import com.IEASmart.sistemaAsistencias.api.dto.request.ParentWithChildRequest;
import com.IEASmart.sistemaAsistencias.api.dto.request.StudentRequest;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.ParentWithChildResponse;
import com.IEASmart.sistemaAsistencias.api.dto.response.StudentResponse;
import com.IEASmart.sistemaAsistencias.api.mapper.ParentApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.ParentWithChildApiMapper;
import com.IEASmart.sistemaAsistencias.api.mapper.StudentApiMapper;
import com.IEASmart.sistemaAsistencias.domain.exception.InvalidArgumentException;
import com.IEASmart.sistemaAsistencias.domain.exception.ConflictException;
import com.IEASmart.sistemaAsistencias.domain.exception.ResourceNotFoundException;
import com.IEASmart.sistemaAsistencias.domain.model.Class;
import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Grade;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.Level;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.School;
import com.IEASmart.sistemaAsistencias.domain.repository.ClassRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.ParentRepository;
import com.IEASmart.sistemaAsistencias.domain.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ParentService {
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ParentApiMapper parentApiMapper;
    private final StudentApiMapper studentApiMapper;
    private final ParentWithChildApiMapper parentWithChildApiMapper;
    private final ClassRepository classRepository;

    public ParentService(ParentRepository parentRepository,
                         StudentRepository studentRepository,
                         ParentApiMapper parentApiMapper,
                         StudentApiMapper studentApiMapper,
                         ParentWithChildApiMapper parentWithChildApiMapper,
                         ClassRepository classRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.parentApiMapper = parentApiMapper;
        this.studentApiMapper = studentApiMapper;
        this.parentWithChildApiMapper = parentWithChildApiMapper;
        this.classRepository = classRepository;
    }

    public StudentResponse addChildToParent(Long parentId, StudentRequest student, School school) {
        if(student == null) throw new IllegalArgumentException("Student cannot be null");

        if (student.getDni() == null || student.getDni().isBlank()) {
            throw new IllegalArgumentException("Student DNI is required and must not be empty when creating a student");
        }

        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();

            Student domainStudent = studentApiMapper.toDomain(student);
            domainStudent.getClassSchool().setSchool(school);

            parent.addChild(domainStudent);
            Parent savedParent = parentRepository.save(parent);

            String studentDni = student.getDni();

            // Buscar el estudiante recién guardado dentro del padre guardado por su dni
            Optional<Student> savedStudentOpt = savedParent.getChildren().stream()
                    .filter(s -> s.getDni().equals(studentDni))
                    .findFirst();

            StudentResponse studentResponse;
            if (savedStudentOpt.isPresent()) {
                studentResponse = studentApiMapper.toResponse(savedStudentOpt.get());
            } else {
                studentResponse = studentApiMapper.toResponse(domainStudent);
            }
            return studentResponse;
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");
    }

    public ParentResponse addParentWithChildren(ParentWithChildRequest request, School school) {
        if(request == null) throw new InvalidArgumentException("Request cannot be null", "request");

        if (request.getChildren() != null) {
            for (StudentRequest s : request.getChildren()) {
                if (s == null) continue;
                if (s.getDni() == null || s.getDni().isBlank()) {
                    throw new InvalidArgumentException("Each student must have a non-empty DNI to be persisted", "children");
                }

                if (studentRepository != null && s.getDni() != null) {
                    if (studentRepository.findById(s.getDni(), school).isPresent()) {
                        throw new ConflictException("El estudiante con DNI " + s.getDni() + " ya existe en la escuela", "STUDENT_DNI_ALREADY_EXISTS");
                    }
                }
            }
        }

        Parent parent = parentWithChildApiMapper.toDomain(request);
        parent.setSchool(school);
        for(Student child : parent.getChildren()) {
            child.getClassSchool().setSchool(school);

            if(child.getClassSchool().getSchool() == null) {
                throw new IllegalStateException("Child debe tener una escuela asignada antes de ser persistido");
            }
        }

        Parent savedParent = parentRepository.save(parent);
        return parentApiMapper.toResponse(savedParent);
    }

    public List<StudentResponse> addChildrenToParent(Long parentId, List<StudentRequest> students, School school) {
        if (students == null) throw new InvalidArgumentException("Students list cannot be null", "students");

        for (StudentRequest s : students) {
            if (s == null) continue;
            if (s.getDni() == null || s.getDni().isBlank()) {
                throw new InvalidArgumentException("Each student must have a non-empty DNI to be persisted", "students");
            }
        }

        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if(parentOpt.isPresent()) {
            Parent parent = parentOpt.get();

            for(Student child : parent.getChildren()) {
                child.getClassSchool().setSchool(school);
            }

            List<Student> studentEntities = students.stream()
                    .map(studentApiMapper::toDomain)
                    .toList();
            studentEntities.forEach(parent::addChild);
            Parent saved = parentRepository.save(parent);
            return saved.getChildren().stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
        }
        throw new InvalidArgumentException("Parent with id " + parentId + " not found", "parentId");
    }

    @Transactional
    public void importFromExcel(MultipartFile file, School school) {
        if (file == null || file.isEmpty()) {
            throw new ConflictException("Archivo no especificado", "NO_FILE_PROVIDED");
        }

        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        if (!filename.endsWith(".xlsx") && !filename.endsWith(".xls")) {
            throw new ConflictException(
                    "Archivo no soportado. Solo se permiten archivos .xlsx o .xls",
                    "UNSUPPORTED_FILE_TYPE");
        }

        Map<String, Parent> parentsCache = new HashMap<>(); // phone -> Parent
        Set<String> seenDnis = new HashSet<>();
        List<String> importErrors = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return;

            DataFormatter formatter = new DataFormatter();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String dni = formatter.formatCellValue(row.getCell(0)).trim();
                if (dni.isEmpty()) {
                    continue;
                }

                if (seenDnis.contains(dni)) {
                    importErrors.add("Fila " + (r+1) + ": DNI duplicado en el archivo -> " + dni);
                    continue;
                }

                String levelRaw = formatter.formatCellValue(row.getCell(1)).trim();
                String gradeRaw = formatter.formatCellValue(row.getCell(2)).trim();
                String sectionRaw = formatter.formatCellValue(row.getCell(3)).trim();

                String firstLast = formatter.formatCellValue(row.getCell(4)).trim();
                String secondLast = formatter.formatCellValue(row.getCell(5)).trim();
                String name = formatter.formatCellValue(row.getCell(6)).trim();

                String parentName = formatter.formatCellValue(row.getCell(7)).trim();
                String parentPhone = formatter.formatCellValue(row.getCell(8)).trim();

                Level level = null;
                Grade grade = null;
                try {
                    if (!levelRaw.isBlank()) level = Level.from(levelRaw);
                } catch (Exception ex) {
                    importErrors.add("Fila " + (r+1) + ": level inválido -> '" + levelRaw + "'");
                    continue;
                }
                try {
                    if (!gradeRaw.isBlank()) grade = Grade.from(gradeRaw);
                } catch (Exception ex) {
                    importErrors.add("Fila " + (r+1) + ": grade inválido -> '" + gradeRaw + "'");
                    continue;
                }

                if (studentRepository.findById(dni, school).isPresent()) {
                    importErrors.add("Fila " + (r+1) + ": el estudiante con DNI " + dni + " ya existe");
                    continue;
                }

                seenDnis.add(dni);

                Optional<Class> classOpt = classRepository.findByClassInformation(sectionRaw, grade, level, school);
                if (classOpt.isEmpty()) {
                    importErrors.add("Fila " + (r+1) + ": no se encontró una clase que coincida con nivel='" + levelRaw + "', grado='" + gradeRaw + "', seccion='" + sectionRaw + "'");
                    continue;
                }

                Student student = new Student(dni, name, firstLast, secondLast, classOpt.get());

                if (parentPhone.isBlank()){
                    importErrors.add("Fila " + (r+1) + ": el padre no tiene número de teléfono");
                    continue;
                }

                Parent parent = parentsCache.get(parentPhone);
                if (parent == null) {
                    Optional<Parent> existing = parentRepository.findByPhoneNumber(parentPhone, school);
                    parent = existing.orElseGet(() -> {
                        Parent p = new Parent();
                        p.setNames(parentName);
                        p.setPhoneNumber(parentPhone);
                        p.setSchool(school);
                        return p;
                    });
                    parentsCache.put(parentPhone, parent);
                }
                parent.addChild(student);
            }

            List<Parent> listParents = new ArrayList<>(parentsCache.values());
            parentRepository.saveAll(listParents);

            if (!importErrors.isEmpty()) {
                throw new ConflictException("Errores en import: " + String.join("; ", importErrors), "EXCEL_VALIDATION_ERROR");
            }
        } catch (ConflictException ce) {
            throw ce;
        } catch (Exception e) {
            throw new ConflictException("Error al procesar el archivo Excel: " + e.getMessage(), "EXCEL_PROCESSING_ERROR");
        }
    }

    @Transactional(readOnly = true)
    public List<ParentResponse> getAllParents(School school) {
        List<Parent> parents = parentRepository.getAll(school);
        return parents.stream()
                .map(parentApiMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ParentWithChildResponse> getParentById(Long parentId, School school) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if(parentOpt.isEmpty()){
            return Optional.empty();
        } else {
            Parent parent = parentOpt.get();
            ParentWithChildResponse response = parentWithChildApiMapper.toResponse(parent);
            return Optional.of(response);
        }
    }

    public List<StudentResponse> getChildrenOfParent(Long parentId, School school) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId, school);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();
            return parent.getChildren().stream()
                    .map(studentApiMapper::toResponse)
                    .toList();
        }
        throw new IllegalArgumentException("Parent with id " + parentId + " not found");
    }

    @Transactional
    public void removeChildByDni(String studentDni, School school) {
        if (studentDni == null || studentDni.isBlank()){
            throw new InvalidArgumentException("El DNI del estudiante es requerido para ser eliminado", "studentDni");
        }

        Optional<Parent> parentOpt = parentRepository.findByAlumnoId(studentDni);
        if (parentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Padre de familia", "el DNI del estudiante", studentDni);
        }

        Parent parent = parentOpt.get();

        if (parent.getSchool() != null && school != null && !parent.getSchool().equals(school)) {
            throw new ConflictException("El padre de familia encontrado no pertenece a la escuela del usuario autenticado", "SCHOOL_MISMATCH");
        }

        List<Student> children = parent.getChildren();
        boolean removed = children.removeIf(s -> studentDni.equals(s.getDni()));

        if (!removed) throw new ConflictException("El estudiante con DNI " + studentDni + " no es hijo del padre de familia encontrado", "STUDENT_NOT_CHILD_OF_PARENT");

        if (children.isEmpty()) {
            parentRepository.delete(parent);
        } else {
            parentRepository.save(parent);
        }
    }
}
