package com.site.andrewsfood.Model.domain;

import javax.persistence.*;

@Entity
@Table(name = "miscellaneous")
public class Miscellaneous {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String controlSequence;

    public Miscellaneous() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getControlSequence() {
        return controlSequence;
    }

    public void setControlSequence(String controlSequence) {
        this.controlSequence = controlSequence;
    }
}
