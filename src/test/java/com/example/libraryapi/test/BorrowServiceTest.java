//package com.example.libraryapi.test;
//
//import com.example.libraryapi.LibraryApiApplication;
//import com.example.libraryapi.dto.ClientDto;
//import com.example.libraryapi.dto.CopyDto;
//import com.example.libraryapi.model.Borrow;
//import com.example.libraryapi.model.Client;
//import com.example.libraryapi.model.Copy;
//import com.example.libraryapi.repository.BorrowRepository;
//import com.example.libraryapi.service.BorrowService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//@SpringBootTest(classes = LibraryApiApplication.class)
//
//public class BorrowServiceTest {
//
//    @Mock
//    private BorrowRepository borrowRepositoryMock;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private BorrowService borrowService;
//
//    @Test
//    void shouldCreateBorrowRecord() {
//        // Given
//        CopyDto copyDto = new CopyDto(
//                1L,
//                LocalDate.of(2023, 12, 1),
//                LocalDateTime.now(),
//                null,
//                null,
//                List.of()
//        );
//
//        ClientDto clientDto = new ClientDto(
//                2L,
//                "Jan",
//                "Nowak",
//                "jan.nowak@example.com",
//                "Nowak.jan1234"
//        );
//
//        Copy copy = new Copy();
//        Client client = new Client();
//
//        when(modelMapper.map(copyDto, Copy.class)).thenReturn(copy);
//        when(modelMapper.map(clientDto, Client.class)).thenReturn(client);
//        when(borrowRepositoryMock.save(any(Borrow.class))).thenReturn(new Borrow());
//
//        // When
//        Borrow createdBorrow = borrowService.createBorrowRecord(copy, client);
//
//        // Then
//        verify(borrowRepositoryMock).save(any(Borrow.class));
//
//        assertEquals(copy, createdBorrow.getCopy());
//        assertEquals(client, createdBorrow.getClient());
//
//    }
//}
