package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/room")
public class RoomController {

    private RoomRepository repository;

    public RoomController(RoomRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = repository.findById(id);
        return new ResponseEntity<>(room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Room> createOrUpdate(@RequestBody Room room) {
        return new ResponseEntity<>(repository.save(room), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        repository.delete(room);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Set<Message>> messagesInRoom(@PathVariable int id) {
        var room = this.repository.findById(id);
        Set<Message> rsl = room.isPresent() ? room.get().getMessages() : new HashSet<>();
        return new ResponseEntity<>(rsl, room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
