package com.baconbao.mxh.Repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

<<<<<<< Updated upstream
public interface RelationshipRepository extends JpaRepository<Relationship, Long>{
    @Query("SELECT r FROM Relationship r " +
            "WHERE r.userOne = :firstUser OR r.userTwo = :firstUser")
    List<Relationship> findAllByUserOneId(@Param("firstUser") User firstUser);
    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.userOne = :firstUser AND r.userTwo = :secondUser) " +
            "OR (r.userOne = :secondUser AND r.userTwo = :firstUser) ")
    Relationship findRelationship(@Param("firstUser") User firstUser,
            @Param("secondUser") User secondUser);

=======
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findAllByUserOneId(Long id);

    @Query("\"SELECT r FROM Relationship r \" +\r\n" + //
            "            \"WHERE (r.userOne = :firstUser AND r.userTwo = :secondUser) \" +\r\n" + //
            "            \"OR (r.userOne = :secondUser AND r.userTwo = :firstUser) \" +\r\n" + //
            "            \"ORDER BY r.createAt\"")
    Relationship relationshipFromUser(@Param("firstUser") User firstUser,
            @Param("secondUser") User secondUser);
>>>>>>> Stashed changes
}
