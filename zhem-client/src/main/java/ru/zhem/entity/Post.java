package ru.zhem.entity;

import lombok.*;


@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    private Long id;

    private String content;

    private ZhemFile image;

}
