package net.smc.services;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.entities.PasswordRecoverToken;
import net.smc.entities.QUser;
import net.smc.entities.User;
import net.smc.repositories.PasswordRecoverTokenRepository;
import net.smc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordRecoverService {

    @Value("${token.recover-clean-in-sec}")
    private String recoverCleanPeriod;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PasswordRecoverTokenRepository passwordRecoverTokenRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private EmailService emailService;

    private QUser user = new QUser("user");

    public Boolean sendRecoverLetter(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            this.deleteOldRecoverCodeByEmail(email);
            PasswordRecoverToken passwordRecoverToken = new PasswordRecoverToken(email);
            passwordRecoverTokenRepository.save(passwordRecoverToken);
            emailService.sendSimpleMessage(user.getEmail(),
                    "Смена пароля",
                    "Для того, чтобы сменить пароль в системе, введите код: "
                            + passwordRecoverToken.getSimpleConfirmationToken());
            return true;
        } else {
            throw new RuntimeException("Введенный email не найден");
        }
    }

    private void deleteOldRecoverCodeByEmail(String email) {
        PasswordRecoverToken passwordRecoverTokenOld = passwordRecoverTokenRepository.findPasswordRecoverTokenByEmail(email);
        if (passwordRecoverTokenOld != null) {
            passwordRecoverTokenRepository.delete(passwordRecoverTokenOld);
            passwordRecoverTokenRepository.flush();
        }
    }

    //    @Scheduled(fixedDelay = 300000)
    private void deleteOldRecoverCodesBySchedule() {
        List<PasswordRecoverToken> allTokens = passwordRecoverTokenRepository.findAll();
        List<PasswordRecoverToken> allOldTokens = allTokens.stream().filter(e ->
                        (Instant.now().getEpochSecond() - e.getCreateInstant().getEpochSecond()) > Integer.parseInt(recoverCleanPeriod))
                .toList();
        passwordRecoverTokenRepository
                .deleteAllById(allOldTokens.stream().map(PasswordRecoverToken::getId).collect(Collectors.toList()));
    }

    public Boolean verifyRecoverCode(String recoverCode, String email) {
        PasswordRecoverToken token = passwordRecoverTokenRepository.findPasswordRecoverTokenByEmail(email);
        if (token != null) {
            if (token.getSimpleConfirmationToken().equals(recoverCode)) {
                return true;
            } else {
                throw new RuntimeException("Введенный код неправильный, повторите ввод ещё раз");
            }
        } else {
            throw new RuntimeException("Ваш токен устарел, пройдите процедуру заново");
        }
    }

    public Boolean saveNewPassword(String newPassword, String email) {
        User user = this.userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.saveAndFlush(user);
        return true;
    }

}
