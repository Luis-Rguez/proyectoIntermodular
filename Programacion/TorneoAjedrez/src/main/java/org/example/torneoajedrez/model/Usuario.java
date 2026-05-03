package org.example.torneoajedrez.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.ls.LSOutput;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String telf;
    private String pass;
    private int edad;

    public Usuario(String dni, String nombre, String apellido, int edad, String telf, String mail, String pass) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.telf = telf;
        this.mail = mail;
        this.pass = pass;
    }

    public Usuario(int id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Usuario(String nombre, String dni, String telf, String mail, String pass)
    {
        this.dni = dni;
        this.nombre = nombre;
        this.telf = telf;
        this.mail = mail;
        this.pass = pass;
    }

    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Usuario(int id, String dni, String nombre, String telf, String mail, String pass) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.telf = telf;
        this.mail = mail;
        this.pass = pass;
    }

    public Usuario(int id, String dni, String nombre, String apellido, int edad, String telf, String mail, String pass) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.telf = telf;
        this.mail = mail;
        this.pass = pass;
    }

    @Override
    public String toString() {
        return id + "\t" + nombre + " " + apellido;
    }
}
