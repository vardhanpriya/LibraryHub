//package com.libraryhub.api.utility;
//
//import com.libraryhub.identity.security.CustomUserDetails;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class SecurityUtils {
//
//    public static Long getCurrentUserId() {
//        CustomUserDetails userDetails =
//                (CustomUserDetails) SecurityContextHolder
//                        .getContext()
//                        .getAuthentication()
//                        .getPrincipal();
//
//        return userDetails.getUser().getUserId();
//    }
//
//    public static String getCurrentUserEmail() {
//        CustomUserDetails userDetails =
//                (CustomUserDetails) SecurityContextHolder
//                        .getContext()
//                        .getAuthentication()
//                        .getPrincipal();
//
//        return userDetails.getUsername();
//    }
//}
