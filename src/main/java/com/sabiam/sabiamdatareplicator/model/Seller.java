package com.sabiam.sabiamdatareplicator.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class Seller implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("public_id")
    private UUID publicId;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("created_by")
    private String createdBy;

    @Override
    public boolean isNew() {
        boolean idIsNull = id == null;
        id = idIsNull ? UUID.randomUUID() : id;
        return idIsNull;
    }
}
