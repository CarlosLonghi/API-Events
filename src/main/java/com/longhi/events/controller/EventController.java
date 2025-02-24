package com.longhi.events.controller;

import com.longhi.events.model.Event;
import com.longhi.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/api/v1/events")
    public Event addNewEvent(@RequestBody Event newEvent) {
        return eventService.addNewEvent(newEvent);
    }

    @GetMapping("/api/v1/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/api/v1/events/{prettyName}")
    public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
        Event event = eventService.getByPrettyName(prettyName);
        
        if (event != null) {
            return ResponseEntity.ok().body(event);
        }

        return ResponseEntity.notFound().build();
    }
}
