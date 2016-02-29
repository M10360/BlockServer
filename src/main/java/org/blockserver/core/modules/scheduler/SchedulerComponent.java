/*
 * This file is part of BlockServer.
 *
 * BlockServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlockServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BlockServer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.blockserver.core.modules.scheduler;

import lombok.Getter;
import org.blockserver.core.modules.thread.ExecutorComponent;
import org.blockserver.core.server.module.Component;
import org.blockserver.core.server.module.JarComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Written by Exerosis!
 *
 * @author BlockServer Team
 * @see JarComponent
 */
public class SchedulerComponent implements Component {
    @Getter private final Map<Runnable, TaskData> tasks = new HashMap<>();
    private final ExecutorComponent executorComponent;

    public SchedulerComponent(ExecutorComponent executorComponent) {
        this.executorComponent = executorComponent;
    }

    //TODO maybe make this better!
    @Override
    public void enable() {
        executorComponent.getExecutorService().execute(() -> {
            while (isEnabled()) {
                for (Map.Entry<Runnable, TaskData> entry : tasks.entrySet()) {
                    TaskData taskData = entry.getValue();
                    if (taskData.getNextTickTime() > System.currentTimeMillis())
                        continue;
                    taskData.repeatTimes--;
                    //So by doing this every task will be run at the same time... not in series... is that ok?
                    executorComponent.getExecutorService().execute(() -> entry.getKey().run());
                    //
                    if (taskData.getRepeatTimes() <= 0)
                        tasks.remove(entry.getKey());
                    taskData.setLastTickTime(System.currentTimeMillis());
                }
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Component.super.enable();
    }

    @Override
    public void disable() {
        tasks.clear();
        Component.super.disable();
    }

    public void registerTask(Runnable task, double delay) {
        registerTask(task, delay, 1);
    }

    public void registerTask(Runnable task, int repeatTimes) {
        registerTask(task, 1, repeatTimes);
    }

    public void registerTask(Runnable task, double delay, int repeatTimes) {
        registerTask(task, new TaskData(delay, repeatTimes));
    }

    public void registerTask(Runnable task, TaskData taskData) {
        synchronized (tasks) {
            tasks.put(task, taskData);
        }
    }

    public TaskData getTaskData(Runnable task) {
        synchronized (tasks) {
            return tasks.get(task);
        }
    }

    public void setTaskData(Runnable task, TaskData taskData) {
        synchronized (tasks) {
            tasks.put(task, taskData);
        }
    }

    public void setTaskDelay(Runnable task, double delay) {
        getTaskData(task).setDelay(delay);
    }

    public void setTaskRepeatTimes(Runnable task, int repeatTimes) {
        getTaskData(task).setRepeatTimes(repeatTimes);
    }

    public double getTaskDelay(Runnable task) {
        return getTaskData(task).getDelay();
    }

    public int getTaskRepeatTimes(Runnable task) {
        return getTaskData(task).getRepeatTimes();
    }

    public void cancelTask(Runnable task) {
        synchronized (tasks) {
            tasks.remove(task);
        }
    }
}
