package com.example.tacocloudapplication.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotNull
    private float totalTacoPrice = 0;

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany(mappedBy = "tacos", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Ingredient> ingredients = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "taco_order_id")
    private TacoOrder tacoOrder;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;
}
