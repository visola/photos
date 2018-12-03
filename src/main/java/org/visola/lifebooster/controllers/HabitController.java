package org.visola.lifebooster.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.visola.lifebooster.dao.HabitDao;
import org.visola.lifebooster.model.Habit;
import org.visola.lifebooster.model.User;

@RequestMapping("${api.base.path}/habits")
@RestController
public class HabitController {

  private final HabitDao habitDao;

  public HabitController(HabitDao habitDao) {
    this.habitDao = habitDao;
  }

  @PostMapping
  public Habit create(@RequestBody Habit habit, @AuthenticationPrincipal User user) {
    long now = System.currentTimeMillis();
    habit.setCreated(now);
    habit.setUpdated(now);
    habit.setUserId(user.getId());
    habit.setId(habitDao.create(habit));

    return habit;
  }

  @GetMapping
  public List<Habit> fetch(@AuthenticationPrincipal User user) {
    return habitDao.findByUserId(user.getId());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Habit> update(@PathVariable("id") long habitId,
      @RequestBody Habit habit,
      @AuthenticationPrincipal User user) {
    Optional<Habit> maybeLoaded = habitDao.findByIdAndUserId(habitId, user.getId());

    if (!maybeLoaded.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Habit loaded = maybeLoaded.get();
    BeanUtils.copyProperties(habit, loaded, "created", "updated");
    loaded.setUpdated(System.currentTimeMillis());

    habitDao.update(loaded);
    return ResponseEntity.ok(loaded);
  }

}
