package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        // "hello"라는 이름의 영속성 유닛(Persistence Unit)을 기반으로 EntityManagerFactory를 생성한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManagerFactory를 사용하여 EntityManager를 생성한다.
        // EntityManager는 JPA에서 데이터베이스와의 상호작용을 관리하는 주요 인터페이스이다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션을 시작하기 위해 EntityTransaction 객체를 가져온다.
        EntityTransaction tx = em.getTransaction();

        // 트랜잭션을 시작한다.
        tx.begin();

        try {
            //팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            //회원 저장
            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team); //단방향 연관관계 설정, 참조 저장
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }

            tx.commit();
        } catch (Exception e) {
            // 예외가 발생하면 트랜잭션을 롤백하여 모든 변경 사항을 취소한다.
            tx.rollback();
        } finally {
            // EntityManager를 닫아 자원을 해제한다.
            em.close();
        }

        // EntityManagerFactory를 닫아 자원을 해제한다.
        emf.close();

    }
}
