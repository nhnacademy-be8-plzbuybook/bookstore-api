package com.nhnacademy.book.member.domain.service.Impl;


import com.nhnacademy.book.member.domain.*;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.exception.AuthNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberAuthRepository memberAuthRepository;
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    // 회원에게 권한 부여
    @Override
    public MemberAuthResponseDto assignAuthToMember(MemberAuthRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
        Auth auth = authRepository.findById(requestDto.getAuthId())
                .orElseThrow(() -> new AuthNotFoundException("권한이 존재하지 않습니다."));

        Optional<MemberAuth> existingMemberAuth = memberAuthRepository.findByMemberAndAuth(member, auth);
        if (existingMemberAuth.isPresent()) {
            throw new IllegalArgumentException("이미 권한이 부여되었습니다.");
        }

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member);
        memberAuth.setAuth(auth);
        memberAuth = memberAuthRepository.save(memberAuth);

        return new MemberAuthResponseDto(
                memberAuth.getMember().getMemberId(),
                memberAuth.getAuth().getAuthId()
        );
    }

//    @Override
//    public List<String> getAuthsByMember(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다"));
//
//        return memberAuthRepository.findByMember(member)
//                .stream()
//                .map(memberAuth -> memberAuth.getAuth().getAuthName())
//                .collect(Collectors.toList());
//    }

    // 회원의 권한 조회
    @Override
    public List<String> getAuthNameByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        return memberAuthRepository.findByMember(member)
                .stream()
                .map(memberAuth -> memberAuth.getAuth().getAuthName())
                .collect(Collectors.toList());
        }

    // 회원 권한 수정
    @Override
    public MemberAuthResponseDto updateMemberAuth(MemberAuthRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
        Auth newAuth = authRepository.findById(requestDto.getAuthId())
                .orElseThrow(() -> new AuthNotFoundException("권한이 존재하지 않습니다."));

        Optional<MemberAuth> existingMemberAuthOpt = memberAuthRepository.findByMemberAndAuth(member, newAuth);
        if (existingMemberAuthOpt.isPresent()) {
            throw new RuntimeException("회원은 이미 해당 권한을 가지고 있습니다.");
        }

        MemberAuth memberAuth = memberAuthRepository.findByMember(member).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("회원의 권한을 찾을 수 없습니다"));

        memberAuth.setAuth(newAuth);
        MemberAuth updatedMemberAuth = memberAuthRepository.save(memberAuth);

        return new MemberAuthResponseDto(updatedMemberAuth.getMember().getMemberId(),
                updatedMemberAuth.getAuth().getAuthId());
    }

    // 회원 권한 제거
    @Override
    public void deleteAuthFromMember(Long memberId, Long authId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new AuthNotFoundException("권한이 존재하지 않습니다."));
        MemberAuth memberAuth = memberAuthRepository.findByMemberAndAuth(member, auth)
                .orElseThrow(() -> new RuntimeException("회원의 해당 권한을 찾을 수 없습니다."));

        memberAuthRepository.delete(memberAuth);
    }
}