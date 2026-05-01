package com.IEASmart.sistemaAsistencias.api.dto.request;

public class StudentRequest {
    private String dni;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private Long classId;

    public StudentRequest(String dni, String name, String firstLastName, String secondLastName, Long classId) {
        this.dni = dni;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.classId = classId;
    }

    public StudentRequest() {}

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
