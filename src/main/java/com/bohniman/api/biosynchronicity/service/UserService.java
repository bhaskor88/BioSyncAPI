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
                mem.getMasterUser().setPassword(null);
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

    public JsonResponse verifyOtpAndCreateUser(RegisterRequest request) {
        JsonResponse res = new JsonResponse();
        MasterOtp masterOtp = verifyOtp(request);
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
                user.setRoles(masterRoleRepository.findAllByRoleName("USER"));
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
            res.setMessage("Invalid Email or OTP received !");
            res.setResult(false);
        }
        return res;
    }

    public MasterOtp verifyOtp(RegisterRequest request) {
        Optional<MasterOtp> mstrOtp = masterOtpRepository.findByEmail(request.getEmail());
        if (mstrOtp.isPresent() && mstrOtp.get().getOtp().equals(request.getOtp())) {
            MasterOtp masterOtp = mstrOtp.get();
            masterOtp.setToken(OtpUtil.getRandomToken(16));
            masterOtp = masterOtpRepository.save(masterOtp);
            return masterOtp;
        }
        return null;
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
                // TODO: Uncomment below line in production
                // res.setMessage("Otp Fired to email - " + registerUser.getEmail() + "
                // successfully ! ");
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
            if (optOpt.isPresent() && optOpt.get().getToken() != null
                    && optOpt.get().getToken().equals(request.getToken())) {

                // Check if password and confirm pasword matches
                if (request.getPassword().equals(request.getCpassword())) {

                    // All validation passed set the user password
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user = masterUserRepository.save(user);

                    if (user != null) {
                        user.setPassword(null);
                        res.setPayload(user);
                        res.setMessage("User Password updated successfully !");
                        res.setResult(true);
                    } else {
                        res.setMessage("Error while updating password !");
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
            if (optOpt.get().getToken() != null && optOpt.isPresent()
                    && optOpt.get().getToken().equals(request.getToken())) {

                // Save profile details
                user.setMobileNumber(request.getMobileNumber());

                user = masterUserRepository.save(user);

                if (user != null) {
                    TransFamilyMember familyMember = null;
                    // Get Primary Family Member for the user
                    Optional<TransFamilyMember> familyMemberOpt = familyMemberRepository
                            .findByIsPrimaryAndMasterUser_userId(true, request.getUserId());
                    if (familyMemberOpt.isPresent()) {

                        // Select existing Family Member for updation
                        familyMember = familyMemberOpt.get();
                    } else {
                        // Create a new Family Member
                        familyMember = new TransFamilyMember();
                        familyMember.setPrimary(true);
                        familyMember.setMasterUser(user);
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
                        familyMember.getMasterUser().setPassword(null);
                        res.setPayload(familyMember);
                        res.setMessage("User profile updated successfully !");
                        res.setResult(true);
                    } else {
                        res.setMessage("Failed saving user profile !");
                        res.setResult(false);
                    }
                } else {
                    res.setMessage("Failed saving user profile !");
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
        JsonResponse res = new JsonResponse();
        // Check if user is valid
        Optional<MasterUser> userOpt = masterUserRepository.findById(request.getUserId());
        if (userOpt.isPresent()) {
            MasterUser user = userOpt.get();
            // Check if token is valid
            Optional<MasterOtp> optOpt = masterOtpRepository.findByEmail(user.getEmail());
            if (optOpt.get().getToken() != null && optOpt.isPresent()
                    && optOpt.get().getToken().equals(request.getToken())) {
                TransFamilyMember familyMember = null;
                // Get Primary Family Member for the user
                Optional<TransFamilyMember> familyMemberOpt = familyMemberRepository
                        .findByIsPrimaryAndMasterUser_userId(true, request.getUserId());
                if (familyMemberOpt.isPresent()) {

                    // Select existing Family Member for updation
                    familyMember = familyMemberOpt.get();

                    familyMember.setAddress(request.getAddress());
                    familyMember.setCity(request.getCity());
                    familyMember.setState(request.getState());
                    familyMember.setZip(request.getZip());

                    familyMember = familyMemberRepository.save(familyMember);

                    if (familyMember != null) {
                        familyMember.getMasterUser().setPassword(null);
                        res.setPayload(familyMember);
                        res.setMessage("User Address updated successfully !");
                        res.setResult(true);
                    } else {
                        res.setMessage("Failed saving user Address !");
                        res.setResult(false);
                    }
                } else {
                    // A primary Family Member not found for the User
                    res.setMessage("Primary Family Member Not found for the user ! Try creating a profile first.");
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

    public JsonResponse createAddtional(@Valid AddtionalRequest request) {
        JsonResponse res = new JsonResponse();
        // Check if user is valid
        Optional<MasterUser> userOpt = masterUserRepository.findById(request.getUserId());
        if (userOpt.isPresent()) {
            MasterUser user = userOpt.get();
            // Check if token is valid
            Optional<MasterOtp> optOpt = masterOtpRepository.findByEmail(user.getEmail());
            if (optOpt.get().getToken() != null && optOpt.isPresent()
                    && optOpt.get().getToken().equals(request.getToken())) {

                // Delete Security Token
                MasterOtp otp = optOpt.get();
                otp.setToken(null);
                otp = masterOtpRepository.save(otp);

                if (otp != null) {

                    // Enable the user for login
                    user.setEnable(true);
                    user = masterUserRepository.save(user);

                    if (user != null) {

                        TransFamilyMember familyMember = null;
                        // Get Primary Family Member for the user
                        Optional<TransFamilyMember> familyMemberOpt = familyMemberRepository
                                .findByIsPrimaryAndMasterUser_userId(true, request.getUserId());
                        if (familyMemberOpt.isPresent()) {
                            // Select existing Family Member for updation
                            familyMember = familyMemberOpt.get();
                            familyMember.setGender(request.getGender());
                            familyMember.setEthnicity(request.getEthnicity());
                            familyMember.setRace(request.getRace());
                            familyMember.setPrimaryUse(request.getPrimaryUse());

                            familyMember = familyMemberRepository.save(familyMember);

                            if (familyMember != null) {
                                familyMember.getMasterUser().setPassword(null);
                                res.setPayload(familyMember);
                                res.setMessage("User Additional Details updated successfully !");
                                res.setResult(true);
                            } else {
                                res.setMessage("Failed saving user Additional Details !");
                                res.setResult(false);
                            }
                        } else {
                            // A primary Family Member not found for the User
                            res.setMessage(
                                    "Primary Family Member Not found for the user ! Try creating a profile first.");
                            res.setResult(false);
                        }
                    } else {
                        res.setMessage("Failed Activating User Account.");
                        res.setResult(false);
                    }

                } else {
                    res.setMessage("Some Error has ocurred !");
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

    public JsonResponse fireOtpForgotPassword(RegisterRequest registerUser) {
        // TODO: Wait 2 Minutes before firing new OTP
        JsonResponse res = new JsonResponse();
        Optional<MasterUser> usrOpt = masterUserRepository.findByEmail(registerUser.getEmail());
        if (usrOpt.isPresent()) {
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
                    // TODO: Uncomment below line in production
                    // res.setMessage("Otp Fired to email - " + registerUser.getEmail() + "
                    // successfully ! ");
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
        res.setMessage("User not found for provided email ID");
        res.setResult(false);
        return res;
    }

    public JsonResponse verifyOtpForgotPassword(RegisterRequest registerRequest) {
        JsonResponse res = new JsonResponse();
        MasterOtp masterOtp = verifyOtp(registerRequest);
        if (masterOtp != null) {
            MasterUser user = null;
            // Check if the user with the same email exist
            Optional<MasterUser> usrOpt = masterUserRepository.findByEmail(registerRequest.getEmail());
            if (usrOpt.isPresent()) {
                // Select the existing user for updation
                user = usrOpt.get();
                user.setPassword(masterOtp.getToken());
                res.setPayload(user);
                res.setResult(true);
                res.setMessage("OTP verified Successfully !");
            } else {
                res.setMessage("User not found for provided email ID");
                res.setResult(false);
            }
        } else {
            res.setMessage("Invalid Email or OTP received !");
            res.setResult(false);
        }
        return res;
    }

    public JsonResponse getUserProfile(Long userId) {
        JsonResponse res = new JsonResponse();

        Optional<TransFamilyMember> memOpt = familyMemberRepository.findByIsPrimaryAndMasterUser_userId(true, userId);
        if (memOpt.isPresent()) {
            TransFamilyMember mem = memOpt.get();
            mem.getMasterUser().setPassword(null);
            res.setPayload(mem);
            res.setMessage("User Profile Fetch Successfully !");
            res.setResult(true);
            return res;
        }
        res.setMessage("Invalid User Id Received !");
        res.setResult(false);
        return res;
    }
}
