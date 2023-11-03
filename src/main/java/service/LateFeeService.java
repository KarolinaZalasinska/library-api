package service;

import dto.LateFeeDto;
import exception.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mapper.LateFeeMapper;
import model.Borrow;
import model.LateFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.BorrowRepository;
import repository.LateFeeRepository;

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

}
