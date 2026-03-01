package org.catools.mcp.entities;

import java.time.Instant;
import lombok.*;

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
