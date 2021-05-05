package com.bohniman.api.biosynchronicity.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import com.bohniman.api.biosynchronicity.model.MasterOtp;
import com.bohniman.api.biosynchronicity.model.MasterRole;
import com.bohniman.api.biosynchronicity.model.MasterUser;
import com.bohniman.api.biosynchronicity.model.TransFamilyMember;
import com.bohniman.api.biosynchronicity.payload.request.AddressRequest;
import com.bohniman.api.biosynchronicity.payload.request.AddtionalRequest;
import com.bohniman.api.biosynchronicity.payload.request.OtpVerify;
import com.bohniman.api.biosynchronicity.payload.request.PasswordRequest;
import com.bohniman.api.biosynchronicity.payload.request.ProfileRequest;
import com.bohniman.api.biosynchronicity.payload.request.RegisterRequest;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.repository.MasterOtpRepository;
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
    MasterOtpRepository masterOtpRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // public JsonResponse registerUser(MasterUser user) {
    // JsonResponse res = new JsonResponse();

    // user.setAccountNotExpired(true);
    // user.setEnable(false);
    // user.setAccountNotLocked(true);
    // user.setCredentialsNotExpired(true);
    // user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Set<MasterRole> roles = masterRoleRepository.findAllByRoleName("USER");
    // user.setRoles(roles);

    // MasterUser createdUser = masterUserRepository.save(user);

    // if (createdUser == null) {
    // res.setMessage("Some error ocurred while saving user");
    // res.setResult(false);
    // return res;
    // }

    // // Creating a new Family Member
    // TransFamilyMember member = new TransFamilyMember();
    // member.setAge(createdUser.getAge());
    // member.setBloodGroup(createdUser.getBloodGroup());
    // member.setEmail(createdUser.getEmail());
    // member.setGender(createdUser.getGender());
    // member.setName(createdUser.getName());
    // member.setMobileNumber(createdUser.getMobileNumber());
    // member.setMasterUser(createdUser);
    // member.setPrimary(true);

    // TransFamilyMember newMember = saveFamilyMember(member);

    // if (newMember == null) {
    // masterUserRepository.delete(createdUser);
    // res.setMessage("Some error ocurred while saving Family Member");
    // res.setResult(false);
    // return res;
    // }

    // // All Suceess ! Fire OTP

    // String otp = OtpUtil.getSixDigitOtp();
    // if (OtpUtil.fireOtp(createdUser.getMobileNumber(), otp)) {
    // createdUser.setOtp(otp);
    // createdUser = masterUserRepository.save(createdUser);

    // // Send Success Response
    // createdUser.setPassword("");
    // createdUser.setFamilyMembers(null);
    // res.setPayload(createdUser);
    // res.setMessage("User registration successful !");
    // res.setResult(true);
    // return res;
    // } else {
    // familyMemberRepository.delete(newMember);
    // masterUserRepository.delete(createdUser);
    // res.setMessage("Some error ocurred while Firing OTP");
    // res.setResult(false);
    // return res;
    // }
    // }

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

    public JsonResponse verifyOtp(RegisterRequest request) {
        JsonResponse res = new JsonResponse();
        Optional<MasterOtp> mstrOtp = masterOtpRepository.findByEmail(request.getEmail());
        if (mstrOtp.isPresent()) {
            MasterOtp masterOtp = mstrOtp.get();
            if (masterOtp.getOtp().equals(request.getOtp())) {
                masterOtp.setToken(OtpUtil.getRandomToken(16));
                masterOtp = masterOtpRepository.save(masterOtp);

                if (masterOtp != null) {

                    MasterUser user = null;
                    // Check if the user with the same email exist
                    Optional<MasterUser> usrOpt = masterUserRepository.findByEmail(request.getEmail());
                    if (usrOpt.isPresent()) {
                        // Select the existing user for updation
                        user = usrOpt.get();
                    } else {
                        // Create a new disabled user
                        user = new MasterUser();
                    }

                    user.setUsername(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getOtp()));
                    user.setMobileNumber("0000000000");
                    user.setEmail(request.getEmail());

                    user.setAccountNotExpired(true);
                    user.setEnable(false);
                    user.setAccountNotLocked(true);
                    user.setCredentialsNotExpired(true);

                    user = masterUserRepository.save(user);

                    if (user != null) {
                        user.setPassword(masterOtp.getToken());
                        res.setPayload(user);
                        res.setResult(true);
                        res.setMessage("OTP verified Successfully !");
                    } else {
                        res.setMessage("Error Creating User Account");
                        res.setResult(false);
                    }

                } else {
                    res.setMessage("Some error has ocurred ! ErrCode : 101");
                    res.setResult(false);
                }

            } else {
                res.setMessage("Invalid OTP Provided");
                res.setResult(false);
            }
        } else {
            res.setMessage("Invalid Email or OTP Provided");
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

    public JsonResponse fireOtp(RegisterRequest registerUser) {
        // TODO: Wait 2 Minutes before firing new OTP
        JsonResponse res = new JsonResponse();
        String otp = OtpUtil.getSixDigitOtp();
        if (OtpUtil.fireOtpEmail(registerUser.getEmail(), otp)) {
            MasterOtp mstrOtp = null;
            Optional<MasterOtp> masterOtp = masterOtpRepository.findByEmail(registerUser.getEmail());
            if (masterOtp.isPresent()) {
                mstrOtp = masterOtp.get();
                mstrOtp.setOtp(otp);
                mstrOtp = masterOtpRepository.save(mstrOtp);
            } else {
                mstrOtp = new MasterOtp(registerUser.getEmail(), otp);
                mstrOtp = masterOtpRepository.save(mstrOtp);
            }
            if (mstrOtp != null) {
                res.setMessage("Otp Fired to email - " + registerUser.getEmail() + " successfully ! " + otp);
                res.setResult(true);
            } else {
                res.setMessage("Some error ocurred while Firing OTP");
                res.setResult(false);
            }
            return res;
        } else {
            res.setMessage("Some error ocurred while Firing OTP");
            res.setResult(false);
            return res;
        }
    }

    public JsonResponse createPassword(PasswordRequest request) {
        JsonResponse res = new JsonResponse();

        // Check if user is valid
        Optional<MasterUser> userOpt = masterUserRepository.findById(request.getUserId());
        if (userOpt.isPresent()) {
            MasterUser user = userOpt.get();
            // Check if token is valid
            Optional<MasterOtp> optOpt = masterOtpRepository.findByEmail(user.getEmail());
            if (optOpt.isPresent() && optOpt.get().getToken().equals(request.getToken())) {

                // Check if password and confirm pasword matches
                if (request.getPassword().equals(request.getCpassword())) {

                    // All validation passed set the user password
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user = masterUserRepository.save(user);

                    if (user != null) {
                        user.setPassword(null);
                        res.setPayload(user);
                        res.setMessage("User Password saved successfully !");
                        res.setResult(false);
                    } else {
                        res.setMessage("Error while updating password ! ErrCode : 102");
                        res.setResult(false);
                    }

                } else {
                    res.setMessage("Password & Confirm Password does not match");
                    res.setResult(false);
                }

            } else {
                res.setMessage("Invalid Token Received");
                res.setResult(false);
            }

        } else {
            res.setMessage("Invalid User Received");
            res.setResult(false);
        }
        return res;
    }

    public JsonResponse createProfile(@Valid ProfileRequest request) {
        JsonResponse res = new JsonResponse();

        // Check if user is valid
        Optional<MasterUser> userOpt = masterUserRepository.findById(request.getUserId());
        if (userOpt.isPresent()) {
            MasterUser user = userOpt.get();
            // Check if token is valid
            Optional<MasterOtp> optOpt = masterOtpRepository.findByEmail(user.getEmail());
            if (optOpt.isPresent() && optOpt.get().getToken().equals(request.getToken())) {

                // TODO: Create a family member with these details
                // Save profile details
                user.setMobileNumber(request.getMobileNumber());

                user = masterUserRepository.save(user);

                if (user != null) {
                    TransFamilyMember familyMember = null;
                    // Get Primary Family Member for the user
                    Optional<TransFamilyMember> familyMemberOpt = familyMemberRepository
                            .findByisPrimaryAndMasterUser_userId(true, request.getUserId());
                    if (familyMemberOpt.isPresent()) {

                        // Select existing Family Member for updation
                        familyMember = familyMemberOpt.get();
                    } else {
                        // Create a new Family Member
                        familyMember = new TransFamilyMember();
                    }
                    // Update the family member with these details
                    familyMember.setFirstName(request.getFirstName());

                    familyMember.setLastName(request.getLastName());
                    familyMember.setDob(request.getDob());
                    familyMember.setMobileNumber(request.getMobileNumber());
                    familyMember.setEmail(user.getEmail());

                    // Save Family Member details
                    familyMember = familyMemberRepository.save(familyMember);

                    if (familyMember != null) {
                        user.setPassword(null);
                        res.setPayload(user);
                        res.setMessage("User profile updated successfully !");
                        res.setResult(true);
                    } else {
                        res.setMessage("Failed saving user profile ! ErrCode : 103");
                        res.setResult(false);
                    }
                } else {
                    res.setMessage("Failed saving user profile ! ErrCode : 103");
                    res.setResult(false);
                }
            } else {
                res.setMessage("Invalid Token Received");
                res.setResult(false);
            }

        } else {
            res.setMessage("Invalid User Received");
            res.setResult(false);
        }

        return res;
    }

    public JsonResponse createAddress(@Valid AddressRequest request) {
        return null;
    }

    public JsonResponse createAddtional(@Valid AddtionalRequest request) {
        return null;
    }
}
