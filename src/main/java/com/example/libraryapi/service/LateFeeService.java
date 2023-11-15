package com.example.libraryapi.service;

import com.example.libraryapi.dto.LateFeeDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.LateFee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BorrowRepository;
import com.example.libraryapi.repository.LateFeeRepository;

import javax.validation.Valid;
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
    public LateFeeDto createLateFee(@Valid final LateFeeDto lateFeeDto) {
        if (lateFeeDto.getAmount() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        Borrow borrow = borrowRepository.findById(lateFeeDto.getBorrowId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Borrow not found with the given ID", lateFeeDto.getBorrowId()));

        LateFee lateFee = modelMapper.map(lateFeeDto, LateFee.class);
        lateFee.setBorrow(borrow);

        LateFee createdLateFee = lateFeeRepository.save(lateFee);
        return modelMapper.map(createdLateFee, LateFeeDto.class);
    }

    public LateFeeDto getLateFeeById(final Long lateFeeId) {
        Optional<LateFee> optionalLateFee = lateFeeRepository.findById(lateFeeId);
        return optionalLateFee.map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("LateFee not found with the given ID", lateFeeId));
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
    public LateFeeDto updateLateFee(final Long id, @Valid final LateFeeDto lateFeeDto) {
        return lateFeeRepository.findById(id)
                .map(lateFee -> {
                    modelMapper.map(lateFeeDto, lateFee);
                    LateFee updatedLateFee = lateFeeRepository.save(lateFee);
                    return modelMapper.map(updatedLateFee, LateFeeDto.class);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("LateFee not found with the given ID", id));
    }

    @Transactional
    public void deleteLateFee(Long lateFeeId) {
        LateFee lateFee = lateFeeRepository.findById(lateFeeId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("LateFee not found with the given ID", lateFeeId));
        lateFeeRepository.delete(lateFee);
    }

    private double calculateLateFeeForReturn(LocalDate expectedReturnDate, LocalDate returnDate) {
        if (returnDate.isAfter(expectedReturnDate)) {
            long daysLate = ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
            double lateFeePerDay = 5.0;
            return daysLate * lateFeePerDay;
        }
        return 0.0;
    }

    public Optional<LateFeeDto> calculateAndRecordLateFee(final Long borrowId, LocalDate returnDate) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Borrow not found with the given ID", borrowId));

        double lateFeeAmount = calculateLateFeeForReturn(borrow.getExpectedReturnDate(), returnDate);

        if (lateFeeAmount > 0) {
            LateFee lateFeeRecord = new LateFee();
            lateFeeRecord.setBorrow(borrow);
            lateFeeRecord.setAmount(lateFeeAmount);
            lateFeeRecord.setDate(returnDate);

            LateFee createdLateFee = lateFeeRepository.save(lateFeeRecord);
            return Optional.of(modelMapper.map(createdLateFee, LateFeeDto.class));
        } else {
            return Optional.empty();
        }
    }

    public List<LateFeeDto> getLateFeesByBorrowID(final Long borrowId) {
        return borrowRepository.findById(borrowId)
                .map(lateFeeRepository::findLateFeesByBorrow)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Borrow not found with the given ID", borrowId))
                .stream()
                .map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                .collect(Collectors.toList());
    }

}
