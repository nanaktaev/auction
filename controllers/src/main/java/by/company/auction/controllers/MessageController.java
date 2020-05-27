package by.company.auction.controllers;

import by.company.auction.dto.MessageDto;
import by.company.auction.services.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<MessageDto> getMessagesByUserId(@RequestParam(required = false) Integer userId) {

        log.debug("getMessagesByUserId() userId = {}", userId);

        return messageService.findMessagesByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Integer id) {

        log.debug("deleteMessage() id = {}", id);

        messageService.delete(id);
    }

}