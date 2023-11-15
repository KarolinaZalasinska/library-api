package com.example.libraryapi;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.model.Copy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.libraryapi.repository.CopyRepository;
import com.example.libraryapi.service.CopyService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LibraryApiApplication.class) // oznacza test integracyjny
public class CopyServiceTest {

    @Mock
    private CopyRepository copyRepository;

    @InjectMocks
    private CopyService copyService;

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);
    private static final LocalDate TWO_DAYS_AGO = TODAY.minusDays(2);

    @Test
    public void testFindCurrentlyBorrowedCopies() {
        // Given
        Copy copy1 = createBorrowedCopy(1L, YESTERDAY, null);
        Copy copy2 = createBorrowedCopy(2L, TWO_DAYS_AGO, null);

        List<Copy> mockCopies = Arrays.asList(copy1, copy2);

        // When
        when(copyRepository.findCurrentlyBorrowedCopies(LocalDate.now())).thenReturn(mockCopies);

        // Then
        List<CopyDto> result = copyService.getCurrentlyBorrowedCopies();

        assertEquals(2, result.size(), "Expected 2 borrowed copies");
        assertEquals(1L, result.get(0).getId(), "Expected ID of the first copy to be 1");
        assertEquals(2L, result.get(1).getId(), "Expected ID of the second copy to be 2");
    }

    private Copy createBorrowedCopy(Long id, LocalDate borrowedDate, LocalDate returnDate) {
        Copy copy = new Copy();
        copy.setId(id);
        copy.setBorrowedDate(borrowedDate);
        copy.setReturnDate(returnDate);
        return copy;
    }
}

