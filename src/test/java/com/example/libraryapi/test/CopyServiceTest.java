//package com.example.libraryapi.test;
//
//import com.example.libraryapi.LibraryApiApplication;
//import com.example.libraryapi.dto.CopyDto;
//import com.example.libraryapi.model.Copy;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//import com.example.libraryapi.repository.CopyRepository;
//import com.example.libraryapi.service.CopyService;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@SpringBootTest
//public class CopyServiceTest {
//    @Mock
//    private CopyRepository copyRepositoryMock;
//
//    @Mock
//    private ModelMapper modelMapperMock;
//
//    @InjectMocks
//    private CopyService copyService;
//
//    @Test
//    void shouldReturnCurrentlyBorrowedCopies() {
//        // given
//        LocalDate currentDate = LocalDate.now();
//        Copy copy1 = new Copy();
//        copy1.setBorrowDate(currentDate);
//        Copy copy2 = new Copy();
//        copy2.setBorrowDate(currentDate);
//        List<Copy> currentlyBorrowedCopies = Arrays.asList(copy1, copy2);
//
//        Mockito.when(copyRepositoryMock.findCurrentlyBorrowedCopies(currentDate)).thenReturn(currentlyBorrowedCopies);
//
//        Mockito.when(modelMapperMock.map(copy1, CopyDto.class))
//                .thenReturn(new CopyDto(1L, currentDate, currentDate.atStartOfDay(), currentDate.plusDays(30).atStartOfDay(), currentDate.atStartOfDay(), List.of()));
//
//        Mockito.when(modelMapperMock.map(copy2, CopyDto.class))
//                .thenReturn(new CopyDto(2L, currentDate, currentDate.atStartOfDay(), currentDate.plusDays(30).atStartOfDay(), currentDate.atStartOfDay(), List.of()));
//
//        // when
//        List<CopyDto> result = copyService.getCurrentlyBorrowedCopies();
//
//        // then
//        assertThat(result).isNotEmpty();
//        assertThat(result.size()).isEqualTo(2);
//
//        for (CopyDto copyDto : result) {
//            assertThat(copyDto.id()).isNotNull();
//            assertThat(copyDto.purchaseDate()).isNotNull();
//            assertThat(copyDto.borrowDate()).isNotNull();
//            assertThat(copyDto.expectedReturnDate()).isNotNull();
//            assertThat(copyDto.returnDate()).isNotNull();
//            assertThat(copyDto.borrows()).isNotNull();
//
//            assertThat(copyDto.borrowDate()).isEqualTo(currentDate);
//        }
//    }
//}
//
//
