package org.visola.lifebooster.controllers;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

}
