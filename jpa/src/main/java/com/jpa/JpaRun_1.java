package com.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

public class JpaRun_1 {
    public static void main(String[] args) {
        //설정 정보 조회 -> 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
        //매니저 생성
        EntityManager em = emf.createEntityManager();


        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close();
        }emf.close();
    }

    private static void logic(EntityManager em){
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setName("재훈");
        member.setAge(2);

        em.persist(member);

        member.setAge(20);

        Member findMember = em.find(Member.class, id);
        System.out.println("findMember = " + findMember.getName() + " age=" + findMember.getAge());

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        System.out.println("size="+members.size());
    }
}
