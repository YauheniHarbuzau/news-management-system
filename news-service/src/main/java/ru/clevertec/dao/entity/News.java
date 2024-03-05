package ru.clevertec.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сущность News
 *
 * @see Comment
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "author", nullable = false)
    private String author;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;
}
