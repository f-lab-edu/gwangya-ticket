# gwangya-ticket
인터파크, 멜론 티켓과 같은 서비스의 ‘티켓 예매’ 기능에 초점을 맞춘 프로젝트입니다.

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
![아키텍처 drawio](https://github.com/f-lab-edu/gwangya-ticket/assets/43931448/3e853214-6ddb-483a-a945-e2ede88e2f42)


# 좌석 선택 동시성 이슈
[Hazelcast 분산 락으로 동시성 제어하기](https://velog.io/@lshlovejys/%ED%8B%B0%EC%BC%93-%EC%98%88%EB%A7%A4-%EB%B6%84%EC%82%B0-%EB%9D%BD%EC%9C%BC%EB%A1%9C-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4%ED%95%98%EA%B8%B0)

# Commit Convention
- feat : 새로운 기능 추가
- fix : 버그 수정
- docs : 문서, 주석 수정
- refactor : 코드 리팩토링
- test : 테스트 코드
- chore : 빌드 업무, 패키지 매니저 수정
