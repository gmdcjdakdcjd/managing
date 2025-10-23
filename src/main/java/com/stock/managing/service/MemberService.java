package com.stock.managing.service;


import com.stock.managing.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception {
    }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException ;
}
