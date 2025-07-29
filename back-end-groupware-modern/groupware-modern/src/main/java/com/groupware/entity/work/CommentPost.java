package com.groupware.entity.work;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupware.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_comment")
public class CommentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String contentComment;

    @ManyToOne
    @JoinColumn(name = "parentCommentId")
    private CommentPost commentPost;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userIdComment")
    private User user;

    @CreationTimestamp
    private Date createdAt;
    @Transient
    private String userNameComment;

    public String getUserNameComment() {
        return user.getUsername();
    }

    public void setUserNameComment(String userNameComment) {
        this.userNameComment = user.getUsername();
    }

    @Transient
    private String positionName;

    public String getPositionName() {
        return user.getPosition().getPositionName();
    }

    public void setPositionName(String positionName) {
        this.positionName = user.getPosition().getPositionName();
    }
}
