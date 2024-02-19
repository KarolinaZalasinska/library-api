package com.example.libraryapi.users;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class PasswordResetService {
    private final UserRepository userRepository;
    private final String emailPassword;

    public PasswordResetService(UserRepository userRepository, String emailPassword) {
        this.userRepository = userRepository;
        this.emailPassword = emailPassword;
    }

    // Metoda główna do rozpoczęcia procesu resetowania hasła
    public void initiatePasswordReset(String username) throws MessagingException {
        // Wyszukanie użytkownika w bazie danych na podstawie nazwy użytkownika
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Generowanie kodu weryfikacyjnego
        String verificationCode = generateVerificationCode();

        // Wysłanie kodu weryfikacyjnego na adres e-mail użytkownika
        sendVerificationCode(user.getEmail(), verificationCode);
    }

    // Metoda do generowania losowego kodu weryfikacyjnego
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Kod z zakresu 100000-999999
        return String.valueOf(code);
    }

    // Metoda do wysyłania kodu weryfikacyjnego na adres e-mail użytkownika
    public void sendVerificationCode(final String userEmail, final String verificationCode) throws MessagingException {
        // Konfiguracja parametrów serwera SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Dane uwierzytelniające do serwera SMTP
        final String username = "your_email@example.com";

        // Utworzenie sesji
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, emailPassword);
            }
        });

        // Tworzenie wiadomości e-mail
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Verification Code for Password Reset");
            message.setText("Your verification code for password reset is: " + verificationCode);

            // Wysłanie wiadomości e-mail
            Transport.send(message);
        } catch (AddressException e) {
            throw new MessagingException("Invalid email address: " + userEmail, e);
        } catch (javax.mail.MessagingException e) {
            throw new MessagingException("Error occurred while sending email message", e);
        }
    }

    // Metoda do resetowania hasła po poprawnej weryfikacji kodu
    public void resetPassword(String username, String newPassword, PasswordEncoder passwordEncoder) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        user.setPassword(passwordEncoder.encode(newPassword));

        // Zapisz zmiany w bazie danych
        userRepository.save(user);
    }


}
