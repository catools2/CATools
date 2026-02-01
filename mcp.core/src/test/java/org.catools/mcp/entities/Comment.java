package org.catools.mcp.entities;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Comment {
  private String id;
  private String authorId;
  private String content;
  private boolean resolved;
  private Instant createdAt;
  private Instant updatedAt;
}
