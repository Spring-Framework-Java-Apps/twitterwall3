package org.woehlke.twitterwall.oodm.model;

import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.twitterwall.oodm.model.common.DomainObjectMinimal;
import org.woehlke.twitterwall.oodm.model.tasks.TaskSendType;
import org.woehlke.twitterwall.oodm.model.tasks.TaskStatus;
import org.woehlke.twitterwall.oodm.model.tasks.TaskType;
import org.woehlke.twitterwall.oodm.model.listener.TaskListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by tw on 09.07.17.
 */
@Entity
@Table(
    name = "task",
    uniqueConstraints = {
        @UniqueConstraint(name="unique_task",columnNames = {"task_type","time_started"})
    },
    indexes = {
        @Index(name = "idx_task_time_finished", columnList = "time_finished"),
        @Index(name = "idx_task_task_status", columnList = "task_status"),
        @Index(name = "idx_task_task_type", columnList = "task_type"),
        @Index(name = "idx_task_send_type", columnList = "send_type"),
    }
)
@NamedQueries({
    @NamedQuery(
        name="Task.findByUniqueId",
        query="select t from Task t where t.taskType=:taskType and t.timeStarted=:timeStarted"
    )
})
@EntityListeners(TaskListener.class)
public class Task implements DomainObjectMinimal<Task> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(columnDefinition="text",nullable = false)
    private String description = "NULL";

    @NotNull
    @Column(name="task_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType taskType = TaskType.NULL;

    @NotNull
    @Column(name="task_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.READY;

    @NotNull
    @Column(name="send_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskSendType taskSendType = TaskSendType.NULL;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_started",nullable = false)
    private Date timeStarted = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_last_update",nullable = false)
    private Date timeLastUpdate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="time_finished")
    private Date timeFinished = null;

    private Task() {
    }

    public Task(String description, TaskType taskType, TaskStatus taskStatus, TaskSendType taskSendType, Date timeStarted, Date timeLastUpdate, Date timeFinished) {
        this.description = description;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
        this.taskSendType = taskSendType;
        this.timeStarted = timeStarted;
        this.timeLastUpdate = timeLastUpdate;
        this.timeFinished = timeFinished;
    }

    @Transient
    @Override
    public String getUniqueId() {
        return "" + taskType.name() +"_"+ timeStarted.getTime();
    }

    @Transient
    @Override
    public boolean isValid() {
        if(taskType == null){
            return false;
        }
        if(taskType == TaskType.NULL){
            return false;
        }
        if(timeStarted == null){
            return false;
        }
        if(timeStarted.after(new Date())){
            return false;
        }
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(Date timeFinished) {
        this.timeFinished = timeFinished;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskSendType getTaskSendType() {
        return taskSendType;
    }

    public void setTaskSendType(TaskSendType taskSendType) {
        this.taskSendType = taskSendType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeLastUpdate() {
        return timeLastUpdate;
    }

    public void setTimeLastUpdate(Date timeLastUpdate) {
        this.timeLastUpdate = timeLastUpdate;
    }

    public void setTimeLastUpdate() {
        this.timeLastUpdate = new Date();
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", taskStatus=" + taskStatus +
                ", taskSendType=" + taskSendType +
                ", timeStarted=" + timeStarted +
                ", timeLastUpdate=" + timeLastUpdate +
                ", timeFinished=" + timeFinished +
                '}';
    }

    @Override
    public int compareTo(Task other) {
        return getUniqueId().compareTo(other.getUniqueId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (getId() != null ? !getId().equals(task.getId()) : task.getId() != null) return false;
        if (getTaskType() != task.getTaskType()) return false;
        return getTimeStarted() != null ? getTimeStarted().equals(task.getTimeStarted()) : task.getTimeStarted() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTaskType() != null ? getTaskType().hashCode() : 0);
        result = 31 * result + (getTimeStarted() != null ? getTimeStarted().hashCode() : 0);
        return result;
    }
}
