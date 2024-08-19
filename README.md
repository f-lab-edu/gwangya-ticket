# gwangya-ticket
<p align="center">
  <img width="100%" alt="image" src="https://github.com/user-attachments/assets/fad840ed-f31f-4f08-9aa3-82a27ecddaba">
</p>
멜론 티켓, 인터파크와 같은 서비스의 ‘티켓 예매’ 기능에 초점을 맞춘 프로젝트입니다.
<br><br>

# 프로젝트 목표
- 특정 시점에 대규모 트래픽이 몰리는 티켓 서비스 특성에 맞춰 유연하면서 데이터 정합성을 보장할 수 있는 환경을 구현하고자 하였습니다.  
- 요구 사항 변경에 유연하게 대응할 수 있도록 객체 지향 원리에 근거한 단단한 설계와 클린 코드 작성을 목표로 하였습니다.
- 요구 사항 기반 테스트 코드를 작성하여 정상 동작을 보장하는 안정적인 애플리케이션을 개발하고자 하였습니다.

# 프로젝트 기술 스택
- ![Static Badge](https://img.shields.io/badge/Java-17-007396)
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-6DB33F?logo=spring%20boot&logoColor=6DB33F)
- ![MySQL](https://img.shields.io/badge/MySQL-8.0.33-4479A1?logo=mysql&logoColor=4479A1)
- ![Hazelcast](https://img.shields.io/badge/Hazelcast-5.3.6-b6ff00%3Flogo%3DHazelcast%26logoColor%3Db6ff00)
- ![Hibernate](https://img.shields.io/badge/JPA(Hibernate)-6.2.13-59666C?&logo=Hibernate&logoColor=white)
- ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.1.5-6DB33F?logo=spring%20boot&logoColor=6DB33F)
- ![QueryDSL](https://img.shields.io/badge/QueryDSL-5.0.0-0089CF?&logo=Querydsl&logoColor=0089CF)
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-3.1.5-6DB33F?style=flat&logo=Spring%20Security&logoColor=6DB33F)
- ![JUnit](https://img.shields.io/badge/JUnit-5.9.3-25A162?logo=junit&logoColor=white)

# 서비스 아키텍처
![Blank document - Page 1 (1)](https://github.com/user-attachments/assets/d03bde0d-1f96-44ab-b62f-7fd2d2457ec9)


# 좌석 선택 Flow
![좌석선택](https://github.com/user-attachments/assets/08cc2f28-8b8e-4884-a2c2-fbecfe7d771d)

# 예매 하기 Flow
![예매하기](https://github.com/user-attachments/assets/d2173fd4-dc89-44e5-9d09-904664e4c6e6)

# 좌석 선택 동시성 이슈
[Hazelcast 분산 락으로 동시성 제어하기](https://velog.io/@lshlovejys/%ED%8B%B0%EC%BC%93-%EC%98%88%EB%A7%A4-%EB%B6%84%EC%82%B0-%EB%9D%BD%EC%9C%BC%EB%A1%9C-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4%ED%95%98%EA%B8%B0)
### ✔️ 문제 
다중 서버 환경에서 동일한 좌석을 2명 이상의 유저가 선택하는 동시성 이슈 발생
### ✔️ 해결 및 성과
Hazelcast의 분산 락을 이용하여 좌석 번호(PK)에 잠금을 걸어 좌석 선택 정보를 보장할 수 있었습니다. <br>
AP를 제공하는 Hazelcast Map 저장소에 좌석과 선택한 유저 정보를 저장함으로써 데이터베이스로의 부하를 최소화하였습니다. 또한 데이터 삭제 시점에 이벤트를 통한 락 해제로 잠금 간의 동시성 문제를 방지할 수 있었습니다.
#### ➡️ 부하테스트 
Hazelcast 단일 서버 2Core, 8GB 사양으로 오류없이 **최대 418 TP**S / 전체 좌석에 대한 **실질 처리율 약 88%**를 확보할 수 있었습니다. <br>
<p align="left">
  <img width="80%" alt="image" src="https://github.com/user-attachments/assets/93d04903-ebec-4bc9-8ef8-d03b9df3c3ef">
  <img width="80%" alt="image" src="https://github.com/user-attachments/assets/f0118641-323f-4b76-b8e8-30f01ee86aa0">
</p>
<br>
MySQL 서버의 CPU 사용률 25% 미만, 메모리 사용률 17% 미만으로 RDB에 대한 부하를 최소화할 수 있었습니다. 
<br><br>
<p align="left">
  <img width="80%" alt="image" src="https://github.com/user-attachments/assets/9c0cf956-8619-4920-9fc6-fd0a098b3548">
  <img width="80%" alt="image" src="https://github.com/user-attachments/assets/d5c8b02a-0ca1-4682-8cf6-dccd14ca3780">
</p>
<br><br>

# Commit Convention
- feat : 새로운 기능 추가
- fix : 버그 수정
- docs : 문서, 주석 수정
- refactor : 코드 리팩토링
- test : 테스트 코드
- chore : 빌드 업무, 패키지 매니저 수정
