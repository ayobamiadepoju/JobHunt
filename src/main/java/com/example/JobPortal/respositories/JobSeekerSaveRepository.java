package com.example.JobPortal.respositories;

import com.example.JobPortal.entity.JobPostActivity;
import com.example.JobPortal.entity.JobSeekerProfile;
import com.example.JobPortal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);

    List<JobSeekerSave> findByJob(JobPostActivity job);
}
