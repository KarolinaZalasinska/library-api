package service;

import dto.LateFeeDto;
import exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import mapper.LateFeeMapper;
import model.Borrow;
import model.LateFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.BorrowRepository;
import repository.LateFeeRepository;

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
    private final LateFeeMapper lateFeeMapper;

    @Transactional
    public LateFeeDto createLateFee(LateFeeDto lateFeeDto) {
        if (lateFeeDto.getAmount() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        Borrow borrow = borrowRepository.findById(lateFeeDto.getBorrowId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Borrow not found with the given ID", lateFeeDto.getBorrowId()));

        LateFee lateFee = lateFeeMapper.toEntity(lateFeeDto);
        lateFee.setBorrow(borrow);

        LateFee createdLateFee = lateFeeRepository.save(lateFee);
        return lateFeeMapper.toDto(createdLateFee);
    }

    public LateFeeDto getLateFeeById(final Long lateFeeId) {
        Optional<LateFee> optionalLateFee = lateFeeRepository.findById(lateFeeId);
        return optionalLateFee.map(lateFeeMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("LateFee not found with the given ID", lateFeeId));
    }

    public List<LateFeeDto> getAllLateFees() {
        List<LateFee> lateFees = lateFeeRepository.findAll();
        if (lateFees.isEmpty()) {
            return Collections.emptyList();
        } else {
            return lateFees.stream()
                    .map(lateFeeMapper::toDto)
                    .collect(Collectors.toList());
        }
    }


    @Transactional
    public LateFeeDto updateLateFee(final Long id, final LateFeeDto lateFeeDto) {
        return lateFeeRepository.findById(id)
                .map(lateFee -> {
                    lateFeeMapper.updateEntityFromDto(lateFeeDto, lateFee);
                    LateFee updatedLateFee = lateFeeRepository.save(lateFee);
                    return lateFeeMapper.toDto(updatedLateFee);
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
            return Optional.of(lateFeeMapper.toDto(createdLateFee));
        } else {
            return Optional.empty();
        }
    }

    public List<LateFeeDto> getLateFeesByBorrowID(final Long borrowId) {
        return borrowRepository.findById(borrowId)
                .map(lateFeeRepository::findLateFeesByBorrow)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Borrow not found with the given ID", borrowId))
                .stream()
                .map(lateFeeMapper::toDto)
                .collect(Collectors.toList());
    }

}
