package com.IEASmart.sistemaAsistencias.domain.model;

public class Student {
    private String dni;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private Class classSchool;

    public Student(String dni, String name, String firstLastName, String secondLastName, Class classSchool) {
        this.dni = dni;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.classSchool = classSchool;
    }

    public Student() {}

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

    public Class getClassSchool() {
        return classSchool;
    }

    public void setClassSchool(Class classSchool) {
        this.classSchool = classSchool;
    }
}