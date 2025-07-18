package com.example.JobPortal.service;

import com.example.JobPortal.entity.JobPostActivity;
import com.example.JobPortal.entity.JobSeekerApply;
import com.example.JobPortal.entity.JobSeekerProfile;
import com.example.JobPortal.respositories.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId){
        return jobSeekerApplyRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerApply> getCandidates(JobPostActivity job){
        return jobSeekerApplyRepository.findByJob(job);
    }
}
