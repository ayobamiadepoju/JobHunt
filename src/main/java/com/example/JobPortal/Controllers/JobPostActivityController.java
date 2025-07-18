package com.example.JobPortal.Controllers;

import com.example.JobPortal.entity.*;
import com.example.JobPortal.service.JobPostActivityService;
import com.example.JobPortal.service.JobSeekerApplyService;
import com.example.JobPortal.service.JobSeekerSaveService;
import com.example.JobPortal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Controller
public class JobPostActivityController {

    public static Logger LOGGER = Logger.getLogger(JobPostActivityController.class.getName());
    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard/")
    public String searchForJobs(Model model, @RequestParam(value = "job", required = false) String job,
                                @RequestParam(value = "location", required = false) String location,
                                @RequestParam(value = "partTime", required = false) String partTime,
                                @RequestParam(value = "internship", required = false) String internship,
                                @RequestParam(value = "fullTime", required = false) String fullTime,
                                @RequestParam(value = "freelance", required = false) String freelance,
                                @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                                @RequestParam(value = "officeOnly", required = false) String officeOnly,
                                @RequestParam(value = "partialRemote", required = false) String partialRemote,
                                @RequestParam(value = "today", required = false) boolean today,
                                @RequestParam(value = "days7", required = false) boolean days7,
                                @RequestParam(value = "days30", required = false) boolean days30){

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("internship", Objects.equals(internship, "internship"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30){
            searchDate = LocalDate.now().minusDays(30);
        }else if (days7){
            searchDate = LocalDate.now().minusDays(7);
        }else {
            dateSearchFlag = false;
        }

        if (partTime==null && fullTime==null && freelance==null && internship==null){
            partTime = "Part-Time";
            internship = "Internship";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly==null && remoteOnly==null && partialRemote==null){
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost = jobPostActivityService.getAll();
            LOGGER.info("All Job Post: " + jobPost);
        } else {
            jobPost = jobPostActivityService.search(job, location, Arrays.asList(partTime, fullTime, freelance, internship),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
            LOGGER.info("Filtered Job Post: " + jobPost);
        }

        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobsDto> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile)currentUserProfile).getUsersAccountId());
                LOGGER.info("recruiterJobs: " + recruiterJobs);
//                System.out.println("recruiterJobs: " + recruiterJobs);
                model.addAttribute("jobPost", recruiterJobs);
            }else {
               List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService
                       .getCandidatesJobs((JobSeekerProfile) currentUserProfile);
               List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService
                       .getCandidatesJobs((JobSeekerProfile) currentUserProfile);

               boolean exist;
               boolean saved;

               for (JobPostActivity jobPostActivity : jobPost){
                   exist = false;
                   saved = false;
                   for (JobSeekerApply jobSeekerApply : jobSeekerApplyList){
                       if (Objects.equals(jobPostActivity.getJobPostId(), jobSeekerApply.getJob().getJobPostId())){
                           jobPostActivity.setIsActive(true);
                           exist = true;
                           break;
                       }
                   }
                   for (JobSeekerSave jobSeekerSave : jobSeekerSaveList){
                       if (Objects.equals(jobPostActivity.getJobPostId(), jobSeekerSave.getJob().getJobPostId())){
                           jobPostActivity.setIsActive(true);
                           exist = true;
                           break;
                       }
                   }
                   if (!exist){
                        jobPostActivity.setIsActive(false);}
                   if (!saved) {
                       jobPostActivity.setIsSaved(false);}
               }
                jobPost = jobPost.stream()
                        .filter(j -> j.getJobLocationId() != null &&
                                j.getJobLocationId().getCity() != null &&
                                j.getJobLocationId().getState() != null &&
                                j.getJobCompanyId() != null &&
                                j.getJobCompanyId().getName() != null)
                        .toList();
                model.addAttribute("jobPost", jobPost);
            }
        }
        model.addAttribute("user", currentUserProfile);
        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity,Model model){

        Users user = usersService.getCurrentUser();
        if (user!=null) {
         jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity", jobPostActivity);
        JobPostActivity savedUser = jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
    }

    @PostMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model){

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/deleteJob/{id}")
    public String deleteJob(@PathVariable("id") int id, Model model) {

        jobPostActivityService.deleteJob(id); // Pass id to the service

        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "redirect:/dashboard/";
    }

}