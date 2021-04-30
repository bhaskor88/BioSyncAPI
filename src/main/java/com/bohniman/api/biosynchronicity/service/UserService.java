package com.bohniman.api.biosynchronicity.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bohniman.api.biosynchronicity.model.MasterRole;
import com.bohniman.api.biosynchronicity.model.MasterUser;
import com.bohniman.api.biosynchronicity.model.TransFamilyMember;
import com.bohniman.api.biosynchronicity.payload.request.OtpVerify;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.repository.MasterRoleRespository;
import com.bohniman.api.biosynchronicity.repository.MasterUserRepository;
import com.bohniman.api.biosynchronicity.repository.TransFamilyMemberRepository;
import com.bohniman.api.biosynchronicity.util.OtpUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    MasterUserRepository masterUserRepository;

    @Autowired
    MasterRoleRespository masterRoleRepository;

    @Autowired
    TransFamilyMemberRepository familyMemberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public JsonResponse registerUser(MasterUser user) {
        JsonResponse res = new JsonResponse();

        user.setAccountNotExpired(true);
        user.setEnable(false);
        user.setAccountNotLocked(true);
        user.setCredentialsNotExpired(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<MasterRole> roles = masterRoleRepository.findAllByRoleName("USER");
        user.setRoles(roles);

        MasterUser createdUser = masterUserRepository.save(user);

        if (createdUser == null) {
            res.setMessage("Some error ocurred while saving user");
            res.setResult(false);
            return res;
        }

        // Creating a new Family Member
        TransFamilyMember member = new TransFamilyMember();
        member.setAge(createdUser.getAge());
        member.setBloodGroup(createdUser.getBloodGroup());
        member.setEmail(createdUser.getEmail());
        member.setGender(createdUser.getGender());
        member.setName(createdUser.getName());
        member.setMobileNumber(createdUser.getMobileNumber());
        member.setMasterUser(createdUser);
        member.setPrimary(true);

        TransFamilyMember newMember = saveFamilyMember(member);

        if (newMember == null) {
            masterUserRepository.delete(createdUser);
            res.setMessage("Some error ocurred while saving Family Member");
            res.setResult(false);
            return res;
        }

        // All Suceess ! Fire OTP

        String otp = OtpUtil.getSixDigitOtp();
        if (OtpUtil.fireOtp(createdUser.getMobileNumber(), otp)) {
            createdUser.setOtp(otp);
            createdUser = masterUserRepository.save(createdUser);

            // Send Success Response
            createdUser.setPassword("");
            createdUser.setFamilyMembers(null);
            res.setPayload(createdUser);
            res.setMessage("User registration successful !");
            res.setResult(true);
            return res;
        } else {
            familyMemberRepository.delete(newMember);
            masterUserRepository.delete(createdUser);
            res.setMessage("Some error ocurred while Firing OTP");
            res.setResult(false);
            return res;
        }
    }

    public TransFamilyMember saveFamilyMember(TransFamilyMember member) {
        return familyMemberRepository.save(member);
    }

    public JsonResponse addFamilyMember(TransFamilyMember member, Long userId) {
        JsonResponse res = new JsonResponse();
        Optional<MasterUser> userOpt = masterUserRepository.findById(userId);
        if (userOpt.isPresent()) {
            member.setMasterUser(userOpt.get());
            member.setPrimary(false);
            TransFamilyMember mem = saveFamilyMember(member);
            if (mem == null) {
                res.setMessage("Error saving Family Member!");
                res.setResult(false);
            } else {
                res.setResult(true);
                res.setMessage("Family Member saved successfully!");
                res.setPayload(mem);
            }
            return res;
        } else {
            res.setMessage("User with id - " + userId + " not found !");
            res.setResult(false);
            return res;
        }
    }

    public JsonResponse getFamilyMembers(Long userId) {
        JsonResponse res = new JsonResponse();
        List<TransFamilyMember> memberList = familyMemberRepository.findAllByMasterUser_userId(userId);
        if (memberList == null || memberList.size() == 0) {
            res.setResult(false);
            res.setMessage("No Family Member Found !");
        } else {
            for (TransFamilyMember member : memberList) {
                member.setMasterUser(null);
            }
            res.setResult(true);
            res.setPayload(memberList);
            res.setMessage("Family Member(s) List fetched successfully !");
        }
        return res;
    }

    public JsonResponse deleteFamilyMembers(Long familyMemberId, Long userId) {
        JsonResponse res = new JsonResponse();
        try {
            TransFamilyMember mem = familyMemberRepository.findByIdAndMasterUser_userId(familyMemberId, userId);

            if (mem != null && !mem.isPrimary()) {
                familyMemberRepository.delete(mem);
                res.setResult(true);
                res.setMessage("Family Member Deleted Successfully !");
            } else if (mem.isPrimary()) {
                res.setResult(false);
                res.setMessage("Primary Family Member cannot be deleted !");
            } else {
                res.setResult(false);
                res.setMessage("Family Member not found for the logged in user !");
            }
        } catch (Exception e) {
            res.setMessage("Some Error has ocurred");
            res.setResult(false);
        }
        return res;
    }

    public JsonResponse verifyOtp(OtpVerify otpDetails) {
        JsonResponse res = new JsonResponse();

        Optional<MasterUser> optUser = masterUserRepository.findByUserIdAndOtpAndMobileNumber(otpDetails.getUserId(),
                otpDetails.getOtp(), otpDetails.getMobileNumber());

        if (optUser.isPresent()) {

            MasterUser user = optUser.get();
            user.setEnable(true);
            user = masterUserRepository.save(user);

            if (user == null) {
                res.setMessage("Some Error has ocurred");
                res.setResult(false);
            } else {
                res.setResult(true);
                res.setMessage("OTP verified Successfully !");
            }

        } else {
            res.setMessage("Invalid UserId or OTP or MobileNumber");
            res.setResult(false);
        }

        return res;
    }

    public JsonResponse updateFamilyMember(TransFamilyMember member, Long userId) {
        JsonResponse res = new JsonResponse();
        if (familyMemberRepository.findById(member.getId()).isPresent()) {
            member = familyMemberRepository.save(member);
            if (member == null) {
                res.setMessage("Some Error has ocurred");
                res.setResult(false);
            } else {
                res.setResult(true);
                res.setMessage("Family Member Updated Successfully !");
            }
        } else {
            res.setMessage("Invalid Family Member Id Received");
            res.setResult(false);
        }
        return res;
    }
}
