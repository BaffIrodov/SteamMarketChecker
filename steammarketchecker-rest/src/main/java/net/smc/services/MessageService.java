package net.smc.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.smc.entities.Message;
import net.smc.entities.QMessage;
import net.smc.entities.User;
import net.smc.repositories.MessageRepository;
import net.smc.repositories.UserRepository;
import net.smc.dto.MessageDto;
import net.smc.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final JPAQueryFactory queryFactory;

    private final EmailService emailService;

    private final UserService userService;

    private final UserRepository userRepository;

    public static final QMessage message = QMessage.message;

    @Transactional
    public void sendMessageOfNewRequests() {
//        List<Message> messages = messageRepository.findAll();
//        List<Message> newMessages = messages.stream().filter(e -> e.getEmailSign().equals(false)
//                && e.getType().equals(Types.newRequests)).toList();
//        String numbers = String.join(",", newMessages.stream().map(Message::getObjectName).toList());
//        List<String> emails = userService.getEmailsByRole("chief");
//        emails.forEach(email -> {
//            emailService.sendSimpleMessage(email,
//                    "Поступили новые заказы",
//                    ("Количество заказов: " + newMessages.size() + "\n"
//                            + "Номера заказов: " + numbers));
//        });
//        newMessages.forEach(e -> e.setEmailSign(true));
//        messageRepository.saveAll(newMessages);
//        System.out.println("Отправил сообщение о новых реквестах");
    }

    public List<MessageDto> getNewMessagesForFrontendByLogin(String userLogin) {
        List<Message> newMessages = queryFactory
                .from(message)
                .select(message)
                .where(message.login.eq(userLogin)
                        .and(message.frontSign.eq(false)
                                .or(message.frontSign.isNull())))
                .fetch();
        return newMessages.stream().map(MessageDto::new).toList();
    }

    @Transactional
    public void setMessageReadFromFrontend(List<Long> messageIds) {
        List<Message> messages = messageRepository.findAllById(messageIds);
        messages.forEach(e -> e.setFrontSign(true));
        messageRepository.saveAll(messages);
        messageRepository.flush();
    }

//    @Scheduled(fixedDelay = 10000)
    public void scheduledSendEmailMessages() {
        List<Message> newMessages = queryFactory.select(message)
                .from(message)
                .where(message.emailSign.isFalse())
                .fetch();

        newMessages.forEach(message -> {
            UserDto user = userService.getUser(message.getLogin());
            emailService.sendSimpleMessage(user.getEmail(), message.getObjectName(), message.getObjectName());
        });

        newMessages.forEach(e -> e.setEmailSign(true));
        messageRepository.saveAll(newMessages);
//        System.out.println("Отправил сообщения на почты, по шедулеру");
    }

    public Message createMessageByLogin(String login, String messageText, Long objectId) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            Message message = new Message(user.login,
                    false,
                    false,
                    objectId,
                    messageText);
            message = messageRepository.save(message);
            return message;
        } else {
            throw new RuntimeException("User не найден");
        }
    }

    public List<Message> createMessagesByRole(String role, Long objectId, String messageText) {
        List<Message> messageList = new ArrayList<>();
        List<User> actualUsers = userRepository.findAllByRole(role);
        for (User user : actualUsers) {
            Message message = new Message(user.login, false, false, objectId, messageText);
            messageList.add(message);
        }
        messageRepository.saveAll(messageList);
        return messageList;
    }

    public List<Message> createMessagesForAllUsers(Long objectId, String messageText) {
        List<Message> messageList = new ArrayList<>();
        List<User> actualUsers = userRepository.findAll();
        for (User user : actualUsers) {
            Message message = new Message(user.login, false, false, objectId, messageText);
            messageList.add(message);
        }
        messageList = messageRepository.saveAll(messageList);
        return messageList;
    }
}
