package com.nguyenphitan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nguyenphitan.entity.OrderAccount;

@Repository
public interface OrderAccountRepository extends JpaRepository<OrderAccount, Integer> {

	@Query(value = "SELECT MAX(Id) FROM order_account", nativeQuery = true)
	public int getMaxId();
	
	@Modifying
	@Query(value = "UPDATE order_account SET order_status = 1 WHERE id = ?1 ", nativeQuery = true)
	public void updateStatus(int id);
	
	@Query(value = "SELECT * FROM order_account where account_id=?1", nativeQuery = true)
	List<OrderAccount> findOrderAccountByAccountId(int accountId);
}
