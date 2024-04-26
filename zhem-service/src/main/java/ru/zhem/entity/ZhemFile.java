package ru.zhem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema = "zhem", name = "files")
public class ZhemFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Size(max = 256)
    @Column(name = "path")
    private String path;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private FileType type;

}
