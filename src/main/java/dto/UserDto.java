package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Imię jest wymagane.")
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane")
    private String lastName;

    @Email(message = "Nieprawidłowy adres email.")
    @NotBlank(message = "Email jest wymagany.")
    private String email;

    private List<BorrowDto> loans; // Zostawić ?????????????? Jeżeli tak, to:
    // Decyzja o tym, czy w klasie DTO użytkownika (UserDto) powinno znajdować się pole loans, czyli lista wypożyczeń,
    // zależy od potrzeb Twojej aplikacji i sposobu, w jaki jest ona projektowana.
    //
    //Ogólnie rzecz biorąc, jeśli Twoja aplikacja będzie wykorzystywała informacje o wypożyczeniach w kontekście użytkownika,
    // na przykład do wyświetlania historii wypożyczeń w panelu użytkownika, to warto dodać pole loans do UserDto.
    // To umożliwi łatwe dostarczanie tych informacji na stronę klienta. Jednak należy zwrócić uwagę na kilka rzeczy:
    //
    //Prywatność i bezpieczeństwo: Upewnij się, że w przypadku, gdy dane o wypożyczeniach są dostępne przez UserDto,
    // są odpowiednio zabezpieczone i dostępne tylko dla właściciela konta użytkownika.
    //
    //Efektywność: W przypadku dużego systemu z wieloma użytkownikami i wieloma wypożyczeniami,
    // przesyłanie wszystkich wypożyczeń wraz z danymi użytkownika może spowolnić przesyłanie danych.
    // W takim przypadku może być bardziej efektywne pobieranie tych informacji na żądanie
    // (np. po wybraniu historii wypożyczeń w panelu użytkownika).
    //
    //Komunikacja międzywarstwowa: Jeśli UserDto jest używane do komunikacji między warstwami aplikacji (np. REST API),
    // to zawarcie listy wypożyczeń może ułatwić korzystanie z tych informacji w przypadku, gdy są potrzebne w jednym zapytaniu.
    //
    //Podsumowując, decyzja o tym, czy dodać pole loans do UserDto zależy od kontekstu aplikacji i jej wymagań.
    // Jeśli dane o wypożyczeniach użytkownika są często używane w kontekście użytkownika, może to być uzasadnione,
    // pod warunkiem odpowiedniego zabezpieczenia tych danych.
}
