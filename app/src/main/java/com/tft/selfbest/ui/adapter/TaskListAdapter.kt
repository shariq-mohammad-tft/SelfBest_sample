package com.tft.selfbest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.overview.CourseDetail

class TaskListAdapter(val list: List<CourseDetail>) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TaskListAdapter.TaskViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.task_detail_row, parent, false)
        return TaskViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: TaskListAdapter.TaskViewHolder, position: Int) {
        val course = list[position]
        holder.courseName.text = course.courseName
        holder.status.text = """${course.completedModules}/${course.modules}"""
        holder.progressBar.progress = course.progressStatus
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var courseName: TextView = view.findViewById(R.id.course_name) as TextView
        var status: TextView = view.findViewById(R.id.course_status) as TextView
        var progressBar: ProgressBar = view.findViewById(R.id.course_progress_bar)
    }
}