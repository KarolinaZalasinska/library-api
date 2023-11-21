package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.BorrowDto;
import com.example.libraryapi.dto.LateFeeDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.LateFee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BorrowRepository;
import com.example.libraryapi.repository.LateFeeRepository;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LateFeeService {
    private final LateFeeRepository lateFeeRepository;
    private final BorrowRepository borrowRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public LateFeeDto createLateFee(@Valid LateFeeDto lateFeeDto) {

        Borrow borrow = borrowRepository.findById(lateFeeDto.id())
                .orElseThrow(() -> new ObjectNotFoundException("Borrow with id " + lateFeeDto.id() + " was not found."));

        LateFee lateFee = modelMapper.map(lateFeeDto, LateFee.class);
        lateFee.setBorrow(borrow);

        LateFee createdLateFee = lateFeeRepository.save(lateFee);
        return modelMapper.map(createdLateFee, LateFeeDto.class);
    }

    public LateFeeDto getLateFeeById(final Long lateFeeId) {
        Optional<LateFee> optionalLateFee = lateFeeRepository.findById(lateFeeId);
        return optionalLateFee.map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Late fee with ID " + lateFeeId + " was not found."));
    }

    public List<LateFeeDto> getAllLateFees() {
        List<LateFee> lateFees = lateFeeRepository.findAll();
        if (lateFees.isEmpty()) {
            return Collections.emptyList();
        } else {
            return lateFees.stream()
                    .map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public LateFeeDto updateLateFee(final Long id, @Valid LateFeeDto lateFeeDto) {
        return lateFeeRepository.findById(id)
                .map(lateFee -> {
                    modelMapper.map(lateFeeDto, lateFee);
                    LateFee updatedLateFee = lateFeeRepository.save(lateFee);
                    return modelMapper.map(updatedLateFee, LateFeeDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("Late fee with id " + id + " was not found."));
    }

    @Transactional
    public void deleteLateFee(Long id) {
        LateFee lateFee = lateFeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Late fee with id " + id + " was not found."));
        lateFeeRepository.delete(lateFee);
    }

    public List<LateFeeDto> getLateFeesByBorrowID(final Long borrowId) {
        return borrowRepository.findById(borrowId)
                .map(lateFeeRepository::findLateFeesByBorrow)
                .orElseThrow(() -> new ObjectNotFoundException("Borrow with id " + borrowId + " was not found."))
                .stream()
                .map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateLateFeeForReturn(LocalDate expectedReturnDate, LocalDate returnDate) {
        if (returnDate.isAfter(expectedReturnDate)) {
            long daysLate = ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
            BigDecimal lateFeePerDay = BigDecimal.valueOf(5.0);
            return lateFeePerDay.multiply(BigDecimal.valueOf(daysLate));
        }
        return BigDecimal.ZERO;
    }

    public Optional<LateFeeDto> calculateAndRecordLateFee(final Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ObjectNotFoundException("Borrow with id " + borrowId + " was not found."));

        BigDecimal lateFeeAmount = calculateLateFeeForReturn(borrow.getExpectedReturnDate(), borrow.getReturnDate());

        if (lateFeeAmount.compareTo(BigDecimal.ZERO) > 0) {
            LateFee lateFeeRecord = new LateFee();
            lateFeeRecord.setBorrow(borrow);
            lateFeeRecord.setAmount(lateFeeAmount);
            lateFeeRecord.setLateFeeDate(LocalDate.now());

            LateFee createdLateFee = lateFeeRepository.save(lateFeeRecord);
            return Optional.of(modelMapper.map(createdLateFee, LateFeeDto.class));
        } else {
            return Optional.empty();
        }
    }


}
