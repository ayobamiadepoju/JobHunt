package com.example.JobPortal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table()
public class JobSeekerApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "jobPostId")
    private JobPostActivity job;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;

    private String coverLetter;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, JobSeekerProfile userId, JobPostActivity job, Date applyDate, String coverLetter) {
        Id = id;
        this.userId = userId;
        this.job = job;
        this.applyDate = applyDate;
        this.coverLetter = coverLetter;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setJobSeekerProfile(JobSeekerProfile userId) {
        this.userId = userId;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return "JobSeekerSave{" +
                "Id = " + Id +
                ", userId = " + userId +
                ", job = " + job +
                ", applyDate = " + applyDate +
                ", coverLetter = '" + coverLetter + '\'' +
                '}';
    }
}
