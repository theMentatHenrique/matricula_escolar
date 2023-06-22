package com.msdisciplinas.msdisciplinas.ENUM;

import lombok.ToString;

public enum HorarioEnum {

    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G");

    private String valor;

    HorarioEnum(String i) {
        valor = i;
    }

    @Override
    public String toString() {
        return valor;
    }
}
