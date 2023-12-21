package com.example.libraryapi.service;

import com.example.libraryapi.dto.LateFeeDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.LateFee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BorrowRepository;
import com.example.libraryapi.repository.LateFeeRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing late fees.
 */
@Service
@RequiredArgsConstructor
public class LateFeeService {

    private final LateFeeRepository lateFeeRepository;
    private final BorrowRepository borrowRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new late fee record based on the provided LateFeeDto.
     *
     * @param lateFeeDto The LateFeeDto containing information for the new late fee.
     * @return The created LateFeeDto.
     */
    @Transactional
    public LateFeeDto createLateFee(@Valid LateFeeDto lateFeeDto) {
        Borrow borrow = getBorrowById(lateFeeDto.id());

        LateFee lateFee = modelMapper.map(lateFeeDto, LateFee.class);
        setBorrowForLateFee(lateFee, borrow);

        return modelMapper.map(lateFeeRepository.save(lateFee), LateFeeDto.class);
    }
    private void setBorrowForLateFee(@NonNull LateFee lateFee, @NonNull Borrow borrow) {
        lateFee.setBorrow(borrow);
    }

    /**
     * Retrieves the late fee information by lateFeeId.
     *
     * @param lateFeeId The identifier for the late fee.
     * @return The LateFeeDto associated with the given lateFeeId.
     */
    public LateFeeDto getLateFeeById(final Long lateFeeId) {
        LateFee lateFee = getLateFeeOrThrow(lateFeeId);
        return modelMapper.map(lateFee, LateFeeDto.class);
    }

    /**
     * Retrieves a list of all late fees.
     *
     * @return A List of LateFeeDto representing all late fees.
     */
    public List<LateFeeDto> getAllLateFees() {
        return lateFeeRepository.findAll().stream()
                .map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of late fees associated with a specific borrow.
     *
     * @param borrowId The identifier for the borrow.
     * @return A List of LateFeeDto associated with the given borrowId.
     */
    public List<LateFeeDto> getLateFeesByBorrowId(final Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ObjectNotFoundException("Borrow with id " + borrowId + " was not found."));

        List<LateFee> lateFees = lateFeeRepository.findLateFeesByBorrow(borrow);
        return lateFees.stream()
                .map(lateFee -> modelMapper.map(lateFee, LateFeeDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing late fee record based on the provided fields to update.
     *
     * @param id             The identifier for the late fee to be updated.
     * @param fieldsToUpdate A map containing field names and their updated values.
     */
    public void updateLateFee(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        LateFee lateFee = getLateFeeOrThrow(id);

        Map<String, BiConsumer<LateFee, String>> fieldSetters = Map.of(
                "lateFeeDate", (lf, val) -> {
                    LocalDate lateFeeDate = LocalDate.parse(val);
                    validateLateFeeDate(lateFeeDate);
                    lf.setLateFeeDate(lateFeeDate);
                },
                "amount", (lf, val) -> {
                    BigDecimal amount = new BigDecimal(val);
                    validateAmount(amount);
                    lf.setAmount(amount);
                }
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (lf, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(lateFee, value);
        }
    }

    private void validateLateFeeDate(LocalDate lateFeeDate) {
        if (lateFeeDate == null || lateFeeDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Late fee date must be in the past or present.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
    }

    /**
     * Deletes a late fee record by its identifier.
     *
     * @param id The identifier for the late fee to be deleted.
     */
    @Transactional
    public void deleteLateFee(Long id) {
        LateFee lateFee = getLateFeeOrThrow(id);
        lateFeeRepository.delete(lateFee);
    }

    /**
     * Calculates the late fee amount based on the expected return date and the actual return date.
     *
     * @param expectedReturnDate The expected return date.
     * @param returnDate         The actual return date.
     * @return The calculated late fee amount.
     */
    private BigDecimal calculateLateFeeForReturn(LocalDateTime expectedReturnDate, LocalDateTime returnDate) {
        if (returnDate.isAfter(expectedReturnDate)) {
            long daysLate = ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
            BigDecimal lateFeePerDay = BigDecimal.valueOf(5.0);
            return lateFeePerDay.multiply(BigDecimal.valueOf(daysLate));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calculates and records a late fee for a specific borrow.
     *
     * @param borrowId The identifier for the borrow.
     * @return An Optional containing the LateFeeDto if a late fee is recorded, or empty otherwise.
     */
    public Optional<LateFeeDto> calculateAndRecordLateFee(final Long borrowId) {
        Borrow borrow = getBorrowById(borrowId);

        BigDecimal lateFeeAmount = calculateLateFeeForReturn(borrow.getExpectedReturnDate(), borrow.getReturnDate());

        if (lateFeeAmount.compareTo(BigDecimal.ZERO) > 0) {
            LateFee lateFeeRecord = new LateFee();
            lateFeeRecord.setBorrow(borrow);
            lateFeeRecord.setAmount(lateFeeAmount);
            lateFeeRecord.setLateFeeDate(borrow.getReturnDate().toLocalDate());

            LateFee createdLateFee = lateFeeRepository.save(lateFeeRecord);
            return Optional.of(modelMapper.map(createdLateFee, LateFeeDto.class));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieves a Borrow object by its identifier, throwing ObjectNotFoundException if not found.
     *
     * @param borrowId The identifier for the borrow.
     * @return The Borrow object associated with the given borrowId.
     * @throws ObjectNotFoundException if the borrow is not found.
     */
    private Borrow getBorrowById(final Long borrowId) {
        return borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ObjectNotFoundException("Borrow with id " + borrowId + " was not found."));
    }

    /**
     * Retrieves a LateFee object by its identifier, throwing ObjectNotFoundException if not found.
     *
     * @param lateFeeId The identifier for the late fee.
     * @return The LateFee object associated with the given lateFeeId.
     * @throws ObjectNotFoundException if the late fee is not found.
     */
    LateFee getLateFeeOrThrow(final Long lateFeeId) {
        return lateFeeRepository.findById(lateFeeId)
                .orElseThrow(() -> new ObjectNotFoundException("Late fee with id " + lateFeeId + " was not found."));
    }
}
