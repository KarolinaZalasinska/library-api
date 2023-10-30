package service;

import dto.CopyDto;
import exception.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import mapper.CopyMapper;
import model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;

import java.awt.desktop.PreferencesEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class CopyService {
    public final CopyRepository copyRepository;
    public final LoanRepository loanRepository;
    public final UserRepository userRepository;
    public final LateFeeRepository lateFeeRepository;
    public final UserActivityRepository userActivityRepository;
    public final CopyMapper mapper;

    @Transactional
    public CopyDto createCopy(final CopyDto copyDto) {
        Copy copy = mapper.toEntity(copyDto);
        Copy savedCopy = copyRepository.save(copy);
        return mapper.toDto(savedCopy);
    }

    public CopyDto getCopyById(final Long id) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);
        return optionalCopy.map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The copy with the given ID could not be found.", id));
    }

    public List<CopyDto> getAllCopies() {
        List<Copy> copies = copyRepository.findAll();
        if (copies.isEmpty()) {
            return Collections.emptyList();
        } else {
            return copies.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CopyDto updateCopy(final Long id, CopyDto copyDto) {
        return copyRepository.findById(id)
                .map(copy -> {
                    mapper.updateEntityFromDto(copyDto, copy);
                    Copy updatedCopy = copyRepository.save(copy);
                    return mapper.toDto(updatedCopy);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update the copy with the given ID.", id));
    }

    @Transactional
    public void deleteCopy(final Long id) {
        copyRepository.deleteById(id);
    }

    @Transactional
    public void borrowCopy(final Long copyId, final Long userId) {
        // Pobierz kopię z repozytorium
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Egzemplarz o podanym ID nie został odnaleziony.", copyId));

        // Upewnij się, że kopia jest dostępna do wypożyczenia
        if (!isCopyAvailable(copyId)) {
            throw new CopyNotAvailableException("Egzemplarz nie jest dostępny do wypożyczenia.");
        }

        // Pobierz użytkownika z repozytorium
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Użytkownik o podanym ID nie został odnaleziony.", userId));

        LocalDate currentDate = LocalDate.now();
        LocalDate expectedReturnDate = currentDate.plusDays(30);

        // Utwórz nowy obiekt Loan i zapisz go w repozytorium
        Loan loan = new Loan();
        loan.setBook(copy.getBook());
        loan.setUser(user);
        loan.setDateOfBorrow(currentDate);
        loan.setReturnDate(expectedReturnDate);

        loanRepository.save(loan);

        // Zaktualizuj pole borrowedDate w obiekcie Copy
        copy.setBorrowedDate(currentDate);
        copyRepository.save(copy);

        // Utwórz nowy obiekt UserActivity i zapisz go w UserActivityRepository
        UserActivity userActivity = new UserActivity();
        userActivity.setUser(user);
        userActivity.setAction("borrow"); // Ustaw odpowiednią akcję, np. "borrow"
        userActivity.setCopy(copy);
        userActivity.setBorrowDate(currentDate);
        userActivityRepository.save(userActivity);
    }

    @Transactional
    public void returnCopy(final Long id, final Long userId) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The copy with the specified ID could not be found.", id));

        if (copy.getBorrowedDate() == null || copy.getExpectedReturnDate() != null) {
            throw new InvalidCopyReturnException("This copy cannot be returned.");
        }

        LocalDate currentDate = LocalDate.now();

        Loan loan = loanRepository.findLoanByCopyAndUserAndReturnDateIsNull(copy, userId);
        if (loan == null) {
            throw new ObjectNotFoundInRepositoryException("The loan for the specified copy and user could not be found.", id);
        }

        LocalDate returnDate = currentDate;

        if (returnDate.isAfter(loan.getCopy().getExpectedReturnDate())) {
            LocalDate plannedReturnDate = loan.getCopy().getExpectedReturnDate();
            int daysLate = (int) ChronoUnit.DAYS.between(plannedReturnDate, returnDate);
            double lateFeePerDay = 5.0; // Przykładowa stawka opłaty za dzień spóźnienia
            double lateFee = daysLate * lateFeePerDay;

            // Zapisz opłatę za spóźnienie do bazy danych
            LateFee lateFeeRecord = new LateFee();
            lateFeeRecord.setLoan(loan);
            lateFeeRecord.setAmount(lateFee);
            lateFeeRecord.setDate(returnDate);
            lateFeeRepository.save(lateFeeRecord);
        }

        loan.setReturnDate(returnDate);
        loanRepository.save(loan);

        copy.setExpectedReturnDate(returnDate);
        copyRepository.save(copy);

        // Utwórz nowy obiekt UserActivity i zapisz go w UserActivityRepository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Użytkownik o podanym ID nie został odnaleziony.", userId));

        UserActivity userActivity = new UserActivity();
        userActivity.setUser(user);
        userActivity.setAction("return"); // Ustaw odpowiednią akcję, np. "return"
        userActivity.setCopy(copy);
        userActivity.setReturnDate(returnDate);
        userActivityRepository.save(userActivity);
    }


    public List<CopyDto> getCopiesForBook(final Long bookId) {
        List<Copy> copies = copyRepository.findByBookId(bookId);
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CopyDto> getAvailableCopiesForBook(final Long bookId) {
        List<Copy> copies = copyRepository.findAvailableCopiesForBook(bookId, LocalDate.now());
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean isCopyAvailable(final Long id) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);
        if (optionalCopy.isEmpty()) {
            return false;
        }

        Copy copy = optionalCopy.get();

        if (copy.getBorrowedDate() == null) {
            return true;
        } else if (copy.getReturnDate() == null) {
            return false;
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalDate returnDate = copy.getReturnDate();

            return returnDate.isBefore(currentDate);
        }
    }

    public List<CopyDto> getBorrowedCopiesForUser(final Long userId) {
        List<Copy> copies = copyRepository.findBorrowedCopiesForUser(userId);
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CopyDto> getOverdueCopies() {
        List<Copy> copies = copyRepository.findOverdueCopies(LocalDate.now());
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}
