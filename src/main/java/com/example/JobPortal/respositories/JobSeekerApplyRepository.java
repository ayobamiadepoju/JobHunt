package com.example.JobPortal.respositories;

import com.example.JobPortal.entity.JobPostActivity;
import com.example.JobPortal.entity.JobSeekerApply;
import com.example.JobPortal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}
