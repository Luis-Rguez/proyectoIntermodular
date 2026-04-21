package org.example.torneoajedrez.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Staff extends Usuario {

    private double salario;
    private long cuenta;
    private String rol;

    public Staff(String dni, String nombre, String apellido, int edad, String telf, String mail, String pass, long cuenta, String rol, double salario) {
        super(dni, nombre, apellido, edad, telf, mail, pass);
        this.rol = rol;
        this.cuenta = cuenta;
        this.salario = salario;
    }

    public Staff(int id, String nombre, String apellido)
    {
        super(id, nombre, apellido);
    }

    public Staff(int id, String dni, String nombre, String apellido, int edad, String telf, String mail, String pass, long cuenta, String rol, double salario) {
        super(id, dni, nombre, apellido, edad, telf, mail, pass);
        this.rol = rol;
        this.cuenta = cuenta;
        this.salario = salario;
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellido();
    }
}
