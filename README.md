# JPA

## 어노테이션
- @Entity : 이 클래스를 테이블과 매핑한다고 JPA에게 알림, 엔티티 클래스라 부름, (기본 생성자 필수, [final, enum, interfae, inner 클래스에 사용 불가], 필드에 final 하면 x)
- @Table : 엔티티 클래스에 매핑할 테이블 정보를 알려줌, name 속성을 통하여 매핑 ex @Table(name="MEMBER")
- @Id : 엔티티 클래스의 필드를 테이블의 기본 키에 매핑, 식별자 필드로 선언
- @IdClass : 식별자 클래스, 복합 기본키를 쓰고 싶을 때 클래스 명 위에 선언, 227p (클래스 생성 안 하고 쓸거면 231p 참고)
- @GeneratedValue : 식별자 값 자동 생성, strategy 속성을 통헤 식별자 생성 전략 선택, 직접 할당, IDENTITY, SEQUENCE, TABLE, AUTO 전략이 존재
  @GeneratedValue(strategy = GenerationType.IDENTITY),   
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")   
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "BOARD_SEQ_GENERATOR")   
  @GeneratedValue(strategy = GenerationType.AUTO) : 디비 방언에 따라 IDENTITY, SEQUENCE, TABLE 전략 중 하나를 자동 선택,   
  스키마 자동 생성 기능 사용하면 하이버네이트가 시퀀스나 키 테이블 만들어 준다.
- @SequenceGenerator : 시퀀스 생성자, name, sequenceName, initialValue, allocationSize 속성 존재
- @TableGenerator : 키 전용 테이블 생성자, name, table, pkColumnValue, allocationSize 속성 존재
- @Column : 필드를 컬럼에 매핑, name 속성을 통하여 특정 컬럼에 매핑 가능, nullable 속성으로 null 처리 설정, length 속성으로 문자의 크기 설정
   (매핑 정보가 없는 필드는 필드명을 통해 컬럼명으로 매핑, 대소문자 구분은 DB마다 다름)   
- @Enumerated : 자바의 enum 타입을 매핑
- @Temporal : 날짜 타입을 매핑
- @Lob : BLOB, CLOB 타입을 매핑
- @Transient : 이 필드는 매핑하지 않는다, 객체에 임시로 어떤 값을 보관하려 할 때 사용
- @Access : JPA가 엔티티에 접근하는 방식을 지정, AccessType.FIELD는 private여도 필드에 직접 접근, AccessType.PROPERTY는 접근자를 사용하여 접근 (152p)
- @org.hibernate.annotations.DynamicUpdate : 전체 필드가 아닌 수정된 데이터만 사용해서 동적으로 UPDATE SQL을 생성
- @ManyToOne : 이름 그대로 다대일 관계라는 매핑 정보
- @OneToMany : 일대다 관계라는 매핑 정보, mappedBy 속성은 양방향 매핑일 때 사용하는데 반대쪽 매핑의 필드 이름 값을 주면 된다(연관관계 주인은 사용 x)
- @ManyToMany : 다대다 관계라는 매핑 정보, 테이블과는 다르게 테이블 3개 필요없이 객체는 객체 2개만으로 다대다 관계 생성 가능
- @JoinColumn : 외래 키를 매핑할 때 사용, name 속성을 통해 매핑할 외래 키 이름을 지정
- @JoinTable : 연결 테이블 매핑, name(연결 테이블 지정), joinColumns와inverseJoinColumns(조인 컬럼 정보 지정)
- @MappedSuperclass : 부모 클래스는 테이블과 매핑하지 않고 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공하고 싶을 때 사용
- @Embeddable : 값 타입을 정의하는 곳에 표시 (응집력과 가독성을 높이기 좋음)
- @Embedded : 값 타임을 사용하는 곳에 표시 (임베디드 타입이 null이면 매핑한 컬럼 값은 모두 null이 됨)
- @AttributeOverride : 임베디드 타입에 정의한 매핑정보를 재정의 할 때 사용


## persistence.xml 설정
- JPA는 persistence.xml을 통해 필요한 설정 정보 관리
- META-INF/persistence.xml 패스에 있으면 JPA가 다른 설정없이 인식 가능
```
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             version="2.1">
    <persistence-unit name="jpa"> <!-- 일반적으로 디비 하나당 영속성 유닛 등록, 유닛에 jpa라는 이름 부여-->
        <class>com.jpa.Member</class>를 통해 직접 엔티티 클래스 인식 하게 함
        <properties>
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/> <!-- JDBC 드라이버 -->
            <property name="javax.persistence.jdbc.user" value="sa"/> <!-- 디비 접속 아이디 -->
            <property name="javax.persistence.jdbc.password" value=""/> <!--  디비 접속 비밀 번호 -->
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/> <!-- 디비 접속 URL -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/> 
            <!-- DB 방언 설정, JPA는 특정 디비에 종속 되지 않음, SQL 문법이 다 다르기에 어떻게 해설할건지 정함 -->
            <property name="hibernate.hbm2ddl.auto" value="create"/> <!-- 스키마 자동 생성 기능 -->
            <property name="hibernate.show_sql" value="true"/> <!-- DDL 출력 -->
        </properties>
    </persistence-unit>
</persistence>
```

## 영속성 컨텍스트 특징
- 영속성 컨텍스트는 엔티티를 식별자 값으로 구분하기에 식별자 값이 반드시 필요, 없으면 예외 발생
- 영속성 컨텍스트에 엔티티를 저장해도 트랜잭션을 커밋하기 전까지는 디비에 반영 x -> 내부 쿼리 저장소에 차곡차곡 저장
- 내부에 1차 캐시라고 하는 캐시를 갖고 있는데 영속 상태 엔티티는 이곳에 저장됨, Map 형식으로 저장, 식별자 값과 엔티티 인스턴스
  (find로 조회 시 1차 캐시에 있으면 캐시에서 가져오고(디비 접근 안 해서 성능상 이점) 없으면 디비 조회 후 캐시에 등록 후 반환)
- 변경 감지, 영속성 컨텍스트에 보관할 때 최초 상태를 복사해서 스냅샷으로 저장해 둠, 플러시 시점에 스냅샷과 엔티티를 비교해서 변경된 엔티티 찾음
  1. 트랙잭션 커밋하면 플러시 호출   
  2. 엔티티와 스냅샷 비교 후 변경된 엔티티 찾음 (영속 상태 엔티티에만 적용)
  3. 변경된 엔티티 있으면 수정 쿼리 생성 후 SQL 저장소에 저장 
  4. 저장된 SQL을 디비에 보냄
  5. 트랜잭션 커밋
  ★ UPDATE 쿼리는 수정된 필드만 반영 하는 것이 아니라 모든 필드를 수정에 반영 함, 데이터 전송량은 증가하지만 수정 쿼리를 일치시키기 편해 재사용성이 높아지기 때문
  (컬럼이 약 30개 이상 되면 @org.hibernate.annotations.DynamicUpdate를 사용하는 것이 쿼리 속도가 빠르다고 함.)
  
## 플러시
- 영속성 컨텍스트의 변경 내용을 디비에 반영하는 것
- 변경 감지 동작하여 수정된 엔티티가 있으면 수정 쿼리를 SQL 저장소에 등록한 후 SQL 저장소에 있던 쿼리들을 디비에 전송 함
- 플러시 하는 방법 3가지
  1. flush()를 통해 직접 호출
  2. 트랜잭션 커밋 시 자동 호출
  3. JPQL 쿼리 실행 시 자동 호출 (but find() 메소드 호출 할 때는 플러시 실행 x, 108p 참고)
- em.setFlushMode(FlushModeType.COMMIT or FlushModeType.AUTO)로 플러시 모드 옵션 지정, COMMIT은 커밋할 때만 되도록, AUTO는 커밋이나 쿼리 실행 할 때 되도록

## merge()
- 준영속 상태의 엔티티를 다시 영속 상태로 변경할 때 사용
- 준영속 상태의 엔티티를 받아서 새로운 영속 상태의 엔티티를 반환, public <T> T merge (T entity)
- 1차 캐시에서 엔티티 조회, 없으면 디비에서 가져와서 캐시에 등록 후 조회한 엔티티에 파라미터 엔티티의 값을 밀어 넣고 조회된 엔티티를 반환
- 비영속 엔티티인 경우 파라미터로 넘어온 엔티티 식별자 값으로 컨텍스트 조회 -> 없으면 디비 조회 -> 없으면 새로운 엔티티 생성 후 병합
- 즉 merge()는 save 혹은 update 기능 수행한다.
  
## IDENTITY 식별자 생성 전략
- 기본 키 생성을 디비에 위임하는 전략, 디비가 자동으로 기본 키를 생성해줌
- 디비에 값을 저장 할 때 기본 키 컬럼을 비워두면 디비가 순서대로 값을 채움
- 엔티티가 영속 상태가 되려면 식별자가 필요
- IDENTITY 전략은 엔티티를 디비에 저장해 식별자를 구할 수 있어서 persist() 호출하는 즉시 INSERT 쿼리가 디비에 전달
- 즉 이 방법은 쓰기 지연이 동작 x
  
## SEQUENCE 식별자 생성 전략 (138p)
- 내부 동작은 IDENTITY 전략과 다름
- persist() 호출 할 때 디비 시퀀스를 사용해서 식별자를 조회 함
- 이후에 엔티티에 할당한 후에 엔티티를 영속성 컨텍스트에 저장
- 그리고 트랜잭션을 커밋하면 플러시가 발생하여 엔티티를 디비에 저장
- 쓰기 지연이 가능한가(?)
- IDENTITY는 엔티티를 디비에 저장 후 식별자 조회 후 엔티티 식별자에 할당
  
## TABLE 식별자 생성 전략 (141p)
- 키 생성 전용 테이블을 만들고 여기에 이름과 값으로 사용할 컬럼을 만들어 디비 시퀀스를 흉내내는 전략
- 어느 디비에서든 적용 가능 하다. (시퀀스 전략은 디비마다 안 될 수도 있음)
- 테이블 사용한다는 것만 제외하면 SEQUENCE 전략과 내부 동작 방식이 같음
  
## 복합 키
- JPA는 영속성 컨텍스트에 엔티티를 보관할 때 엔티티의 식별자를 키로 사용
- 식별자를 구분하기 위해 equals와 hashCode를 사용해서 동등성을 비교
- 식별자 필드가 하나 일 떄는 자바의 기본 타입을 사용하므로 문제 없지만,
- 식별자 필드가 2개 이상이면 별도의 식별자 클래스를 만들고 그곳에 equals와 hashCode를 구현해야 함
- @IdClass (관계형 베이스에 가까운 방법), @EmbeddedId (객체지향에 가까운 방법) 두 어노테이션을 사용하여 복합 키 구현
- 식별자 클래스 조건 확인하기 : 258p, 260p
- 259p 외래 키가 복합 키일 경우
  
## 프록시 (지연 로딩)
  - 특정 엔티티만 사용하려하는데 연관된 엔티티까지 조회하는 것은 너무 비효율적
  - 엔티티가 실제 사용될 때까지 조회를 지연할 수 있는데, 지연 로딩이라 함
  - em.getReference를 통해 디비를 조회하지 않고 실제 엔티티를 생성하지도 않고 디비 접근을 위임한 프록시 객체를 반환
  - 프록시 클래스는 실제 클래스를 상속 받아서 만들어져서 실제 클래스와 겉 모습은 같은
  - 프록시 객체를 초기화한다해서 실제 엔티티로 바뀌는 건 x
  - 프록시 객체는 원본 엔티티를 상속 받은 거라 타입 체크 주의
  - 영속성 컨텍스트에 찾으려는 엔티티가 있으면 디비 조회 필요 없어서 getReference() 호출해도 실제 엔티티 반환

## 즉시 로딩과 지연 로딩
  - 즉시 로딩 : 엔티티 조회할 때 연관된 엔티티도 함께 조회 (fetch=FetchType.EAGER)
  - 지연 로딩 : 연관된 엔티티를 실제 사용할 때 조회 (fetch=FetchType.LAZY)
  
## 외부 조인 -> 내부 조인
  - 디폴트는 외부 조인, 성능이 상대적으로 안 좋음
  - @JoinColumn 속성에 nullable = false로 하여 내부 조인 가능
  - @ManyToOne 속성에 optional = false로 하여 내부 조인 가능
  - JPA는 선택적 관계면 외부 조인, 필수 관계면 내부 조인 사용
  
## 영속성 전이 : CASCADE
  - JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 함
  - 영속성 전이 저장을 사용하면 부모만 영속 상태로 만들면 연관된 자식까지 한 번에 영속 상태로 만들 수 있음
  - 영속성 전이 삭제를 사용하면 부모 엔티티를 삭제하면 연관된 자식 엔티티도 함께 삭제 된다.
  - CascadeType.PERSIST, CascadeType.REMOVE는 persist()나 remove() 호출 시 전이가 바로 발생하지 않고 플러시하면 전이가 발생 한다.
  
