package com.core.drm.crypto.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_file_temp_storage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileTempStorage {

    @Id
    @GeneratedValue
    @Column(name = "file_storage_id")
    private UUID fileStorageId;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "request_id")
    private FileRequest fileRequest;

    @Column(nullable = false, name = "file_save_path")
    private String fileSavePath;

    @Column(name = "temp_save_file_name", nullable = false)
    private String tempSaveFileName;

    @Column(name = "save_time")
    private LocalDateTime saveTime;

}
