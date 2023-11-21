package com.example.libraryapi.repository;

import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.LateFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LateFeeRepository extends JpaRepository<LateFee, Long> {
    List<LateFee> findAllByBorrow_User_Id(Long userId);

   // List<LateFee> findAllByAmountGreaterThan(double amount);

 //   List<LateFee> findAllByLateFeeDate(LocalDate date);

    List<LateFee> findLateFeesByBorrow(Borrow borrow);
}
