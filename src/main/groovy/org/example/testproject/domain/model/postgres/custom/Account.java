package org.example.testproject.domain.model.postgres.custom;

import jakarta.persistence.*;
import lombok.*;

@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    private String id;

    @Id
    @Column(name = "id", length = 10)
    public String getId() {
        return id;
    }
}
