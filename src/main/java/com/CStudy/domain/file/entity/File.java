package com.CStudy.domain.file.entity;

import com.CStudy.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Builder
    public File(String fileName, Member member) {
        this.fileName = fileName;
    }
}
