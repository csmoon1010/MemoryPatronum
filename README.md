# MemoryPatronum : 2020-1 졸업작품
## 주제
* 치매 예방을 위한 안드로이드 어플


## 개발 동기
* 통합적, 개인 맞춤형 관리를 통해 치매를 예방 수칙의 실행을 도모하기 위해서 개발했다.
* 보건복지부와 중앙치매센터가 권고하는 3가지인 3권(걷기, 생선과 채소 골고루 먹기, 부지런히 읽고 쓰기)와 관련 지음


## 개발 언어 및 환경
* Android Studio(Java)
* MySQL
* php
* Python(Crawling)
* Ultra Edit
* DL4J 라이브러리
<img src="https://user-images.githubusercontent.com/49116712/104541180-60760e00-5664-11eb-8e06-ba24049baf33.PNG">
<br>

## 개요

* 식단 관리 : MIND 식단에 대한 정보 조회와 일일 평가
  * Python 크롤링을 통한 식단 DB 구축
  * Ultra Edit를 통한 데이터 클렌징
  * Trigger를 통한 개인 식단 저장 및 평가 과정 수행

* 예방체조 : 보건복지부와 중앙치매센터가 제작한 치매예방체조 참조
  * url을 이용한 videoview 활용으로 동영상 삽입

* 회상일기 : 일기 작성과 키워드 추출 기반의 회상 테스트
  * Komoran 형태소 분석기 사용
  * DL4J를 이용해 TextRank 알고리즘을 구현한 키워드 추출

* 인지게임 : 인지 능력 향상을 위한 카드 짝 맞추기 게임
* 음성 인식, 음성 출력 기능 추가
  * SST, TTS 이용

## 기타
[관련 자료](https://drive.google.com/open?id=1ogs0yZZUMmYDphnPCvVe4AezSzvJEl5X)
