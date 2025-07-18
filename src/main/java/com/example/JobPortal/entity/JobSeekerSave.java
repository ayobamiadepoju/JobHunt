package com.example.JobPortal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "job"})
})
public class JobSeekerSave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "jobPostId")
    private JobPostActivity job;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;

    public JobSeekerSave() {
    }

    public JobSeekerSave(Integer id, JobPostActivity job, JobSeekerProfile userId) {
        Id = id;
        this.job = job;
        this.userId = userId;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(JobSeekerProfile userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JobSeekerSave{" +
                "Id=" + Id +
                ", job=" + job +
                ", userId=" + userId.toString() +
                '}';
    }
}