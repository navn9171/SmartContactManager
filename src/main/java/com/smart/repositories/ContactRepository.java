package com.smart.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	@Query("from Contact c where c.user.id =:user_id")
	public Page<Contact> getUserContacts(@Param("user_id") int user_id, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("delete from Contact c where c.c_id =:id")
	public void deleteContactByIdCustom(@Param("id") int id);
	
}
