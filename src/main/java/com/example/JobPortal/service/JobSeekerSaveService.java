package com.example.JobPortal.service;

import com.example.JobPortal.entity.JobPostActivity;
import com.example.JobPortal.entity.JobSeekerProfile;
import com.example.JobPortal.entity.JobSeekerSave;
import com.example.JobPortal.respositories.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {

    private final JobSeekerSaveRepository jobSeekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidatesJobs(JobSeekerProfile userAccountId){
        return jobSeekerSaveRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerSave> getCandidates(JobPostActivity job){
        return jobSeekerSaveRepository.findByJob(job);
    }
}
