package com.example.tacocloudapplication.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Drink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();

    @NotNull
    private String name;

    @NotNull
    private float liters;

    @NotNull
    private float price = 0;

    @NotNull
    private String associatedPhotoName;

    @JsonIgnore
    @ManyToMany(mappedBy = "drinks", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TacoOrder> tacoOrders = new ArrayList<>();
}
